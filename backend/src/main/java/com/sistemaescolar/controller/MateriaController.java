package com.sistemaescolar.controller;

import com.sistemaescolar.dto.MateriaRequest;
import com.sistemaescolar.model.Materia;
import com.sistemaescolar.service.MateriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoints del CRUD de materias.
 */
@RestController
@RequestMapping("/api/materias")
@RequiredArgsConstructor
public class MateriaController {

    private final MateriaService materiaService;

    // GET /api/materias  (con ?todas=true incluye las inactivas)
    @GetMapping
    public List<Materia> listar(@RequestParam(defaultValue = "false") boolean todas) {
        return materiaService.listar(todas);
    }

    // GET /api/materias/{id}
    @GetMapping("/{id}")
    public Materia obtener(@PathVariable String id) {
        return materiaService.obtener(id);
    }

    // POST /api/materias → 201 Created
    @PostMapping
    public ResponseEntity<Materia> crear(@Valid @RequestBody MateriaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(materiaService.crear(request));
    }

    // PUT /api/materias/{id}
    @PutMapping("/{id}")
    public Materia actualizar(@PathVariable String id, @Valid @RequestBody MateriaRequest request) {
        return materiaService.actualizar(id, request);
    }

    // DELETE /api/materias/{id} → baja lógica, 204 No Content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> darDeBaja(@PathVariable String id) {
        materiaService.darDeBaja(id);
        return ResponseEntity.noContent().build();
    }
}