package com.Rakhi1999.Ecommerce_Shop.dto;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long orderId;
    private String paymentMethod;  // "RAZORPAY" or "COD"
}
