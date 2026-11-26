package com.sistemaescolar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 - Documento almacenado en la coleccion calificaciones.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "calificaciones")
public class Calificacion {
    @Id
    private String id;
    private String alumnoId;
    private String grupoId;
    private Integer parcial; //1 a 3
    private Double calificacion; // 0 a 10
    private String comentario;
    private LocalDateTime fechaRegistro;
}