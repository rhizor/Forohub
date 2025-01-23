package com.foroHub.ForoHub.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.foroHub.ForoHub.domain.models.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Servicio para la generación y validación de tokens JWT.
 * @author rhizor
 * @version 1.0
 * @since 2025-01-22 00:47:33
 */
@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String apiSecret;

    @Operation(summary = "Generar token JWT",
            description = "Genera un token JWT para el usuario autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token generado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error al generar el token")
    })
    public String generarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("ForoHub")
                    .withSubject(usuario.getEmail())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Error al generar token JWT", exception);
        }
    }

    @Operation(summary = "Obtener subject del token",
            description = "Obtiene el email del usuario del token JWT")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Subject obtenido exitosamente"),
            @ApiResponse(responseCode = "400", description = "Token inválido")
    })
    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.require(algorithm)
                    .withIssuer("ForoHub")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido o expirado!");
        }
    }

    /**
     * Genera la fecha de expiración del token (2 horas desde su creación).
     * @return Instant con la fecha de expiración
     */
    private Instant generarFechaExpiracion() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }
}