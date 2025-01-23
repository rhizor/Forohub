package com.foroHub.ForoHub.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para el registro de un nuevo usuario")
public record DatosRegistroUsuario(
        @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @Schema(description = "Correo electrónico del usuario", example = "juan.perez@email.com")
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato del email no es válido")
        String email,

        @Schema(description = "Contraseña del usuario", example = "contraseña123")
        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}
