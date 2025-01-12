package com.arnoldmanuel.pricecatalogservice.output;

import com.arnoldmanuel.pricecatalogservice.domain.ApplicablePriceDomain;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepositoryPort {

    List<ApplicablePriceDomain> findApplicablePrices(Long productId, Long brandId, LocalDateTime applicationDate);
}
