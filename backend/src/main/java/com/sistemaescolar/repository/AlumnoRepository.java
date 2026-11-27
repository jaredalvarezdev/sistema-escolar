package com.sistemaescolar.repository;

import com.sistemaescolar.model.Alumno;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Acceso a la colección alumnos.
 */
public interface AlumnoRepository extends MongoRepository<Alumno, String> {

    // Valida que la matrícula no esté registrada
    boolean existsByMatricula(String matricula);

    // Valida que el correo no esté registrado
    boolean existsByEmail(String email);

    // Lista solo los alumnos activos
    List<Alumno> findByActivoTrue();
}
