package com.sistemaescolar.dto;

import com.sistemaescolar.model.Rol;

/**
 * Respuesta del login y registro: el token y los datos que ocupa el frontend.
 */
public record AuthResponse(
        String token,
        String id,
        String nombre,
        String email,
        Rol rol,
        String alumnoId,  // Solo si es ALUMNO
        String docenteId  // Solo si es PROFESOR
) {
}