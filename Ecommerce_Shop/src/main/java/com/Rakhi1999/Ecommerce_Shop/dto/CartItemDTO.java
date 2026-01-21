package com.Rakhi1999.Ecommerce_Shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CartItemDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String imageUrl;
    private BigDecimal price;
    private int quantity;
}

