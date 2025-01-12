package com.arnoldmanuel.pricecatalogservice.service;

import com.arnoldmanuel.pricecatalogservice.domain.ApplicablePriceDomain;
import com.arnoldmanuel.pricecatalogservice.domain.PriceCalculation;
import com.arnoldmanuel.pricecatalogservice.output.PriceRepositoryPort;
import com.arnoldmanuel.pricecatalogservice.usecase.GetPriceUseCase;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetPriceUseCaseImpl implements GetPriceUseCase {

    private final PriceRepositoryPort priceRepositoryPort;

    public GetPriceUseCaseImpl(PriceRepositoryPort priceRepositoryPort) {
        this.priceRepositoryPort = priceRepositoryPort;
    }

    @Override
    public Optional<ApplicablePriceDomain> getPrice(PriceCalculation priceCalculation) {
        return priceRepositoryPort.findApplicablePrices(priceCalculation.product().id(),
                    priceCalculation.brand().id(), priceCalculation.date())
                    .stream().max(ApplicablePriceDomain::compareByPriority);
    }
}
