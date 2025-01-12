package com.arnoldmanuel.pricecatalogservice.infrastructure.exceptions;

import com.arnoldmanuel.pricecatalogservice.exceptions.DomainException;

public class PriceNotFoundException extends DomainException {
    public PriceNotFoundException(String message) {
        super(message);
    }
}
