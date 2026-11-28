package com.sistemaescolar.controller;

import com.sistemaescolar.dto.DocenteRequest;
import com.sistemaescolar.model.Docente;
import com.sistemaescolar.service.DocenteService;
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
 * Controlador REST que expone los endpoints CRUD para gestionar docentes.
 */
@RestController
@RequestMapping("/api/docentes")
public class DocenteController {

    private final DocenteService service;

    public DocenteController(DocenteService service) {
        this.service = service;
    }

    // Lista todos los docentes o solo los activos si se envía ?activos=true
    @GetMapping
    public List<Docente> obtenerTodos(
            @RequestParam(required = false, defaultValue = "false") boolean activos) {
        if (activos) {
            return service.obtenerActivos();
        }
        return service.obtenerTodos();
    }

    // Busca un docente por su id; devuelve 404 si no existe
    @GetMapping("/{id}")
    public ResponseEntity<Docente> obtenerPorId(@PathVariable String id) {
        Docente docente = service.obtenerPorId(id);
        if (docente == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(docente);
    }

    // Valida y crea un nuevo docente; responde 201 Created
    @PostMapping
    public ResponseEntity<Docente> crear(@Valid @RequestBody DocenteRequest request) {
        Docente creado = service.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // Valida y actualiza un docente por id; responde 200 OK o 404 Not Found
    @PutMapping("/{id}")
    public ResponseEntity<Docente> actualizar(
            @PathVariable String id,
            @Valid @RequestBody DocenteRequest request) {
        Docente actualizado = service.actualizar(id, request);
        if (actualizado == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(actualizado);
    }

    // Elimina un docente por su id y responde 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}