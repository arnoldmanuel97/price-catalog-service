package com.arnoldmanuel.pricecatalogservice.domain;

import jakarta.validation.constraints.NotNull;

public record Brand(
        @NotNull Long id
) {
}
