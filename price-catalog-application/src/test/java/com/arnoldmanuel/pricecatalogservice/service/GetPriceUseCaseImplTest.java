package com.arnoldmanuel.pricecatalogservice.service;

import com.arnoldmanuel.pricecatalogservice.domain.ApplicablePriceDomain;
import com.arnoldmanuel.pricecatalogservice.domain.Brand;
import com.arnoldmanuel.pricecatalogservice.domain.DateRange;
import com.arnoldmanuel.pricecatalogservice.domain.PriceCalculation;
import com.arnoldmanuel.pricecatalogservice.domain.PriceRate;
import com.arnoldmanuel.pricecatalogservice.domain.Product;
import com.arnoldmanuel.pricecatalogservice.output.PriceRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPriceUseCaseImplTest {

    @Mock
    private PriceRepositoryPort priceRepositoryPort;

    @InjectMocks
    private GetPriceUseCaseImpl getPriceUseCase;

    @Test
    void getPrice_whenPriceIsAvailable_ThenReturnPriceFromRepository() {
        PriceCalculation priceCalculation = new PriceCalculation(LocalDateTime.now(), new Brand(1L), new Product(1L));
        ApplicablePriceDomain repositoryPrice = new ApplicablePriceDomain(
                new Product(1L),
                new Brand(1L),
                new PriceRate(1, new DateRange(LocalDateTime.now(), LocalDateTime.now()), new BigDecimal(1)),
                1
        );

        when(priceRepositoryPort.findApplicablePrices(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of(repositoryPrice));

        Optional<ApplicablePriceDomain> result = getPriceUseCase.getPrice(priceCalculation);

        assertTrue(result.isPresent());
        assertEquals(repositoryPrice, result.get());
        verify(priceRepositoryPort, times(1)).findApplicablePrices(anyLong(), anyLong(), any(LocalDateTime.class));
    }

    @Test
    void getPrice_whenNoPriceFound_ThenReturnEmpty() {
        PriceCalculation priceCalculation = new PriceCalculation(LocalDateTime.now(), new Brand(1L), new Product(1L));

        when(priceRepositoryPort.findApplicablePrices(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(List.of());

        Optional<ApplicablePriceDomain> result = getPriceUseCase.getPrice(priceCalculation);

        assertTrue(result.isEmpty());
        verify(priceRepositoryPort, times(1)).findApplicablePrices(anyLong(), anyLong(), any(LocalDateTime.class));
    }
}
