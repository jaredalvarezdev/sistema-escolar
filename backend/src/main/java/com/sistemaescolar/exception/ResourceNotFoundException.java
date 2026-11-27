package com.sistemaescolar.exception;

/**
 * El registro solicitado no existe (404).
 * Ej.: buscar una materia o alumno por un id que no está registrado.
 */
public class ResourceNotFoundException extends RuntimeException {

    // Recibe el mensaje que se mostrará en la respuesta de error
    public ResourceNotFoundException(String message) {
        super(message);
    }
}