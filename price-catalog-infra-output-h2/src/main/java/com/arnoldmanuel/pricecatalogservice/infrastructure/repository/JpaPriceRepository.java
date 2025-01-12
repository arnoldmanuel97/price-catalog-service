package com.arnoldmanuel.pricecatalogservice.infrastructure.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arnoldmanuel.pricecatalogservice.infrastructure.entity.Price;
import com.arnoldmanuel.pricecatalogservice.infrastructure.projection.ApplicablePriceProjection;

@Repository
public interface JpaPriceRepository extends JpaRepository<Price, Long> {

    @Query(value = "SELECT p.product_id AS productId, p.brand_id AS brandId, "
            + "p.start_date AS startDate, p.end_date AS endDate, p.price AS price, "
            + "p.price_list AS priceList, p.priority AS priority "
            + "FROM prices p "
            + "WHERE p.product_id = :productId "
            + "AND p.brand_id = :brandId "
            + "AND :applicationDate BETWEEN p.start_date AND p.end_date",
            nativeQuery = true)
    List<ApplicablePriceProjection> findApplicablePrices(@Param("productId") Long productId,
            @Param("brandId") Long brandId,
            @Param("applicationDate") LocalDateTime applicationDate);
}
