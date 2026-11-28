package com.sistemaescolar.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
/**
    Registro de asistencia de un alumno en un grupo por cada dia de clase
*/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "asistencias")
public class Asistencia {
    @Id
    private String id;
    private String grupoId;
    private String alumnoId;
    private LocalDate fecha;
    private EstadoAsistencia estado;
    private String observacion;
}
