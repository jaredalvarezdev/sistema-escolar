package com.sistemaescolar.service;

import com.sistemaescolar.dto.AuthResponse;
import com.sistemaescolar.dto.LoginRequest;
import com.sistemaescolar.dto.RegisterRequest;
import com.sistemaescolar.exception.BusinessRuleException;
import com.sistemaescolar.exception.DuplicateResourceException;
import com.sistemaescolar.exception.ResourceNotFoundException;
import com.sistemaescolar.model.Alumno;
import com.sistemaescolar.model.Rol;
import com.sistemaescolar.model.Usuario;
import com.sistemaescolar.repository.AlumnoRepository;
import com.sistemaescolar.repository.UsuarioRepository;
import com.sistemaescolar.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Registro (activación de cuenta de alumno) e inicio de sesión.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    // Crea la cuenta de un alumno que ya existe en la colección alumnos
    public AuthResponse registrar(RegisterRequest request) {
        String matricula = request.matricula().trim().toUpperCase();
        String email = request.email().trim();

        Alumno alumno = alumnoRepository.findByMatricula(matricula)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un alumno con la matrícula " + matricula));

        if (!Boolean.TRUE.equals(alumno.getActivo())) {
            throw new BusinessRuleException("El alumno está dado de baja");
        }
        if (!email.equalsIgnoreCase(alumno.getEmail())) {
            throw new BusinessRuleException("El email no coincide con el registrado para esa matrícula");
        }
        if (usuarioRepository.existsByAlumnoId(alumno.getId())) {
            throw new DuplicateResourceException("Este alumno ya tiene una cuenta");
        }
        if (usuarioRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateResourceException("Ya existe una cuenta con ese email");
        }

        Usuario usuario = Usuario.builder()
                .nombre(alumno.getNombre() + " " + alumno.getApellidoPaterno())
                .email(alumno.getEmail()) // Se guarda igual que en alumnos
                .password(passwordEncoder.encode(request.password())) // Se guarda cifrada
                .rol(Rol.ALUMNO)
                .alumnoId(alumno.getId())
                .build();

        return crearRespuesta(usuarioRepository.save(usuario));
    }

    // 401 si el email no existe o la contraseña no coincide (mismo mensaje para no dar pistas)
    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim();

        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email)
                .filter(u -> passwordEncoder.matches(request.password(), u.getPassword()))
                .orElseThrow(() -> new BadCredentialsException("Email o contraseña incorrectos"));

        // 403 si la cuenta fue desactivada
        if (!usuario.isActivo()) {
            throw new DisabledException("La cuenta está desactivada");
        }

        return crearRespuesta(usuario);
    }

    private AuthResponse crearRespuesta(Usuario usuario) {
        return new AuthResponse(
                jwtService.generarToken(usuario),
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.getAlumnoId(),
                usuario.getDocenteId()
        );
    }
}