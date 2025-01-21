package com.foroHub.ForoHub.repository;

import com.foroHub.ForoHub.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    // Aquí podrías añadir métodos específicos si los necesitas
}