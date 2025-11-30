package com.Rakhi1999.Ecommerce_Shop.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Response {

    private int status;
    private String message;

    // For User
    private UserDto user;
    private List<UserDto> userList;

    // For Product
    private ProductDto product;
    private List<ProductDto> productList;

    // For Category
    private CategoryDto category;
    private List<CategoryDto> categoryList;

    // Generic data field
    private Object data;

    // For pagination
    private Integer totalPage;
    private Long totalElement;

    // For token / login
    private String token;
    private String expirationTime;
    private String role;
}
