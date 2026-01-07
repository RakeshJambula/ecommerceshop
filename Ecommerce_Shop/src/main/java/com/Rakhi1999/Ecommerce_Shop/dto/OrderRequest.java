package com.Rakhi1999.Ecommerce_Shop.dto;

import com.Rakhi1999.Ecommerce_Shop.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequest {

    private List<OrderItemRequest> items;
    private PaymentMethod paymentMethod;

    // ADDRESS FIELDS (REQUIRED)
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
