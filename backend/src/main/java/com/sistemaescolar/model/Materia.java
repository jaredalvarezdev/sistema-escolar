package com.sistemaescolar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Materia del catálogo escolar.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "materias")
public class Materia {

    @Id
    private String id;

    @Indexed(unique = true) // No se repiten claves
    private String clave;

    private String nombre;
    private String descripcion;
    private Integer creditos;    // 1 a 10
    private Integer semestre;    // 1 a 12
    private Integer horasSemana; // 1 a 20

    @Builder.Default
    private Boolean activo = true; // Baja lógica
}