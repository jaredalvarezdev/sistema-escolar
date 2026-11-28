package com.sistemaescolar.service;

import com.sistemaescolar.dto.DocenteRequest;
import com.sistemaescolar.model.Docente;
import com.sistemaescolar.repository.DocenteRepository;
import java.util.List;
import org.springframework.stereotype.Service;
/**/
/**
 * Servicio con la lógica de negocio para gestionar las operaciones CRUD de docentes.
 */
@Service
public class DocenteService {

    private final DocenteRepository repository;

    public DocenteService(DocenteRepository repository) {
        this.repository = repository;
    }

    // Obtiene todos los docentes registrados en la colección
    public List<Docente> obtenerTodos() {
        return repository.findAll();
    }

    // Obtiene únicamente los docentes activos
    public List<Docente> obtenerActivos() {
        return repository.findByActivoTrue();
    }

    // Busca un docente por su id; devuelve null si no existe
    public Docente obtenerPorId(String id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Crea un nuevo docente validando que número de empleado y correo no estén duplicados.
     */
    public Docente crear(DocenteRequest request) {
        // Verifica duplicidad de número de empleado
        if (repository.existsByNumeroEmpleado(request.numeroEmpleado())) {
            throw new IllegalArgumentException("Ya existe un docente con ese número de empleado");
        }
        // Verifica duplicidad de correo electrónico
        if (repository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Ya existe un docente con ese correo");
        }

        // Construye la entidad Docente (activo queda en true por @Builder.Default)
        Docente docente = Docente.builder()
                .numeroEmpleado(request.numeroEmpleado())
                .nombre(request.nombre())
                .apellidoPaterno(request.apellidoPaterno())
                .apellidoMaterno(request.apellidoMaterno())
                .email(request.email())
                .telefono(request.telefono())
                .especialidad(request.especialidad())
                .build();

        return repository.save(docente);
    }

    /**
     * Actualiza un docente existente validando que no choque con el número de empleado
     * o correo de otro registro.
     */
    public Docente actualizar(String id, DocenteRequest request) {
        // Busca el docente actual; si no existe devuelve null para que el controlador responda 404
        Docente existente = repository.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }

        // Valida número de empleado solo si cambió respecto al guardado
        if (!request.numeroEmpleado().equals(existente.getNumeroEmpleado())
                && repository.existsByNumeroEmpleado(request.numeroEmpleado())) {
            throw new IllegalArgumentException("Ya existe un docente con ese número de empleado");
        }
        // Valida correo solo si cambió respecto al guardado
        if (!request.email().equals(existente.getEmail())
                && repository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Ya existe un docente con ese correo");
        }

        // Actualiza los campos con los valores del record DocenteRequest
        existente.setNumeroEmpleado(request.numeroEmpleado());
        existente.setNombre(request.nombre());
        existente.setApellidoPaterno(request.apellidoPaterno());
        existente.setApellidoMaterno(request.apellidoMaterno());
        existente.setEmail(request.email());
        existente.setTelefono(request.telefono());
        existente.setEspecialidad(request.especialidad());

        return repository.save(existente);
    }

    // Elimina un docente por su id
    public void eliminar(String id) {
        repository.deleteById(id);
    }
}
