package com.foroHub.ForoHub.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generarToken(String username, List<String> roles) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withSubject(username)
                    .withExpiresAt(Date.from(Instant.now().plus(expiration, ChronoUnit.MILLIS)))
                    .withArrayClaim("roles", roles.toArray(new String[0])) // Añadimos los roles al token
                    .sign(algorithm);
        } catch (Exception exception) {
            logger.error("Error al generar token JWT para el usuario: {}", username, exception);
            throw new RuntimeException("Error al generar token JWT", exception);
        }
    }

    public String getSubject(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException e) {
            logger.error("Error al validar token JWT: {}", e.getMessage());
            throw new JWTVerificationException("Token JWT inválido o expirado", e);
        }
    }

    public Collection<GrantedAuthority> getAuthorities(String token) {
        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);
            // Obtenemos los roles del claim 'roles' del token JWT
            List<String> roles = jwt.getClaim("roles").asList(String.class);

            // Convertimos los roles en GrantedAuthority
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
        } catch (JWTVerificationException e) {
            logger.error("Error al obtener autoridades del token JWT: {}", e.getMessage());
            throw new JWTVerificationException("Error al obtener autoridades del token JWT", e);
        }
    }
}