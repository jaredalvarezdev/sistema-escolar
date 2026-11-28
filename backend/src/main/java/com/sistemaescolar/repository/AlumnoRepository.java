package com.sistemaescolar.repository;

import com.sistemaescolar.model.Alumno;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

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

    // Busca al alumno por su matrícula (para activar su cuenta)
    Optional<Alumno> findByMatricula(String matricula);
}