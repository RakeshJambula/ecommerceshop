package com.Rakhi1999.Ecommerce_Shop.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    private Long productId;
    private String content;
    private int rating;
}
