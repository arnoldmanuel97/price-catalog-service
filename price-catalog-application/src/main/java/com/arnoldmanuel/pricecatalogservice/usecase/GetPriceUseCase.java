package com.arnoldmanuel.pricecatalogservice.usecase;

import com.arnoldmanuel.pricecatalogservice.domain.ApplicablePriceDomain;
import com.arnoldmanuel.pricecatalogservice.domain.PriceCalculation;

import java.util.Optional;

public interface GetPriceUseCase {

    Optional<ApplicablePriceDomain> getPrice(PriceCalculation priceCalculation);

}
