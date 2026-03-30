package com.Rakhi1999.Ecommerce_Shop.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CouponValidationResponse {
    private boolean valid;
    private BigDecimal discount;
    private BigDecimal finalAmount;
    private String message;
}
