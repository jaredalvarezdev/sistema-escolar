package com.sistemaescolar.dto;

import com.sistemaescolar.model.DiaSemana;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class GrupoRequest {
    @NotBlank(message = "La clave es obligatoria")
    @Size(max = 30, message = "La clave no puede exceder 30 caracteres")
    private String clave;

    @NotBlank(message = "El id de la materia es obligatorio")
    private String materiaId;

    @NotBlank(message = "El id del docente es obligatorio")
    private String docenteId;

    @NotBlank(message = "El periodo es obligatorio")
    @Size(max = 20, message = "El periodo no puede exceder 20 caracteres")
    private String periodo;

    @NotNull(message = "El cupo máximo es obligatorio")
    @Min(value = 1, message = "El cupo máximo debe ser al menos 1")
    @Max(value = 50, message = "El cupo máximo no puede exceder 50")
    private Integer cupoMaximo;

    @Valid
    @NotEmpty(message = "Debe incluir al menos un horario")
    private List<HorarioRequest> horarios;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo = true;

    @Data
    public static class HorarioRequest {

        @NotBlank(message = "El día es obligatorio")
        private DiaSemana dia;

        @NotBlank(message = "La hora de inicio es obligatoria")
        @Pattern(regexp = "^([01]?\\d|2[0-3]):[0-5]\\d$", message = "La hora de inicio debe tener formato HH:mm")
        private String horaInicio;

        @NotBlank(message = "La hora de fin es obligatoria")
        @Pattern(regexp = "^([01]?\\d|2[0-3]):[0-5]\\d$", message = "La hora de fin debe tener formato HH:mm")
        private String horaFin;

        @NotBlank(message = "El aula es obligatoria")
        @Size(max = 30, message = "El aula no puede exceder 30 caracteres")
        private String aula;
    }
}
