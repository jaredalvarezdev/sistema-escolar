package com.sistemaescolar.repository;

import com.sistemaescolar.model.DiaSemana;
import com.sistemaescolar.model.Grupo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
/**
 * Repositorio para consultar y persistir grupos y sus horarios en MongoDB.
 */
public interface GrupoRepository extends MongoRepository<Grupo, String> {

    // Verifica si ya existe un grupo con esa clave antes de guardar uno nuevo
    boolean existsByClave(String clave);

    // Obtiene solo los grupos que tienen el campo "activo" en true
    List<Grupo> findByActivoTrue();

    // Busca grupos activos de un docente en un periodo y día específicos
    List<Grupo> findByDocenteIdAndPeriodoAndHorariosDiaAndActivoTrue(
            String docenteId,
            String periodo,
            DiaSemana dia
    );
}
