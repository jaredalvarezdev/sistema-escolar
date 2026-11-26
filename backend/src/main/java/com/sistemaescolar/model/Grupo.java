package com.sistemaescolar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "grupos")

/*
 * Grupo académico asignado a una materia y docente en un periodo.
 */
public class Grupo {
    @Id
    private String id;

    @Indexed(unique = true)
    private String clave;

    private String materiaId;
    private String docenteId;
    private String periodo;
    private Integer cupoMaximo;

    private List<Horario> horarios;

    @Builder.Default
    private Boolean activo = true;
}
