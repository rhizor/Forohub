package com.foroHub.ForoHub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.foroHub.ForoHub.domain.dto.DatosRegistroUsuario;
import com.foroHub.ForoHub.domain.dto.UsuarioDTO;
import com.foroHub.ForoHub.domain.models.Usuario;
import com.foroHub.ForoHub.repository.UsuarioRepository;
import jakarta.validation.Valid;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioDTO registrar(@Valid DatosRegistroUsuario datos) {
        if (usuarioRepository.existsByEmail(datos.email())) {
            throw new RuntimeException("El email ya está registrado");
        }

        var usuario = new Usuario();
        usuario.setNombre(datos.nombre());
        usuario.setEmail(datos.email());
        usuario.setPassword(passwordEncoder.encode(datos.password()));
        usuario.setActivo(true);

        var usuarioGuardado = usuarioRepository.save(usuario);
        return new UsuarioDTO(
                usuarioGuardado.getId(),
                usuarioGuardado.getNombre(),
                usuarioGuardado.getEmail()
        );
    }

    public UsuarioDTO buscarPorId(Long id) {
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail()
        );
    }

    public UsuarioDTO buscarPorEmail(String email) {
        var usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail()
        );
    }
}
