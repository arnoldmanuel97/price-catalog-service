package com.arnoldmanuel.pricecatalogservice.infrastructure.mapper;

import com.arnoldmanuel.pricecatalogservice.domain.ApplicablePriceDomain;
import com.arnoldmanuel.pricecatalogservice.domain.Brand;
import com.arnoldmanuel.pricecatalogservice.domain.Product;
import com.arnoldmanuel.pricecatalogservice.infrastructure.exceptions.InvalidFormatException;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.arnoldmanuel.pricecatalogservice.domain.PriceCalculation;
import com.arnoldmanuel.pricecatalogservice.infrastructure.api.model.PriceDTO;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Mapper(componentModel = "spring")
public interface PriceDtoMapper {

    @Mapping(target = "startDate", source = "priceRate.dateRange.startDate")
    @Mapping(target = "endDate", source = "priceRate.dateRange.endDate")
    @Mapping(target = "applicablePrice", source = "priceRate.price")
    @Mapping(target = "brandId", source = "brand.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "priceListId", source = "priceRate.priceList")
    PriceDTO fromDomain(ApplicablePriceDomain applicablePrice);

    @Named("stringToLocalDateTime")
    default LocalDateTime stringToLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            return LocalDateTime.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new InvalidFormatException("Invalid date format. Expected format: yyyy-MM-dd HH:mm:ss");
        }
    }

    @Mapping(target = "date", source = "date", qualifiedByName = "stringToLocalDateTime")
    @Mapping(target = "brand.id", source = "brandId")
    @Mapping(target = "product.id", source = "productId")
    PriceCalculation toDomain(Long brandId, Long productId, String date);

}
