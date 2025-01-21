package com.foroHub.ForoHub.repository;

import com.foroHub.ForoHub.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {
    // Aquí podrías añadir métodos específicos si los necesitas
}
