package com.foroHub.ForoHub.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos de respuesta del usuario")
public record UsuarioDTO(
        @Schema(description = "ID del usuario", example = "1")
        Long id,

        @Schema(description = "Nombre del usuario", example = "Juan Pérez")
        String nombre,

        @Schema(description = "Email del usuario", example = "juan.perez@email.com")
        String email
) {}
