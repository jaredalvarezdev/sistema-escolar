package com.sistemaescolar.repository;

import com.sistemaescolar.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Acceso a la colección usuarios.
 * MongoRepository ya incluye guardar, buscar, listar y eliminar.
 */
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    // Busca el usuario para el inicio de sesión (sin importar mayúsculas)
    Optional<Usuario> findByEmailIgnoreCase(String email);

    // Valida que el correo no esté registrado (sin importar mayúsculas)
    boolean existsByEmailIgnoreCase(String email);

    // Revisa si ese alumno ya tiene cuenta
    boolean existsByAlumnoId(String alumnoId);
}