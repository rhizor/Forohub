package com.foroHub.ForoHub.repository;

import com.foroHub.ForoHub.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Método para encontrar un usuario por su correo electrónico
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);

    // Método para verificar la existencia de un usuario por correo electrónico
    boolean existsByCorreoElectronico(String correoElectronico);

    // Si necesitas otros métodos específicos, puedes añadirlos aquí
}
