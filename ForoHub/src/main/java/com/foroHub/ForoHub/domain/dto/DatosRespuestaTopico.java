// src/main/java/com/foroHub/ForoHub/domain/dto/DatosRespuestaTopico.java
package com.foroHub.ForoHub.domain.dto;

import com.foroHub.ForoHub.domain.models.StatusTopico;
import java.time.LocalDateTime;

public record DatosRespuestaTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        StatusTopico status,
        DatosAutor autor,
        DatosCurso curso
) {
    public record DatosAutor(Long id, String nombre) {}
    public record DatosCurso(Long id, String nombre) {}
}
