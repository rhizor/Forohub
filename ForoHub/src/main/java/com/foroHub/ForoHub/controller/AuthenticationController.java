package com.foroHub.ForoHub.controller;

import com.foroHub.ForoHub.dto.LoginRequest;
import com.foroHub.ForoHub.dto.LoginResponse;
import com.foroHub.ForoHub.dto.RegisterRequest;
import com.foroHub.ForoHub.dto.RegisterResponse;
import com.foroHub.ForoHub.model.Usuario;
import com.foroHub.ForoHub.repository.UsuarioRepository;
import com.foroHub.ForoHub.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {
        try {
            logger.info("Attempting to authenticate user: {}", loginRequest.username());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.username());
            if (userDetails == null) {
                throw new UsernameNotFoundException("User not found: " + loginRequest.username());
            }

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .collect(Collectors.toList());

            String jwtToken = tokenService.generarToken(loginRequest.username(), roles);
            logger.info("Authentication successful for user: {}", loginRequest.username());

            return ResponseEntity.ok(new LoginResponse(jwtToken));
        } catch (BadCredentialsException e) {
            logger.error("Bad credentials for user: {}", loginRequest.username(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        } catch (UsernameNotFoundException e) {
            logger.error("User not found: {}", loginRequest.username(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            String errorMessage = "Error during authentication: " + e.getMessage();
            logger.error(errorMessage, e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", errorMessage);
            errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
        if (usuarioRepository.existsByCorreoElectronico(registerRequest.correoElectronico())) {
            return ResponseEntity.badRequest().body(new RegisterResponse("El correo electrónico ya está en uso"));
        }

        Usuario newUser = new Usuario();
        newUser.setNombre(registerRequest.nombre());
        newUser.setCorreoElectronico(registerRequest.correoElectronico());
        newUser.setContrasena(passwordEncoder.encode(registerRequest.contrasena()));

        Usuario savedUser = usuarioRepository.save(newUser);
        logger.info("New user registered: {}", savedUser.getCorreoElectronico());

        // Ajuste: Solo pasamos un argumento al constructor de RegisterResponse
        return ResponseEntity.ok(new RegisterResponse("Usuario registrado exitosamente"));
    }
}