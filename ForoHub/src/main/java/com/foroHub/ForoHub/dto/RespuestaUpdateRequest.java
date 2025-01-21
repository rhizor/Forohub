package com.foroHub.ForoHub.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para actualizar una respuesta")
public record RespuestaUpdateRequest(String mensaje, boolean solucion) {}
