package com.sistemaescolar.service;

import com.sistemaescolar.dto.MateriaRequest;
import com.sistemaescolar.exception.DuplicateResourceException;
import com.sistemaescolar.exception.ResourceNotFoundException;
import com.sistemaescolar.model.Materia;
import com.sistemaescolar.repository.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Lógica del CRUD de materias.
 */
@Service
@RequiredArgsConstructor
public class MateriaService {

    private final MateriaRepository materiaRepository;

    // Activas por defecto; todas si se pide
    public List<Materia> listar(boolean todas) {
        return todas ? materiaRepository.findAll() : materiaRepository.findByActivoTrue();
    }

    // 404 si no existe
    public Materia obtener(String id) {
        return materiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la materia con id " + id));
    }

    // 409 si la clave ya existe
    public Materia crear(MateriaRequest request) {
        validarClave(request.clave());
        Materia materia = new Materia();
        copiarDatos(materia, request);
        materia.setActivo(true);
        return materiaRepository.save(materia);
    }

    // 409 si cambia a una clave que ya existe
    public Materia actualizar(String id, MateriaRequest request) {
        Materia materia = obtener(id);
        if (!materia.getClave().equals(request.clave())) {
            validarClave(request.clave());
        }
        copiarDatos(materia, request);
        return materiaRepository.save(materia);
    }

    // Baja lógica
    public void darDeBaja(String id) {
        Materia materia = obtener(id);
        materia.setActivo(false);
        materiaRepository.save(materia);
    }

    private void validarClave(String clave) {
        if (materiaRepository.existsByClave(clave)) {
            throw new DuplicateResourceException("Ya existe una materia con la clave " + clave);
        }
    }

    private void copiarDatos(Materia materia, MateriaRequest request) {
        materia.setClave(request.clave());
        materia.setNombre(request.nombre());
        materia.setDescripcion(request.descripcion());
        materia.setCreditos(request.creditos());
        materia.setSemestre(request.semestre());
        materia.setHorasSemana(request.horasSemana());
    }
}