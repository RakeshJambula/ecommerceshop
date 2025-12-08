package com.Rakhi1999.Ecommerce_Shop.service.impl;

import com.Rakhi1999.Ecommerce_Shop.dto.OrderRequest;
import com.Rakhi1999.Ecommerce_Shop.dto.Response;
import com.Rakhi1999.Ecommerce_Shop.entity.*;
import com.Rakhi1999.Ecommerce_Shop.enums.OrderStatus;
import com.Rakhi1999.Ecommerce_Shop.enums.PaymentMethod;
import com.Rakhi1999.Ecommerce_Shop.exceptions.NotFoundException;
import com.Rakhi1999.Ecommerce_Shop.mapper.EntityDtoMapper;
import com.Rakhi1999.Ecommerce_Shop.repository.OrderItemRepo;
import com.Rakhi1999.Ecommerce_Shop.repository.OrderRepo;
import com.Rakhi1999.Ecommerce_Shop.repository.PaymentRepo;
import com.Rakhi1999.Ecommerce_Shop.repository.ProductRepo;
import com.Rakhi1999.Ecommerce_Shop.service.interf.KafkaProducerService;
import com.Rakhi1999.Ecommerce_Shop.service.interf.OrderItemService;
import com.Rakhi1999.Ecommerce_Shop.service.interf.UserService;
import com.Rakhi1999.Ecommerce_Shop.specification.OrderItemSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductRepo productRepo;
    private final UserService userService;
    private final EntityDtoMapper entityDtoMapper;
    private final PaymentRepo paymentRepo;
    private final KafkaProducerService kafkaProducerService;
    private final com.Rakhi1999.Ecommerce_Shop.service.interf.RazorpayService razorpayService;

    @Override
    public Response placeOrder(OrderRequest orderRequest) {

        User user = userService.getLoginUser();

        // Validate address
        if (orderRequest.getAddress() == null || orderRequest.getAddress().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Delivery address is required to place an order!");
        }

        // Validate items
        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Order must contain at least one product!");
        }

        List<OrderItem> orderItems = orderRequest.getItems().stream().map(itemReq -> {
            Product product = productRepo.findById(itemReq.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product Not Found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setPrice(product.getPrice()
                    .multiply(BigDecimal.valueOf(itemReq.getQuantity())));
            orderItem.setStatus(OrderStatus.PENDING);
            orderItem.setUser(user);
            return orderItem;
        }).collect(Collectors.toList());

        BigDecimal totalPrice = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Orders order = new Orders();
        order.setOrderItemList(orderItems);
        order.setTotalPrice(totalPrice);
        order.setAddress(orderRequest.getAddress());
        order.setStatus(OrderStatus.PENDING);
        order.setUser(user);

        orderItems.forEach(item -> item.setOrder(order));

        // Save Order first
        orderRepo.save(order);

        // Set default payment method
        if (orderRequest.getPaymentMethod() == null) {
            orderRequest.setPaymentMethod(PaymentMethod.COD);
        }

        // COD Payment Processing
        if (orderRequest.getPaymentMethod() == PaymentMethod.COD) {

            Payment payment = new Payment();
            payment.setAmount(totalPrice);
            payment.setMethod("COD");
            payment.setStatus("PENDING_COD");
            payment.setOrder(order);
            paymentRepo.save(payment);

            order.getOrderItemList()
                    .forEach(oi -> oi.setStatus(OrderStatus.CONFIRMED));
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepo.save(order);

            kafkaProducerService.publishOrderPlaced(entityDtoMapper.mapOrderToDto(order));

            return Response.builder()
                    .status(200)
                    .message("Order placed successfully with COD")
                    .data(entityDtoMapper.mapOrderToDto(order))
                    .build();
        }

        // Razorpay Payment Request
        long amountInPaise = totalPrice.multiply(BigDecimal.valueOf(100)).longValue();
        JSONObject rpOrder = razorpayService.createRazorpayOrder(order.getId(), amountInPaise,
                "INR", "receipt_" + order.getId());

        Payment payment = new Payment();
        payment.setAmount(totalPrice);
        payment.setMethod("RAZORPAY");
        payment.setStatus("CREATED");
        payment.setOrder(order);
        paymentRepo.save(payment);

        JSONObject resp = new JSONObject();
        resp.put("razorpayOrderId", rpOrder.getString("id"));
        resp.put("amount", rpOrder.getLong("amount"));
        resp.put("currency", rpOrder.getString("currency"));
        resp.put("orderId", order.getId());

        return Response.builder()
                .status(200)
                .message("Razorpay order created. Complete payment on client and verify.")
                .data(resp.toMap())
                .build();
    }


    @Override
    public Response updateOrderItemStatus(Long orderItemId, String status) {
        OrderItem orderItem = orderItemRepo.findById(orderItemId)
                .orElseThrow(() -> new NotFoundException("Order Item not found"));

        orderItem.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderItemRepo.save(orderItem);

        kafkaProducerService.sendOrderItemStatusUpdated(entityDtoMapper.mapOrderItemToDtoPlusProductAndUser(orderItem));

        return Response.builder()
                .status(200)
                .message("Order status updated successfully")
                .build();
    }

    @Override
    public Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable) {
        Specification<OrderItem> spec = Specification.where(OrderItemSpecification.hasStatus(status))
                .and(OrderItemSpecification.createdBetween(startDate, endDate))
                .and(OrderItemSpecification.hasItemId(itemId));

        Page<OrderItem> orderItemPage = orderItemRepo.findAll(spec, pageable);

        if (orderItemPage.isEmpty()) {
            throw new NotFoundException("No Order Found");
        }

        List<OrderItem> orderItems = orderItemPage.getContent();
        List<?> orderItemDtos = orderItems.stream()
                .map(entityDtoMapper::mapOrderItemToDtoPlusProductAndUser)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .data(orderItemDtos)
                .totalPage(orderItemPage.getTotalPages())
                .totalElement(orderItemPage.getTotalElements())
                .build();
    }
}
