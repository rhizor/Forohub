package com.foroHub.ForoHub.repository;

import com.foroHub.ForoHub.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Integer> {
    Page<Topico> findByCurso_NombreContainingAndFechaCreacionBetween(String nombreCurso, LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
}

