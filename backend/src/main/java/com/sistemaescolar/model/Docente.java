package com.sistemaescolar.model;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
/*
 * Docente registrado en el sistema escolar.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "docentes")
public class Docente {
    @Id
    private String id;

    @Indexed(unique = true)
    private String numeroEmpleado;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;

    @Indexed(unique = true)
    private String email;

    private String telefono;
    private String especialidad;

    @Builder.Default
    private Boolean activo = true;
}
