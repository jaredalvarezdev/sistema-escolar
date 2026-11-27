package com.sistemaescolar.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

/**
    Relaciona a un alumno con un grupo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "inscripciones")
public class Inscripcion {
    @Id
    private String id;
    private String alumnoId;
    private String grupoId;
    private LocalDate fechaInscripcion;

    @Builder.Default
    private EstadoInscripcion estado = EstadoInscripcion.INSCRITO;
}
