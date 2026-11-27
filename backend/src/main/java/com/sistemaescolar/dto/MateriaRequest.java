package com.sistemaescolar.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Datos que se reciben para crear o actualizar una materia.
 */
public record MateriaRequest(

        @NotBlank(message = "La clave es obligatoria")
        @Size(max = 20, message = "Máximo 20 caracteres")
        String clave,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "Máximo 100 caracteres")
        String nombre,

        @Size(max = 255, message = "Máximo 255 caracteres")
        String descripcion, // Opcional

        @NotNull(message = "Los créditos son obligatorios")
        @Min(value = 1, message = "Mínimo 1 crédito")
        @Max(value = 10, message = "Máximo 10 créditos")
        Integer creditos,

        @NotNull(message = "El semestre es obligatorio")
        @Min(value = 1, message = "Mínimo semestre 1")
        @Max(value = 12, message = "Máximo semestre 12")
        Integer semestre,

        @NotNull(message = "Las horas por semana son obligatorias")
        @Min(value = 1, message = "Mínimo 1 hora")
        @Max(value = 20, message = "Máximo 20 horas")
        Integer horasSemana
) {
}