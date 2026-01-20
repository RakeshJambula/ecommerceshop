package com.Rakhi1999.Ecommerce_Shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductWishlistDTO {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;

    private Long categoryId;
    private String categoryName;
}
