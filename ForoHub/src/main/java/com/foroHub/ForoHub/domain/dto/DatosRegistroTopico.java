// src/main/java/com/foroHub/ForoHub/domain/dto/DatosRegistroTopico.java
package com.foroHub.ForoHub.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosRegistroTopico(
        @NotBlank
        String titulo,

        @NotBlank
        String mensaje,

        @NotNull
        Long autorId,

        @NotNull
        Long cursoId
) {}

