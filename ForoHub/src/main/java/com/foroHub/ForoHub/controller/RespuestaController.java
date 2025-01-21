package com.foroHub.ForoHub.controller;

import com.foroHub.ForoHub.dto.RespuestaCreateRequest; // Importación corregida
import com.foroHub.ForoHub.dto.RespuestaUpdateRequest; // Importación corregida
import com.foroHub.ForoHub.exception.ResourceNotFoundException;
import com.foroHub.ForoHub.model.Respuesta;
import com.foroHub.ForoHub.service.RespuestaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/respuestas")
@Tag(name = "Respuestas", description = "Operaciones relacionadas con las respuestas a los tópicos del foro")
public class RespuestaController {

    private final RespuestaService respuestaService;

    public RespuestaController(RespuestaService respuestaService) {
        this.respuestaService = respuestaService;
    }

    @PostMapping
    @ApiOperation(value = "Crear una nueva respuesta", notes = "Permite crear una nueva respuesta a un tópico existente en el foro")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Respuesta creada exitosamente", response = Respuesta.class),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 400, message = "Datos de entrada inválidos"),
            @ApiResponse(code = 404, message = "Usuario o Tópico no encontrado")
    })
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Respuesta> crearRespuesta(
            @ApiParam(value = "Datos para crear la respuesta", required = true) @Valid @RequestBody RespuestaCreateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Respuesta respuesta = respuestaService.crearRespuesta(request);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping
    @ApiOperation(value = "Listar todas las respuestas", notes = "Devuelve una lista de todas las respuestas del foro")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operación exitosa", response = Respuesta.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "No autorizado")
    })
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Respuesta>> listarRespuestas(@AuthenticationPrincipal UserDetails userDetails) {
        List<Respuesta> respuestas = respuestaService.listarRespuestas();
        return ResponseEntity.ok(respuestas);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtener detalles de una respuesta", notes = "Devuelve los detalles de una respuesta específica por su ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Respuesta encontrada", response = Respuesta.class),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 404, message = "Respuesta no encontrada")
    })
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Respuesta> obtenerRespuesta(
            @ApiParam(value = "ID de la respuesta a obtener", required = true, example = "1") @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        Respuesta respuesta = respuestaService.obtenerRespuesta(id);
        return ResponseEntity.ok(respuesta);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Actualizar una respuesta", notes = "Permite actualizar los datos de una respuesta existente en el foro")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Respuesta actualizada exitosamente", response = Respuesta.class),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 403, message = "Acceso denegado"),
            @ApiResponse(code = 404, message = "Respuesta no encontrada"),
            @ApiResponse(code = 400, message = "Datos de entrada inválidos")
    })
    @PreAuthorize("isAuthenticated() and (@respuestaControllerSecurity.hasPermissionToEdit(#id, authentication))")
    public ResponseEntity<Respuesta> actualizarRespuesta(
            @ApiParam(value = "ID de la respuesta a actualizar", required = true, example = "1") @PathVariable Integer id,
            @ApiParam(value = "Datos actualizados de la respuesta", required = true) @Valid @RequestBody RespuestaUpdateRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        Respuesta respuesta = respuestaService.actualizarRespuesta(id, request);
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Eliminar una respuesta", notes = "Elimina una respuesta del foro por su ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Respuesta eliminada exitosamente"),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 403, message = "Acceso denegado"),
            @ApiResponse(code = 404, message = "Respuesta no encontrada")
    })
    @PreAuthorize("isAuthenticated() and (@respuestaControllerSecurity.hasPermissionToDelete(#id, authentication))")
    public ResponseEntity<?> eliminarRespuesta(
            @ApiParam(value = "ID de la respuesta a eliminar", required = true, example = "1") @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        respuestaService.eliminarRespuesta(id);
        return ResponseEntity.ok().build();
    }
}