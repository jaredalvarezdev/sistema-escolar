package com.sistemaescolar.exception;

/**
 * El registro ya existe (409).
 * Ej.: registrar una clave, matrícula o correo que ya está en uso.
 */
public class DuplicateResourceException extends RuntimeException {

    // Recibe el mensaje que se mostrará en la respuesta de error
    public DuplicateResourceException(String message) {
        super(message);
    }
}