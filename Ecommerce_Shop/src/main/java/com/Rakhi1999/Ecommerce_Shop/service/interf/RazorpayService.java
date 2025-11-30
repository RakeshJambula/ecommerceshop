package com.Rakhi1999.Ecommerce_Shop.service.interf;

import org.json.JSONObject;

public interface RazorpayService {

    /**
     * Create Razorpay order
     * @param orderId Internal order ID
     * @param amountInPaise Amount in paise
     * @param currency Currency (INR)
     * @param receipt Receipt ID
     * @return Razorpay order JSON
     */
    JSONObject createRazorpayOrder(Long orderId, long amountInPaise, String currency, String receipt);

    /**
     * Verify Razorpay payment signature
     */
    boolean verifySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature);
}
