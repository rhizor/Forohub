package com.foroHub.ForoHub.controller;

import com.foroHub.ForoHub.model.Topico;
import com.foroHub.ForoHub.repository.TopicoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/topicos")
@Api(value = "Tópicos", tags = "Operaciones relacionadas con los tópicos del foro")
public class TopicoController {

    private final TopicoRepository topicoRepository;

    public TopicoController(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }

    @GetMapping
    @ApiOperation(value = "Listar todos los tópicos", notes = "Devuelve una lista paginada de todos los tópicos del foro")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operación exitosa", response = Page.class),
            @ApiResponse(code = 401, message = "No autorizado")
    })
    public ResponseEntity<Page<Topico>> listarTopicos(
            @ApiParam(value = "Nombre del curso para filtrar los tópicos", required = false) @RequestParam(required = false) String nombreCurso,
            @ApiParam(value = "Fecha de inicio para filtrar los tópicos", required = false, example = "2023-01-01T00:00:00Z") @RequestParam(required = false) LocalDateTime fechaInicio,
            @ApiParam(value = "Fecha de fin para filtrar los tópicos", required = false, example = "2023-12-31T23:59:59Z") @RequestParam(required = false) LocalDateTime fechaFin,
            @ApiParam(value = "Número de página (0..N)", defaultValue = "0") @RequestParam(defaultValue = "0") int page,
            @ApiParam(value = "Tamaño de la página", defaultValue = "10") @RequestParam(defaultValue = "10") int size,
            @ApiParam(value = "Campo y dirección para ordenar los tópicos", defaultValue = "fechaCreacion,asc", allowableValues = "fechaCreacion,asc;fechaCreacion,desc;titulo,asc;titulo,desc") @RequestParam(defaultValue = "fechaCreacion,asc") String[] sort,
            @AuthenticationPrincipal UserDetails userDetails) throws InterruptedException {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sort[1]), sort[0]));

        Page<Topico> topicos;
        if (nombreCurso != null && fechaInicio != null && fechaFin != null) {
            topicos = topicoRepository.findByCurso_NombreContainingAndFechaCreacionBetween(nombreCurso, fechaInicio, fechaFin, pageable);
        } else {
            topicos = topicoRepository.findAll(pageable);
        }

        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtener detalles de un tópico", notes = "Devuelve los detalles de un tópico específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tópico encontrado", response = Topico.class),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 404, message = "Tópico no encontrado")
    })
    public ResponseEntity<Topico> obtenerDetalleTopico(
            @ApiParam(value = "ID del tópico a obtener", required = true, example = "1") @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Topico> topicoOptional = topicoRepository.findById(id);
        return topicoOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ApiOperation(value = "Crear un nuevo tópico", notes = "Permite crear un nuevo tópico en el foro")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tópico creado exitosamente", response = Topico.class),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 400, message = "Datos de entrada inválidos")
    })
    public ResponseEntity<Topico> crearTopico(
            @ApiParam(value = "Datos del nuevo tópico", required = true) @Valid @RequestBody Topico topico,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Topico nuevoTopico = topicoRepository.save(topico);
        return ResponseEntity.ok(nuevoTopico);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Actualizar un tópico existente", notes = "Permite actualizar los detalles de un tópico existente en el foro")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tópico actualizado exitosamente", response = Topico.class),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 404, message = "Tópico no encontrado"),
            @ApiResponse(code = 400, message = "Datos de entrada inválidos")
    })
    public ResponseEntity<Topico> actualizarTopico(
            @ApiParam(value = "ID del tópico a actualizar", required = true, example = "1") @PathVariable Integer id,
            @ApiParam(value = "Datos actualizados del tópico", required = true) @Valid @RequestBody Topico topicoDetails,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Topico> topicoOptional = topicoRepository.findById(id);
        if (topicoOptional.isPresent()) {
            Topico topico = topicoOptional.get();
            topico.setTitulo(topicoDetails.getTitulo());
            topico.setMensaje(topicoDetails.getMensaje());
            topico.setFechaCreacion(topicoDetails.getFechaCreacion());
            topico.setStatus(topicoDetails.getStatus());
            return ResponseEntity.ok(topicoRepository.save(topico));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Eliminar un tópico", notes = "Elimina un tópico del foro por su ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Tópico eliminado exitosamente"),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 404, message = "Tópico no encontrado")
    })
    public ResponseEntity<?> eliminarTopico(
            @ApiParam(value = "ID del tópico a eliminar", required = true, example = "1") @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (topicoRepository.existsById(id)) {
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}