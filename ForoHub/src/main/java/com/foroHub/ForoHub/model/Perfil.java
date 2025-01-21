package com.foroHub.ForoHub.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "perfil")
@Data
@NoArgsConstructor
public class Perfil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @ManyToMany(mappedBy = "perfiles", fetch = FetchType.LAZY)

    private Set<Usuario> usuarios;

    // Constructor, getters y setters se generan automáticamente con Lombok
}