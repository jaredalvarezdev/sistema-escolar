package com.sistemaescolar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Alumno inscrito en el sistema escolar.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "alumnos")
public class Alumno {

    @Id
    private String id;

    @Indexed(unique = true) // No se repiten matrículas
    private String matricula;

    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;

    @Indexed(unique = true) // No se repiten correos
    private String email;

    private String telefono;
    private LocalDate fechaNacimiento;
    private String carrera;
    private Integer semestre; // 1 a 12

    @Builder.Default
    private Boolean activo = true; // Baja lógica
}
