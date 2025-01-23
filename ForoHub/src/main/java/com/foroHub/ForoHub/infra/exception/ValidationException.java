// src/main/java/com/foroHub/ForoHub/infra/exception/ValidationException.java
package com.foroHub.ForoHub.infra.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
