package com.Rakhi1999.Ecommerce_Shop.dto;

import com.Rakhi1999.Ecommerce_Shop.entity.Payment;
import com.Rakhi1999.Ecommerce_Shop.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequest {

    @NotBlank(message = "Delivery address is required")
    private String address;

    private BigDecimal totalPrice;
    private List<OrderItemRequest> items;
    private Payment paymentInfo;

    // NEW: choose COD or RAZORPAY
    private PaymentMethod paymentMethod;

    // Client will send this when verifying payment
    private String razorpayOrderId;
}
