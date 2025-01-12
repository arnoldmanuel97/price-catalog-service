package com.arnoldmanuel.pricecatalogservice.infrastructure.mapper;

import com.arnoldmanuel.pricecatalogservice.domain.ApplicablePriceDomain;
import com.arnoldmanuel.pricecatalogservice.infrastructure.projection.ApplicablePriceProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    @Mapping(target = "brand.id", source = "brandId")
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "priceRate.dateRange.startDate", source = "startDate")
    @Mapping(target = "priceRate.dateRange.endDate", source = "endDate")
    @Mapping(target = "priceRate.price", source = "price")
    @Mapping(target = "priceRate.priceList", source = "priceList")
    @Mapping(target = "priority", source = "priority")
    ApplicablePriceDomain toDomain(ApplicablePriceProjection applicablePriceProjection);
}
