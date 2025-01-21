package com.foroHub.ForoHub.security;

import com.foroHub.ForoHub.model.Respuesta;
import com.foroHub.ForoHub.model.Usuario;
import com.foroHub.ForoHub.repository.RespuestaRepository;
import com.foroHub.ForoHub.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("respuestaControllerSecurity")
public class RespuestaControllerSecurity {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean hasPermissionToEdit(Integer respuestaId, Authentication authentication) {
        Optional<Respuesta> respuestaOptional = respuestaRepository.findById(respuestaId);
        if (respuestaOptional.isPresent()) {
            Respuesta respuesta = respuestaOptional.get();
            Optional<Usuario> usuarioAutenticado = usuarioRepository.findByCorreoElectronico(authentication.getName());
            // El usuario puede editar si es el autor de la respuesta o tiene el rol de ADMIN
            return respuesta.getAutor().equals(usuarioAutenticado) || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return false; // Si no se encuentra la respuesta, no hay permiso
    }

    public boolean hasPermissionToDelete(Integer respuestaId, Authentication authentication) {
        // La lógica para eliminar es similar a la de editar
        return hasPermissionToEdit(respuestaId, authentication);
    }
}