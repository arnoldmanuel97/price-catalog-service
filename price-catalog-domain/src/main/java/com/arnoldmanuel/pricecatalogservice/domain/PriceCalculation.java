package com.arnoldmanuel.pricecatalogservice.domain;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record PriceCalculation(
        @NotNull
        LocalDateTime date,
        @NotNull
        Brand brand,
        @NotNull
        Product product) {

    @Override
    public String toString() {
        return "prices::" + product.id() + "::" + brand.id() + "::" + date;
    }
}
