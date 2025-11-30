package com.Rakhi1999.Ecommerce_Shop.service.impl;

import com.Rakhi1999.Ecommerce_Shop.entity.Orders;
import com.Rakhi1999.Ecommerce_Shop.enums.OrderStatus;
import com.Rakhi1999.Ecommerce_Shop.repository.OrderRepo;
import com.Rakhi1999.Ecommerce_Shop.repository.PaymentRepo;
import com.Rakhi1999.Ecommerce_Shop.service.interf.PaymentService;
import com.Rakhi1999.Ecommerce_Shop.service.interf.RazorpayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepo orderRepo;
    private final PaymentRepo paymentRepo;
    private final RazorpayService razorpayService;

    @Override
    public JSONObject createRazorpayOrder(Orders order) {
        long amountInPaise = order.getTotalPrice().multiply(java.math.BigDecimal.valueOf(100)).longValue();
        String receipt = "order_" + order.getId();

        // Call REST-based Razorpay service
        JSONObject rpOrder = razorpayService.createRazorpayOrder(order.getId(), amountInPaise, "INR", receipt);

        // Save order payment details
        order.setPaymentId(rpOrder.getString("id"));
        order.setStatus(OrderStatus.PENDING);
        orderRepo.save(order);

        return rpOrder;
    }

    @Override
    public void handleCODPayment(Orders order) {
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepo.save(order);
        log.info("COD order confirmed for order id {}", order.getId());
    }

    @Override
    public void updatePaymentStatus(String paymentId, String status) {
        Orders order = orderRepo.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Order not found for paymentId: " + paymentId));

        if ("success".equalsIgnoreCase(status)) {
            order.setStatus(OrderStatus.CONFIRMED);
        } else {
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderRepo.save(order);
    }
    /**
     * Helper method to verify Razorpay payment signature
     */
    public boolean paymentServiceVerifySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
        return razorpayService.verifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);
    }
}
