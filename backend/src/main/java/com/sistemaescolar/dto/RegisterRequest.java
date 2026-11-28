package com.sistemaescolar.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos para que un alumno ya dado de alta active su cuenta.
 */
public record RegisterRequest(
        @NotBlank(message = "La matrícula es obligatoria")
        @Size(max = 20, message = "Máximo 20 caracteres")
        String matricula,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no es válido")
        @Size(max = 120, message = "Máximo 120 caracteres")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 50, message = "La contraseña debe tener entre 8 y 50 caracteres")
        String password
) {
}