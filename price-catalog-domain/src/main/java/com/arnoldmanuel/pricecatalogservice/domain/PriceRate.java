package com.arnoldmanuel.pricecatalogservice.domain;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PriceRate(
        @NotNull Integer priceList,
        @NotNull DateRange dateRange,
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal price) {
}
