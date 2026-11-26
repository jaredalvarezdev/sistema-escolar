package com.sistemaescolar.repository;

import com.sistemaescolar.model.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Acceso a la colección usuarios.
 * MongoRepository ya incluye guardar, buscar, listar y eliminar.
 */
public interface UsuarioRepository extends MongoRepository<Usuario, String> {

    // Busca el usuario para el inicio de sesión
    Optional<Usuario> findByEmail(String email);

    // Valida que el correo no esté registrado
    boolean existsByEmail(String email);
}