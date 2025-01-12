package com.arnoldmanuel.pricecatalogservice.infrastructure.persistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;

import com.arnoldmanuel.pricecatalogservice.domain.ApplicablePriceDomain;
import com.arnoldmanuel.pricecatalogservice.infrastructure.repository.JpaPriceRepository;

@SpringBootTest
class PriceRepositoryAdapterIT {

    @Autowired
    private PriceRepositoryAdapter priceRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @SpyBean
    private JpaPriceRepository jpaPriceRepository;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void whenFindApplicablePricesCalledWithExistingDataThenPricesAreReturned() {
        long productId = 35455L;
        long brandId = 1L;
        LocalDateTime appliedDate = LocalDateTime.parse("2020-06-14 10:00:00", formatter);

        List<ApplicablePriceDomain> result = priceRepository.findApplicablePrices(productId, brandId, appliedDate);
        assertFalse(result.isEmpty());
        assertEquals(brandId, result.getFirst().brand().id());
        assertEquals(productId, result.getFirst().product().id());
    }

    @Test
    void whenFindApplicablePricesCalled_thenResultShouldBeCached() {
        cacheManager.getCache("prices").clear();
        long productId = 35455L;
        long brandId = 1L;
        LocalDateTime appliedDate = LocalDateTime.parse("2020-06-14 10:00:00", formatter);

        List<ApplicablePriceDomain> firstCall = priceRepository.findApplicablePrices(productId, brandId, appliedDate);
        assertFalse(firstCall.isEmpty());

        List<ApplicablePriceDomain> secondCall = priceRepository.findApplicablePrices(productId, brandId, appliedDate);
        assertEquals(firstCall, secondCall);

        verify(jpaPriceRepository, times(1)).findApplicablePrices(productId, brandId, appliedDate);
    }

    @Test
    void whenFindApplicablePricesCalledWithNonExistingDataThenNoPricesAreReturned() {
        long productId = 35455L;
        long brandId = 1L;
        LocalDateTime appliedDate = LocalDateTime.parse("2021-01-01 10:00:00", formatter);

        List<ApplicablePriceDomain> result = priceRepository.findApplicablePrices(productId, brandId, appliedDate);
        assertTrue(result.isEmpty());
    }
}
