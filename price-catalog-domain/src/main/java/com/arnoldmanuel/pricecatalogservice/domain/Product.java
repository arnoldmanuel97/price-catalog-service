package com.arnoldmanuel.pricecatalogservice.domain;

import jakarta.validation.constraints.NotNull;

public record Product(
        @NotNull Long id
) {
}
