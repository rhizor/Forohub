// src/main/java/com/foroHub/ForoHub/controller/TopicoController.java
package com.foroHub.ForoHub.controller;

import com.foroHub.ForoHub.domain.dto.DatosRegistroTopico;
import com.foroHub.ForoHub.domain.dto.DatosRespuestaTopico;
import com.foroHub.ForoHub.service.TopicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/api/topicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Tópicos", description = "Endpoints para gestionar los tópicos del foro")
public class TopicoController {

    @Autowired
    private TopicoService service;

    @PostMapping
    @Operation(summary = "Registra un nuevo tópico")
    public ResponseEntity<DatosRespuestaTopico> registrar(
            @RequestBody @Valid DatosRegistroTopico datos,
            UriComponentsBuilder uriBuilder) {
        var respuesta = service.registrar(datos);
        var uri = uriBuilder.path("/api/topicos/{id}")
                .buildAndExpand(respuesta.id()).toUri();
        return ResponseEntity.created(uri).body(respuesta);
    }

    @GetMapping
    @Operation(summary = "Lista todos los tópicos activos")
    public ResponseEntity<Page<DatosRespuestaTopico>> listar(
            @PageableDefault(size = 10) Pageable paginacion) {
        return ResponseEntity.ok(service.listar(paginacion));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un tópico por su ID")
    public ResponseEntity<DatosRespuestaTopico> detallar(@PathVariable Long id) {
        return ResponseEntity.ok(service.detallar(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un tópico existente")
    public ResponseEntity<DatosRespuestaTopico> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid DatosRegistroTopico datos) {
        return ResponseEntity.ok(service.actualizar(id, datos));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un tópico")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
