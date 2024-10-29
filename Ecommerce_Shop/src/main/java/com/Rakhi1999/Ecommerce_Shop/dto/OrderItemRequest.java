package com.Rakhi1999.Ecommerce_Shop.dto;

import lombok.Data;

@Data
public class OrderItemRequest {

    private Long productId;
    private int quantity;
}
