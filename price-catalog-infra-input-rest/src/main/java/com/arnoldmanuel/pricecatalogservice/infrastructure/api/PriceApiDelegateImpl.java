package com.arnoldmanuel.pricecatalogservice.infrastructure.api;

import com.arnoldmanuel.pricecatalogservice.infrastructure.api.api.PriceApiDelegate;
import com.arnoldmanuel.pricecatalogservice.infrastructure.api.model.PriceDTO;
import com.arnoldmanuel.pricecatalogservice.infrastructure.exceptions.PriceNotFoundException;
import com.arnoldmanuel.pricecatalogservice.infrastructure.mapper.PriceDtoMapper;
import com.arnoldmanuel.pricecatalogservice.usecase.GetPriceUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class PriceApiDelegateImpl implements PriceApiDelegate {

    private final GetPriceUseCase getPriceUseCase;
    private final PriceDtoMapper priceDtoMapper;

    public PriceApiDelegateImpl(GetPriceUseCase getPriceUseCase, PriceDtoMapper priceDtoMapper) {
        this.getPriceUseCase = getPriceUseCase;
        this.priceDtoMapper = priceDtoMapper;
    }

    @Override
    public ResponseEntity<PriceDTO> getApplicablePrice(Long brandId, Long productId, String date) {
        return ResponseEntity.ok(
                priceDtoMapper.fromDomain(
                        getPriceUseCase.getPrice(priceDtoMapper.toDomain(brandId, productId, date))
                        .orElseThrow(() -> new PriceNotFoundException("No se encontró precio aplicable"))
                )
        );
    }
}
