package com.foroHub.ForoHub.dto;

public record RegisterRequest(String nombre, String correoElectronico, String contrasena) {
    // Este record ya define los getters implícitamente
}
