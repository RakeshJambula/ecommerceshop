package com.Rakhi1999.Ecommerce_Shop.dto;

import com.Rakhi1999.Ecommerce_Shop.entity.Payment;
import com.Rakhi1999.Ecommerce_Shop.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequest {

    private BigDecimal totalPrice;
    private List<OrderItemRequest> items;
    private Payment paymentInfo;

    // NEW: choose COD or RAZORPAY
    private PaymentMethod paymentMethod;

    // Client will send this when verifying payment
    private String razorpayOrderId;
}
