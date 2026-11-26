package com.sistemaescolar.repository;

import com.sistemaescolar.model.Docente;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
/*
 * Repositorio para consultar y persistir docentes en MongoDB.
 */
public interface DocenteRepository extends MongoRepository<Docente, String> {
    // Devuelve true si ya existe un docente con ese numeroEmpleado (más ligero que traer el documento)
    boolean existsByNumeroEmpleado(String numeroEmpleado);

    // Devuelve true si ya existe un docente registrado con ese correo
    boolean existsByEmail(String email);

    // Lista únicamente los docentes cuyo campo "activo" sea true
    List<Docente> findByActivoTrue();
}
