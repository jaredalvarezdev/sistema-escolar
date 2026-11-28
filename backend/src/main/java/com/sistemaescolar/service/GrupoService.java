package com.sistemaescolar.service;

import com.sistemaescolar.dto.GrupoRequest;
import com.sistemaescolar.model.Grupo;
import com.sistemaescolar.model.Horario;
import com.sistemaescolar.repository.GrupoRepository;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;

/*
- Servicio encargado de la lógica de negocio de los grupos académicos,
- incluyendo el mapeo desde DTO y la validación de horarios simultáneos por docente.
 */
/**/
@Service // Registra esta clase como un bean de servicio administrado por Spring
public class GrupoService {

    private final GrupoRepository repository;

    public GrupoService(GrupoRepository repository) {
        this.repository = repository;
    }

    public List<Grupo> obtenerTodos() {
        return repository.findAll();
    }

    public List<Grupo> obtenerActivos() {
        return repository.findByActivoTrue();
    }

    public Grupo obtenerPorId(String id) {
        return repository.findById(id).orElse(null);
    }

    /*
- Crea un nuevo grupo verificando que la clave sea única y que el docente
- no tenga horarios empalmados en el mismo periodo y día.
     */
    public Grupo crear(GrupoRequest request) {
        // Verifica en MongoDB si ya existe otro grupo con la misma clave
        if (repository.existsByClave(request.clave())) {
            throw new IllegalArgumentException("Ya existe un grupo con la clave: " + request.clave());
        }

        // Convierte la lista de HorarioRequest (DTO) a objetos Horario del modelo
        List<Horario> horarios = mapearHorarios(request.horarios());

        // Valida coherencia de horas y ausencia de choques
        validarHorarios(null, request.docenteId(), request.periodo(), horarios);

        // Construye la entidad Grupo lista para persistirse en MongoDB (activo queda en true por @Builder.Default)
        Grupo grupo = Grupo.builder()
                .clave(request.clave()) // Asigna la clave del grupo
                .materiaId(request.materiaId()) // Asigna el id de la materia
                .docenteId(request.docenteId()) // Asigna el id del docente responsable
                .periodo(request.periodo()) // Asigna el periodo escolar (ej. "2026-3")
                .cupoMaximo(request.cupoMaximo()) // Asigna el límite de alumnos
                .horarios(horarios) // Asigna los horarios embebidos
                .build();

        return repository.save(grupo);
    }

    /*
- Actualiza un grupo existente validando duplicidad de clave (si cambió)
- y evitando cruces de horario para el docente asignado.
     */
    public Grupo actualizar(String id, GrupoRequest request) {
        // Busca el grupo actual en la base de datos por su id
        Grupo existente = repository.findById(id).orElse(null);

        // Si no existe, retorna null para que el controlador responda 404 Not Found
        if (existente == null) {
            return null;
        }

        // Si la clave cambió respecto a la actual, verifica que no esté ocupada por otro grupo
        if (!request.clave().equals(existente.getClave())
                && repository.existsByClave(request.clave())) {
            throw new IllegalArgumentException("Ya existe un grupo con la clave: " + request.clave());
        }

        // Transforma los nuevos horarios enviados en el request a objetos del modelo
        List<Horario> nuevosHorarios = mapearHorarios(request.horarios());

        // Valida horarios pasando el id actual para excluir este mismo grupo de la comparación
        validarHorarios(id, request.docenteId(), request.periodo(), nuevosHorarios);

        // Actualiza cada atributo de la entidad existente con los valores del record GrupoRequest
        existente.setClave(request.clave());
        existente.setMateriaId(request.materiaId());
        existente.setDocenteId(request.docenteId());
        existente.setPeriodo(request.periodo());
        existente.setCupoMaximo(request.cupoMaximo());
        existente.setHorarios(nuevosHorarios);

        // Persiste los cambios en MongoDB y devuelve el grupo actualizado
        return repository.save(existente);
    }

    // Elimina de MongoDB el grupo correspondiente al id indicado
    public void eliminar(String id) {
        repository.deleteById(id);
    }

    /*
- Convierte una lista de HorarioRequest (entrada HTTP) en una lista de
- Horario para asignar al grupo.
     */
    private List<Horario> mapearHorarios(List<GrupoRequest.HorarioRequest> requests) {
        return requests.stream() // Recorre cada elemento de la lista de horarios del DTO
                .map(h -> Horario.builder() // Crea un objeto Horario por cada elemento
                        .dia(h.dia()) // Copia el enum DiaSemana (LUNES..SABADO)
                        .horaInicio(h.horaInicio()) // Copia la hora de inicio en formato HH:mm
                        .horaFin(h.horaFin()) // Copia la hora de fin en formato HH:mm
                        .aula(h.aula()) // Copia el aula asignada
                        .build())
                .toList();
    }

    /*
- Valida que cada bloque tenga horaInicio < horaFin, que no choquen bloques
- dentro del mismo grupo y que el docente no tenga otro grupo activo empalmado.
     */
    private void validarHorarios(
            String grupoIdActual,
            String docenteId,
            String periodo,
            List<Horario> horarios
    ) {
        // Recorre cada bloque de horario por índice para poder compararlo con los siguientes
        for (int i = 0; i < horarios.size(); i++) {
            Horario actual = horarios.get(i); // Obtiene el bloque actual
            LocalTime inicioA = LocalTime.parse(actual.getHoraInicio()); // Convierte horaInicio a LocalTime
            LocalTime finA = LocalTime.parse(actual.getHoraFin()); // Convierte horaFin a LocalTime

            // Comprueba que la hora de inicio sea estrictamente anterior a la hora de fin
            if (!inicioA.isBefore(finA)) {
                throw new IllegalArgumentException("La hora de inicio debe ser menor que la hora de fin");
            }

            // Compara el bloque actual contra los demás bloques dentro de este mismo grupo
            for (int j = i + 1; j < horarios.size(); j++) {
                Horario otro = horarios.get(j); // Obtiene otro bloque del mismo grupo
                // Solo compara si ambos bloques caen en el mismo día de la semana
                if (actual.getDia() == otro.getDia()) {
                    LocalTime inicioB = LocalTime.parse(otro.getHoraInicio()); // Hora inicio del otro bloque
                    LocalTime finB = LocalTime.parse(otro.getHoraFin()); // Hora fin del otro bloque
                    // Dos bloques de tiempo se chocan o enciman cuando el horario A empieza antes de que termine el horario B,
                    // y al mismo tiempo el horario A termina después de que empieza el horario B
                    if (inicioA.isBefore(finB) && finA.isAfter(inicioB)) {
                        throw new IllegalStateException(
                                "El grupo tiene horarios empalmados el día " + actual.getDia()
                        );
                    }
                }
            }

            // Consulta en MongoDB los grupos activos del mismo docente, periodo y día
            List<Grupo> gruposExistentes = repository
                    .findByDocenteIdAndPeriodoAndHorariosDiaAndActivoTrue(
                            docenteId,
                            periodo,
                            actual.getDia()
                    );

            // Recorre cada grupo encontrado para verificar posibles cruces
            for (Grupo grupoExistente : gruposExistentes) {
                // Si se está actualizando un grupo, omite compararlo consigo mismo
                if (grupoIdActual != null && grupoIdActual.equals(grupoExistente.getId())) {
                    continue;
                }
                // Si este grupo guardado no tiene horarios registrados, salta al siguiente
                if (grupoExistente.getHorarios() == null) {
                    continue;
                }

                // Revisa cada bloque horario del grupo ya registrado en MongoDB
                for (Horario existente : grupoExistente.getHorarios()) {
                    // Ignora los horarios de otros días, ya que un grupo puede darse en distintos días
                    if (existente.getDia() != actual.getDia()) {
                        continue;
                    }
                    LocalTime inicioExistente = LocalTime.parse(existente.getHoraInicio()); // Hora inicio guardada
                    LocalTime finExistente = LocalTime.parse(existente.getHoraFin()); // Hora fin guardada

                    // Detecta si el nuevo bloque se superpone con el bloque ya asignado al docente
                    if (inicioA.isBefore(finExistente) && finA.isAfter(inicioExistente)) {
                        throw new IllegalStateException(
                                "El docente ya tiene clase en el grupo " + grupoExistente.getClave()
                                        + " el " + existente.getDia()
                                        + " (" + existente.getHoraInicio() + "-" + existente.getHoraFin() + ")"
                        );
                    }
                }
            }
        }
    }
}