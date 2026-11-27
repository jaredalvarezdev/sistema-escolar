package com.sistemaescolar.exception;

/**
 * Se incumple una regla de negocio (400).
 * Ej.: inscribir en un grupo sin cupo o registrar un horario que choca.
 */
public class BusinessRuleException extends RuntimeException {

    // Recibe el mensaje que se mostrará en la respuesta de error
    public BusinessRuleException(String message) {
        super(message);
    }
}