package com.arnoldmanuel.pricecatalogservice.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "prices", indexes = {
        @Index(name = "idx_product_brand_dates", columnList = "productId, brandId, startDate, endDate"),
        @Index(name = "idx_priority", columnList = "priority")
})
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price_list")
    private Integer priceList;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "curr")
    private String currency;

}

