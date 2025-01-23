package com.foroHub.ForoHub.repository;

import com.foroHub.ForoHub.domain.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * @author rhizor
 * @version 1.0
 * @since 2025-01-22
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}
