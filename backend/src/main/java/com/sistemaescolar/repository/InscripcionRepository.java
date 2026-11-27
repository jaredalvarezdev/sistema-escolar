package com.sistemaescolar.repository;

import com.sistemaescolar.model.Inscripcion;
import com.sistemaescolar.model.EstadoInscripcion;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

/**
    Acceso a la coleccion inscripciones
 */
public interface InscripcionRepository extends MongoRepository<Inscripcion, String> {
    // Valida que el alumno no tenga una inscripcion en el grupo
    boolean existsByAlumnoIdAndGrupoId(
            String alumnoId,
            String grupoId
    );

    // Busca una inscripcion especifica
    Optional<Inscripcion> findByAlumnoIdAndGrupoId(
            String alumnoId,
            String grupoId
    );

    // Lista las inscripciones de un alumno
    List<Inscripcion> findByAlumnoId(String alumnoId);

    // Lista las inscripciones de un grupo
    List<Inscripcion> findByGrupoId(String grupoId);

    // Lista las inscripciones de un grupo segun su estado
    List<Inscripcion> findByGrupoIdAndEstado(
            String grupoId,
            EstadoInscripcion estado
    );

    // Cuenta las inscripciones activas de un grupo
    long countByGrupoIdAndEstado(
            String grupoId,
            EstadoInscripcion estado
    );

    // Comprueba si un alumno está inscrito en un grupo
    boolean existsByAlumnoIdAndGrupoIdAndEstado(
            String alumnoId,
            String grupoId,
            EstadoInscripcion estado
    );
}
