package com.arnoldmanuel.pricecatalogservice.domain;

import com.arnoldmanuel.pricecatalogservice.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PriceDomainTest {

    @Test
    void dateRangeValidation_withInvalidDateRange_thenThrowException() {
        LocalDateTime now = LocalDateTime.now();
        assertThrows(DomainException.class, () -> new DateRange(now.plusDays(1), now));
    }
}
