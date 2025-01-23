// src/main/java/com/foroHub/ForoHub/repository/CursoRepository.java
package com.foroHub.ForoHub.repository;

import com.foroHub.ForoHub.domain.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}