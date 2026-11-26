package com.sistemaescolar.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Formato único de respuesta de error de la API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL) // No muestra campos vacíos en el JSON
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors // Errores por campo en las validaciones
) {
}