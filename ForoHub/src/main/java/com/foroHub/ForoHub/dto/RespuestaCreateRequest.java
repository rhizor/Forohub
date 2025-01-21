package com.foroHub.ForoHub.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para crear una nueva respuesta")
public record RespuestaCreateRequest(Integer autorId, Integer topicoId, String mensaje) {}
