package com.sistemaescolar.service;

import com.sistemaescolar.dto.MateriaRequest;
import com.sistemaescolar.exception.BusinessRuleException;
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
        String clave = normalizarClave(request.clave());
        validarClave(clave);
        Materia materia = new Materia();
        copiarDatos(materia, request, clave);
        materia.setActivo(true);
        return materiaRepository.save(materia);
    }

    // 400 si está dada de baja; 409 si cambia a una clave que ya existe
    public Materia actualizar(String id, MateriaRequest request) {
        Materia materia = obtener(id);
        if (!estaActiva(materia)) {
            throw new BusinessRuleException("No se puede editar una materia dada de baja");
        }
        String clave = normalizarClave(request.clave());
        if (!materia.getClave().equals(clave)) {
            validarClave(clave);
        }
        copiarDatos(materia, request, clave);
        return materiaRepository.save(materia);
    }

    // Baja lógica; 400 si ya estaba dada de baja
    public void darDeBaja(String id) {
        Materia materia = obtener(id);
        if (!estaActiva(materia)) {
            throw new BusinessRuleException("La materia ya está dada de baja");
        }
        materia.setActivo(false);
        materiaRepository.save(materia);
    }

    // Reactiva una materia; 400 si ya estaba activa
    public Materia activar(String id) {
        Materia materia = obtener(id);
        if (estaActiva(materia)) {
            throw new BusinessRuleException("La materia ya está activa");
        }
        materia.setActivo(true);
        return materiaRepository.save(materia);
    }

    // true solo si activo es true; si es false o null, la toma como inactiva
    private boolean estaActiva(Materia materia) {
        return Boolean.TRUE.equals(materia.getActivo());
    }

    // Quita espacios y pasa a mayúsculas: " mat101 " → "MAT101"
    private String normalizarClave(String clave) {
        return clave.trim().toUpperCase();
    }

    private void validarClave(String clave) {
        if (materiaRepository.existsByClave(clave)) {
            throw new DuplicateResourceException("Ya existe una materia con la clave " + clave);
        }
    }

    private void copiarDatos(Materia materia, MateriaRequest request, String clave) {
        materia.setClave(clave);
        materia.setNombre(request.nombre().trim());
        materia.setDescripcion(request.descripcion());
        materia.setCreditos(request.creditos());
        materia.setSemestre(request.semestre());
        materia.setHorasSemana(request.horasSemana());
    }
}