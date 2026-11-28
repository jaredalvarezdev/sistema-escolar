package com.sistemaescolar.controller;

import com.sistemaescolar.dto.GrupoRequest;
import com.sistemaescolar.model.Grupo;
import com.sistemaescolar.service.GrupoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**/
/**
 * Controlador REST que expone los endpoints CRUD para gestionar grupos académicos.
 */
@RestController // Indica que esta clase responde peticiones HTTP con JSON
@RequestMapping("/api/grupos") // Ruta base de todos los endpoints de grupos
public class GrupoController {

    // Servicio con la lógica de negocio y validación de horarios
    private final GrupoService service;

    // Inyección por constructor del servicio de grupos
    public GrupoController(GrupoService service) {
        this.service = service;
    }

    // Lista todos los grupos o solo los activos si se envía ?activos=true
    @GetMapping
    public List<Grupo> obtenerTodos(
            @RequestParam(required = false, defaultValue = "false") boolean activos) {
        if (activos) {
            return service.obtenerActivos();
        }
        return service.obtenerTodos();
    }

    // Busca un grupo por su id; devuelve 404 si no existe
    @GetMapping("/{id}")
    public ResponseEntity<Grupo> obtenerPorId(@PathVariable String id) {
        Grupo grupo = service.obtenerPorId(id);
        if (grupo == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(grupo);
    }

    // Valida y crea un nuevo grupo; responde 201 Created
    @PostMapping
    public ResponseEntity<Grupo> crear(@Valid @RequestBody GrupoRequest request) {
        Grupo creado = service.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Valida y actualiza un grupo por id; responde 200 OK o 404 Not Found
    @PutMapping("/{id}")
    public ResponseEntity<Grupo> actualizar(
            @PathVariable String id,
            @Valid @RequestBody GrupoRequest request) {
        Grupo actualizado = service.actualizar(id, request);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    // Elimina un grupo por su id y responde 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}