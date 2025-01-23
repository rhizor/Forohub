// src/main/java/com/foroHub/ForoHub/repository/TopicoRepository.java
package com.foroHub.ForoHub.repository;

import com.foroHub.ForoHub.domain.models.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    Page<Topico> findAllByActivoTrue(Pageable paginacion);
    Optional<Topico> findByIdAndActivoTrue(Long id);
}