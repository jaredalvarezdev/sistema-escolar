package com.sistemaescolar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Usuario con acceso al sistema.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;

    // Datos de la cuenta
    private String nombre;

    @Indexed(unique = true) // No se repiten correos
    private String email;

    private String password; // Cifrada con BCrypt

    private Rol rol;

    // Relación según el rol
    private String alumnoId;  // Solo si es ALUMNO
    private String docenteId; // Solo si es PROFESOR

    // Control
    @Builder.Default
    private boolean activo = true;

    @Builder.Default
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}