package com.sistemaescolar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

/*
 * Bloque de horario embebido dentro de un grupo.
 */
public class Horario {
    private String dia;
    private String horaInicio;
    private String horaFin;
    private String aula;
}
