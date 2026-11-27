package com.sistemaescolar.dto;

import com.sistemaescolar.model.DiaSemana;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * Datos que se reciben para crear o actualizar un grupo con sus horarios.
 */
public record GrupoRequest(
        @NotBlank(message = "La clave es obligatoria")
        @Size(max = 30, message = "La clave no puede exceder 30 caracteres")
        String clave,

        @NotBlank(message = "El id de la materia es obligatorio")
        String materiaId,

        @NotBlank(message = "El id del docente es obligatorio")
        String docenteId,

        @NotBlank(message = "El periodo es obligatorio")
        @Size(max = 20, message = "El periodo no puede exceder 20 caracteres")
        String periodo,

        @NotNull(message = "El cupo máximo es obligatorio")
        @Min(value = 1, message = "El cupo máximo debe ser al menos 1")
        @Max(value = 50, message = "El cupo máximo no puede exceder 50")
        Integer cupoMaximo,

        @Valid
        @NotEmpty(message = "Debe incluir al menos un horario")
        List<HorarioRequest> horarios
) {
    // Horas siempre en formato HH:mm (08:00, no 8:00)
    public record HorarioRequest(
            @NotNull(message = "El día es obligatorio")
            DiaSemana dia,

            @NotBlank(message = "La hora de inicio es obligatoria")
            @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora de inicio debe tener formato HH:mm")
            String horaInicio,

            @NotBlank(message = "La hora de fin es obligatoria")
            @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "La hora de fin debe tener formato HH:mm")
            String horaFin,

            @NotBlank(message = "El aula es obligatoria")
            @Size(max = 30, message = "El aula no puede exceder 30 caracteres")
            String aula
    ) {
    }
}