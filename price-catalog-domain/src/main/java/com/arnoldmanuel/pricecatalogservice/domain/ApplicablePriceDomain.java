package com.arnoldmanuel.pricecatalogservice.domain;

import jakarta.validation.constraints.NotNull;

public record ApplicablePriceDomain(
        @NotNull Product product,
        @NotNull Brand brand,
        @NotNull PriceRate priceRate,
        @NotNull Integer priority
) {
    public int compareByPriority(ApplicablePriceDomain other) {
        return Integer.compare(this.priority, other.priority);
    }
}
