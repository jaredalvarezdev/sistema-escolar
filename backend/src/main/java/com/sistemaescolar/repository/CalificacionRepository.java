package com.sistemaescolar.repository;

import com.sistemaescolar.model.Calificacion;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Acceso a la coleccion calificaciones.
 */
public interface CalificacionRepository extends MongoRepository<Calificacion, String> {

    // Valida antes de guardar, rechaza duplicado
    boolean existsByAlumnoIdAndGrupoIdAndParcial(
            String alumnoId,
            String grupoId,
            Integer parcial
    );

    //Devuelve parciales de un alumno en un grupo, orden asc
    List<Calificacion> findByAlumnoIdAndGrupoIdOrderByParcialAsc(
            String alumnoId,
            String grupoId
    );

    List<Calificacion> findByAlumnoId(String alumnoId);

    List<Calificacion> findByGrupoId(String grupoId);
}