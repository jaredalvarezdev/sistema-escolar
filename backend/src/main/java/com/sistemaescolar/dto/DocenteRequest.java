package com.sistemaescolar.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DocenteRequest {
    @NotBlank(message = "El número de empleado es obligatorio")
    @Size(max = 30, message = "El número de empleado no puede exceder 30 caracteres")
    private String numeroEmpleado;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "El nombre no puede exceder 80 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 80, message = "El apellido paterno no puede exceder 80 caracteres")
    private String apellidoPaterno;

    @Size(max = 80, message = "El apellido materno no puede exceder 80 caracteres")
    private String apellidoMaterno;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es válido")
    @Size(max = 120, message = "El correo no puede exceder 120 caracteres")
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = "El teléfono debe contener exactamente 10 dígitos")
    private String telefono;

    @NotBlank(message = "La especialidad es obligatoria")
    @Size(max = 100, message = "La especialidad no puede exceder 100 caracteres")
    private String especialidad;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean activo = true;
}
