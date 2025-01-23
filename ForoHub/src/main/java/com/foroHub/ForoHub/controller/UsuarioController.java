package com.foroHub.ForoHub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.foroHub.ForoHub.domain.dto.DatosRegistroUsuario;
import com.foroHub.ForoHub.domain.dto.UsuarioDTO;
import com.foroHub.ForoHub.service.UsuarioService;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuario", description = "API para la gestión de usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Registrar un nuevo usuario",
            description = "Crea un nuevo usuario en el sistema con los datos proporcionados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos"),
            @ApiResponse(responseCode = "409", description = "El email ya está registrado")
    })
    @PostMapping
    public ResponseEntity<UsuarioDTO> registrar(@RequestBody @Valid DatosRegistroUsuario datos) {
        var usuario = usuarioService.registrar(datos);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Buscar usuario por ID",
            description = "Obtiene los detalles de un usuario específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        var usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(usuario);
    }
}
