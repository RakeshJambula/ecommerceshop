package com.Rakhi1999.Ecommerce_Shop.service.interf;

import com.Rakhi1999.Ecommerce_Shop.entity.Orders;
import org.json.JSONObject;

public interface PaymentService {

    JSONObject createRazorpayOrder(Orders order);

    void handleCODPayment(Orders order);

    void updatePaymentStatus(String paymentId, String status);
}
