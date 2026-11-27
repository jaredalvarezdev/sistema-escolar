package com.sistemaescolar.repository;

import com.sistemaescolar.model.Asistencia;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
    Acceso a la coleccion asistencias
 */
public interface AsistenciaRepository extends MongoRepository<Asistencia, String> {
    // Valida que no exista una asistencia duplicada
    boolean existsByGrupoIdAndAlumnoIdAndFecha(
            String grupoId,
            String alumnoId,
            LocalDate fecha
    );

    // Busca un registro de asistencia especifico
    Optional<Asistencia> findByGrupoIdAndAlumnoIdAndFecha(
            String grupoId,
            String alumnoId,
            LocalDate fecha
    );

    // Lista las asistencias de un alumno
    List<Asistencia> findByAlumnoId(String alumnoId);

    // Lista las asistencias de un grupo
    List<Asistencia> findByGrupoId(String grupoId);

    // Consulta el pase de lista de un grupo en una fecha
    List<Asistencia> findByGrupoIdAndFecha(
            String grupoId,
            LocalDate fecha
    );

    // Consulta las asistencias de un alumno en un grupo
    List<Asistencia> findByAlumnoIdAndGrupoId(
            String alumnoId,
            String grupoId
    );

    // Consulta las asistencias de un grupo en un rango de fechas
    List<Asistencia> findByGrupoIdAndFechaBetween(
            String grupoId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );
}
