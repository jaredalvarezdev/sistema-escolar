package com.sistemaescolar.repository;

import com.sistemaescolar.model.Materia;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Acceso a la colección materias.
 */
public interface MateriaRepository extends MongoRepository<Materia, String> {

    // Valida que la clave no esté registrada
    boolean existsByClave(String clave);

    // Lista solo las materias activas
    List<Materia> findByActivoTrue();
}