package com.Rakhi1999.Ecommerce_Shop.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    // PERCENTAGE or FLAT
    private String discountType;

    private BigDecimal discountValue;

    private BigDecimal minOrderAmount;

    private LocalDateTime expiryDate;

    private Integer usageLimit;

    private Integer usedCount = 0;

    private boolean active = true;
}