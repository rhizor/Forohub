package com.foroHub.ForoHub.dto;

public record LoginRequest(String username, String password) {
    // Este record ya define los getters implícitamente
}
