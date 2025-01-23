// src/main/java/com/foroHub/ForoHub/domain/dto/DatosAutenticacionUsuario.java
package com.foroHub.ForoHub.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DatosAutenticacionUsuario(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password
) {}



