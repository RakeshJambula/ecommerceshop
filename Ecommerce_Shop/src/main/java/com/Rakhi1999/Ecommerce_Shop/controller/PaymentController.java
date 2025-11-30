package com.Rakhi1999.Ecommerce_Shop.controller;

import com.Rakhi1999.Ecommerce_Shop.dto.OrderDto;
import com.Rakhi1999.Ecommerce_Shop.dto.Response;
import com.Rakhi1999.Ecommerce_Shop.entity.Orders;
import com.Rakhi1999.Ecommerce_Shop.entity.Payment;
import com.Rakhi1999.Ecommerce_Shop.enums.OrderStatus;
import com.Rakhi1999.Ecommerce_Shop.exceptions.NotFoundException;
import com.Rakhi1999.Ecommerce_Shop.mapper.EntityDtoMapper;
import com.Rakhi1999.Ecommerce_Shop.repository.OrderRepo;
import com.Rakhi1999.Ecommerce_Shop.repository.PaymentRepo;
import com.Rakhi1999.Ecommerce_Shop.service.interf.KafkaProducerService;
import com.Rakhi1999.Ecommerce_Shop.service.interf.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final OrderRepo orderRepo;
    private final PaymentRepo paymentRepo;
    private final EntityDtoMapper mapper;
    private final KafkaProducerService kafkaProducerService;

    @PostMapping("/create/{orderId}")
    public ResponseEntity<Response> createRazorpayOrder(@PathVariable Long orderId) {
        Orders order = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        JSONObject rpOrder = paymentService.createRazorpayOrder(order);

        // Save payment entity
        Payment payment = new Payment();
        payment.setAmount(order.getTotalPrice());
        payment.setMethod("RAZORPAY");
        payment.setStatus("CREATED");
        payment.setOrder(order);
        paymentRepo.save(payment);

        JSONObject response = new JSONObject();
        response.put("razorpayOrderId", rpOrder.getString("id"));
        response.put("amount", rpOrder.getLong("amount"));
        response.put("currency", rpOrder.getString("currency"));
        response.put("key", System.getenv("RAZORPAY_KEY_ID") != null ? System.getenv("RAZORPAY_KEY_ID") : "");
        response.put("razorpayOrder", rpOrder);

        return ResponseEntity.ok(Response.builder()
                .status(200)
                .message("Razorpay order created")
                .data(response.toMap())
                .build());
    }

    @PostMapping("/verify")
    public ResponseEntity<Response> verifyRazorpayPayment(@RequestBody JSONObject body) {
        String razorpayOrderId = body.optString("razorpay_order_id");
        String razorpayPaymentId = body.optString("razorpay_payment_id");
        String razorpaySignature = body.optString("razorpay_signature");
        Long internalOrderId = body.has("orderId") ? body.getLong("orderId") : null;

        if (internalOrderId == null) {
            return ResponseEntity.badRequest()
                    .body(Response.builder().status(400).message("orderId required").build());
        }

        Orders order = orderRepo.findById(internalOrderId)
                .orElseThrow(() -> new NotFoundException("Order not found"));

        boolean ok = paymentService instanceof com.Rakhi1999.Ecommerce_Shop.service.impl.PaymentServiceImpl
                && ((com.Rakhi1999.Ecommerce_Shop.service.impl.PaymentServiceImpl) paymentService)
                .createRazorpayOrder(order) != null;

        // Use our REST-based RazorpayService to verify signature
        ok = ((com.Rakhi1999.Ecommerce_Shop.service.impl.PaymentServiceImpl) paymentService)
                .paymentServiceVerifySignature(razorpayOrderId, razorpayPaymentId, razorpaySignature);

        if (ok) {
            // Save payment success
            Payment payment = new Payment();
            payment.setAmount(order.getTotalPrice());
            payment.setMethod("RAZORPAY");
            payment.setStatus("SUCCESS");
            payment.setOrder(order);
            paymentRepo.save(payment);

            order.setStatus(OrderStatus.CONFIRMED);
            orderRepo.save(order);

            OrderDto orderDto = mapper.mapOrderToDto(order);
            kafkaProducerService.publishPaymentEvent(Response.builder()
                    .status(200)
                    .message("Payment successful")
                    .data(orderDto)
                    .build());
            kafkaProducerService.publishOrderPlaced(orderDto);

            return ResponseEntity.ok(Response.builder()
                    .status(200)
                    .message("Payment verified and order confirmed")
                    .data(orderDto)
                    .build());
        } else {
            Payment payment = new Payment();
            payment.setAmount(order.getTotalPrice());
            payment.setMethod("RAZORPAY");
            payment.setStatus("FAILED");
            payment.setOrder(order);
            paymentRepo.save(payment);

            kafkaProducerService.publishPaymentEvent(Response.builder()
                    .status(400)
                    .message("Payment verification failed")
                    .data(order.getId())
                    .build());

            return ResponseEntity.status(400)
                    .body(Response.builder().status(400).message("Payment verification failed").build());
        }
    }
}
