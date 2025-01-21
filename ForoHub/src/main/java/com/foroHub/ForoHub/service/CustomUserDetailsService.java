package com.foroHub.ForoHub.service;

import com.foroHub.ForoHub.model.Usuario;
import com.foroHub.ForoHub.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Usuario usuario = usuarioRepository.findByCorreoElectronico(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el correo electrónico: " + username));

            logger.info("Usuario encontrado: {}", username);

            return User.builder()
                    .username(usuario.getCorreoElectronico())
                    .password(usuario.getContrasena())
                    .roles("USER") // Asumiendo que todos los usuarios tienen el rol USER por defecto
                    .build();
        } catch (UsernameNotFoundException e) {
            logger.error("Error al cargar el usuario con el correo electrónico: {}", username, e);
            throw e;
        }
    }
}