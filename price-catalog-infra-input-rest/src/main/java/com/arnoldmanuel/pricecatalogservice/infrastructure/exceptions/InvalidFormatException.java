package com.arnoldmanuel.pricecatalogservice.infrastructure.exceptions;

public class InvalidFormatException extends RuntimeException {

    public InvalidFormatException(String message) {
        super(message);
    }
}
