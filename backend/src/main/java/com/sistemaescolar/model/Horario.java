package com.sistemaescolar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Bloque de horario embebido dentro de un grupo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Horario {

    private DiaSemana dia;
    private String horaInicio; // HH:mm
    private String horaFin;    // HH:mm
    private String aula;
}