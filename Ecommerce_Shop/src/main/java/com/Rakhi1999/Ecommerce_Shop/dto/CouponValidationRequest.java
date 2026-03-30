package com.Rakhi1999.Ecommerce_Shop.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CouponValidationRequest {
    private String couponCode;
    private BigDecimal amount;
}
