package com.foroHub.ForoHub.repository;

import com.foroHub.ForoHub.model.Respuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Integer> {
    // Puedes añadir consultas personalizadas si es necesario
}
