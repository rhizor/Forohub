package com.foroHub.ForoHub.controller;

import com.foroHub.ForoHub.model.Usuario;
import com.foroHub.ForoHub.repository.UsuarioRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
@Api(value = "Usuarios", tags = "Operaciones relacionadas con los usuarios del foro")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    @ApiOperation(value = "Crear un nuevo usuario", notes = "Permite registrar un nuevo usuario en el foro")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario creado exitosamente", response = Usuario.class),
            @ApiResponse(code = 400, message = "Datos de entrada inválidos"),
            @ApiResponse(code = 409, message = "Conflicto: El correo electrónico ya está registrado")
    })
    public ResponseEntity<Usuario> crearUsuario(
            @ApiParam(value = "Datos del nuevo usuario", required = true) @Valid @RequestBody Usuario usuario) {
        // Verificar si el correo electrónico ya está registrado
        if (usuarioRepository.findByCorreoElectronico(usuario.getCorreoElectronico()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Hashear la contraseña antes de guardarla
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    @GetMapping
    @ApiOperation(value = "Listar todos los usuarios", notes = "Devuelve una lista de todos los usuarios registrados en el foro")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operación exitosa", response = Usuario.class, responseContainer = "List"),
            @ApiResponse(code = 401, message = "No autorizado")
    })
    public ResponseEntity<List<Usuario>> listarUsuarios(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtener detalles de un usuario", notes = "Devuelve los detalles de un usuario específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario encontrado", response = Usuario.class),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 404, message = "Usuario no encontrado")
    })
    public ResponseEntity<Usuario> obtenerUsuario(
            @ApiParam(value = "ID del usuario a obtener", required = true, example = "1") @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        return usuarioOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Actualizar un usuario", notes = "Permite actualizar los datos de un usuario existente en el foro")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario actualizado exitosamente", response = Usuario.class),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 404, message = "Usuario no encontrado"),
            @ApiResponse(code = 400, message = "Datos de entrada inválidos"),
            @ApiResponse(code = 409, message = "Conflicto: El correo electrónico ya está registrado por otro usuario")
    })
    public ResponseEntity<Usuario> actualizarUsuario(
            @ApiParam(value = "ID del usuario a actualizar", required = true, example = "1") @PathVariable Integer id,
            @ApiParam(value = "Datos actualizados del usuario", required = true) @Valid @RequestBody Usuario usuarioDetails,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            // Verificar si el correo electrónico ha cambiado y si es único
            if (!usuario.getCorreoElectronico().equals(usuarioDetails.getCorreoElectronico())) {
                Optional<Usuario> existingUser = usuarioRepository.findByCorreoElectronico(usuarioDetails.getCorreoElectronico());
                if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
            }

            usuario.setNombre(usuarioDetails.getNombre());
            usuario.setCorreoElectronico(usuarioDetails.getCorreoElectronico());
            // Hashear la contraseña solo si se proporciona una nueva
            if (usuarioDetails.getContrasena() != null && !usuarioDetails.getContrasena().isEmpty()) {
                usuario.setContrasena(passwordEncoder.encode(usuarioDetails.getContrasena()));
            }
            return ResponseEntity.ok(usuarioRepository.save(usuario));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Eliminar un usuario", notes = "Elimina un usuario del foro por su ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Usuario eliminado exitosamente"),
            @ApiResponse(code = 401, message = "No autorizado"),
            @ApiResponse(code = 404, message = "Usuario no encontrado")
    })
    public ResponseEntity<?> eliminarUsuario(
            @ApiParam(value = "ID del usuario a eliminar", required = true, example = "1") @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}