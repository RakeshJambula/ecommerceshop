//package com.Rakhi1999.Ecommerce_Shop.service.impl;
//
//import com.Rakhi1999.Ecommerce_Shop.dto.OrderRequest;
//import com.Rakhi1999.Ecommerce_Shop.dto.Response;
//import com.Rakhi1999.Ecommerce_Shop.entity.*;
//import com.Rakhi1999.Ecommerce_Shop.enums.OrderStatus;
//import com.Rakhi1999.Ecommerce_Shop.exceptions.NotFoundException;
//import com.Rakhi1999.Ecommerce_Shop.mapper.EntityDtoMapper;
//import com.Rakhi1999.Ecommerce_Shop.repository.*;
//import com.Rakhi1999.Ecommerce_Shop.service.interf.KafkaProducerService;
//import com.Rakhi1999.Ecommerce_Shop.service.interf.OrderItemService;
//import com.Rakhi1999.Ecommerce_Shop.service.interf.UserService;
//import com.Rakhi1999.Ecommerce_Shop.specification.OrderItemSpecification;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class OrderItemServiceImpl implements OrderItemService {
//
//    private final OrderRepo orderRepo;
//    private final AddressRepo addressRepo;
//    private final OrderItemRepo orderItemRepo;
//    private final ProductRepo productRepo;
//    private final UserService userService;
//    private final EntityDtoMapper entityDtoMapper;
//    private final PaymentRepo paymentRepo;
//    private final KafkaProducerService kafkaProducerService; // optional
//    private final com.Rakhi1999.Ecommerce_Shop.service.interf.RazorpayService razorpayService;
//
//    @Override
//    public Response placeOrder(OrderRequest orderRequest) {
//
//        User user = userService.getCurrentUser();
//
//        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//                    "Order must contain at least one product!");
//        }
//
//        // ===== ADDRESS =====
//        Address address = addressRepo
//                .findTopByUserIdOrderByCreatedAtDesc(user.getId())
//                .orElseGet(() -> {
//
//                    if (orderRequest.getStreet() == null) {
//                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//                                "Delivery address required");
//                    }
//
//                    Address newAddress = new Address();
//                    newAddress.setUser(user);
//                    newAddress.setStreet(orderRequest.getStreet());
//                    newAddress.setCity(orderRequest.getCity());
//                    newAddress.setState(orderRequest.getState());
//                    newAddress.setZipCode(orderRequest.getZipCode());
//                    newAddress.setCountry(orderRequest.getCountry());
//
//                    return addressRepo.save(newAddress);
//                });
//
//        String fullAddress = address.getStreet() + ", " +
//                address.getCity() + ", " +
//                address.getState() + " - " +
//                address.getZipCode() + ", " +
//                address.getCountry();
//
//        // ===== ORDER ITEMS =====
//        List<OrderItem> orderItems = orderRequest.getItems().stream().map(itemReq -> {
//
//            Product product = productRepo.findById(itemReq.getProductId())
//                    .orElseThrow(() -> new NotFoundException("Product Not Found"));
//
//            OrderItem item = new OrderItem();
//            item.setProduct(product);
//            item.setQuantity(itemReq.getQuantity());
//            item.setPrice(product.getPrice()
//                    .multiply(BigDecimal.valueOf(itemReq.getQuantity())));
//            item.setStatus(OrderStatus.PENDING);
//            item.setUser(user);
//            return item;
//
//        }).collect(Collectors.toList());
//
//        BigDecimal totalPrice = orderItems.stream()
//                .map(OrderItem::getPrice)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        // ===== ORDER =====
//        Orders order = new Orders();
//        order.setUser(user);
//        order.setTotalPrice(totalPrice);
//        order.setAddress(fullAddress);
//        order.setStatus(OrderStatus.PENDING);
//        order.setOrderItemList(orderItems);
//
//        orderItems.forEach(i -> i.setOrder(order));
//
//        orderRepo.save(order);
//
//        // ===== PAYMENT (COD) =====
//        Payment payment = new Payment();
//        payment.setAmount(totalPrice);
//        payment.setMethod("COD");
//        payment.setStatus("PENDING_COD");
//        payment.setOrder(order);
//        paymentRepo.save(payment);
//
//        order.setStatus(OrderStatus.CONFIRMED);
//        orderItems.forEach(i -> i.setStatus(OrderStatus.CONFIRMED));
//        orderRepo.save(order);
//
//        return Response.builder()
//                .status(200)
//                .message("Order placed successfully")
//                .data(entityDtoMapper.mapOrderToDto(order))
//                .build();
//    }
//
//
//    @Override
//    public Response updateOrderItemStatus(Long orderItemId, String status) {
//        OrderItem orderItem = orderItemRepo.findById(orderItemId)
//                .orElseThrow(() -> new NotFoundException("Order Item not found"));
//
//        orderItem.setStatus(OrderStatus.valueOf(status.toUpperCase()));
//        orderItemRepo.save(orderItem);
//
//        // Optional Kafka publishing
//        try {
//            if (kafkaProducerService != null) {
//                kafkaProducerService.sendOrderItemStatusUpdated(entityDtoMapper.mapOrderItemToDtoPlusProductAndUser(orderItem));
//            }
//        } catch (Exception e) {
//            log.warn("Kafka publish failed: {}", e.getMessage());
//        }
//
//        return Response.builder()
//                .status(200)
//                .message("Order status updated successfully")
//                .build();
//    }
//
//    @Override
//    public Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable) {
//        Specification<OrderItem> spec = Specification.where(OrderItemSpecification.hasStatus(status))
//                .and(OrderItemSpecification.createdBetween(startDate, endDate))
//                .and(OrderItemSpecification.hasItemId(itemId));
//
//        Page<OrderItem> orderItemPage = orderItemRepo.findAll(spec, pageable);
//
//        if (orderItemPage.isEmpty()) {
//            throw new NotFoundException("No Order Found");
//        }
//
//        List<?> orderItemDtos = orderItemPage.getContent().stream()
//                .map(entityDtoMapper::mapOrderItemToDtoPlusProductAndUser)
//                .collect(Collectors.toList());
//
//        return Response.builder()
//                .status(200)
//                .data(orderItemDtos)
//                .totalPage(orderItemPage.getTotalPages())
//                .totalElement(orderItemPage.getTotalElements())
//                .build();
//    }
//}

package com.Rakhi1999.Ecommerce_Shop.service.impl;

import com.Rakhi1999.Ecommerce_Shop.dto.OrderRequest;
import com.Rakhi1999.Ecommerce_Shop.dto.Response;
import com.Rakhi1999.Ecommerce_Shop.entity.*;
import com.Rakhi1999.Ecommerce_Shop.enums.OrderStatus;
import com.Rakhi1999.Ecommerce_Shop.exceptions.NotFoundException;
import com.Rakhi1999.Ecommerce_Shop.mapper.EntityDtoMapper;
import com.Rakhi1999.Ecommerce_Shop.repository.*;
import com.Rakhi1999.Ecommerce_Shop.service.interf.KafkaProducerService;
import com.Rakhi1999.Ecommerce_Shop.service.interf.OrderItemService;
import com.Rakhi1999.Ecommerce_Shop.service.interf.UserService;
import com.Rakhi1999.Ecommerce_Shop.specification.OrderItemSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final AddressRepo addressRepo;
    private final OrderItemRepo orderItemRepo;
    private final ProductRepo productRepo;
    private final UserService userService;
    private final EntityDtoMapper entityDtoMapper;
    private final PaymentRepo paymentRepo;
    private final KafkaProducerService kafkaProducerService;

    // 🔹 NEW SERVICES
    private final OrderTimelineService orderTimelineService;
    private final NotificationService notificationService;

    private final com.Rakhi1999.Ecommerce_Shop.service.interf.RazorpayService razorpayService;

    // PLACE ORDER
    @Override
    public Response placeOrder(OrderRequest orderRequest) {

        User user = userService.getCurrentUser();

        if (orderRequest.getItems() == null || orderRequest.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Order must contain at least one product!");
        }

        // ADDRESS
        Address address = addressRepo
                .findTopByUserIdOrderByCreatedAtDesc(user.getId())
                .orElseGet(() -> {

                    if (orderRequest.getStreet() == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Delivery address required");
                    }

                    Address newAddress = new Address();
                    newAddress.setUser(user);
                    newAddress.setStreet(orderRequest.getStreet());
                    newAddress.setCity(orderRequest.getCity());
                    newAddress.setState(orderRequest.getState());
                    newAddress.setZipCode(orderRequest.getZipCode());
                    newAddress.setCountry(orderRequest.getCountry());

                    return addressRepo.save(newAddress);
                });

        String fullAddress = address.getStreet() + ", " +
                address.getCity() + ", " +
                address.getState() + " - " +
                address.getZipCode() + ", " +
                address.getCountry();

        // ORDER ITEMS
        List<OrderItem> orderItems = orderRequest.getItems().stream().map(itemReq -> {

            Product product = productRepo.findById(itemReq.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product Not Found"));

            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemReq.getQuantity());
            item.setPrice(product.getPrice()
                    .multiply(BigDecimal.valueOf(itemReq.getQuantity())));
            item.setStatus(OrderStatus.PENDING);
            item.setUser(user);
            return item;

        }).collect(Collectors.toList());

        BigDecimal totalPrice = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // ORDER
        Orders order = new Orders();
        order.setUser(user);
        order.setTotalPrice(totalPrice);
        order.setAddress(fullAddress);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderItemList(orderItems);

        orderItems.forEach(i -> i.setOrder(order));

        orderRepo.save(order);

        // ORDER TIMELINE
        orderTimelineService.addTimeline(order, "PENDING", "Order placed successfully");

        // ===== PAYMENT (COD) =====
        Payment payment = new Payment();
        payment.setAmount(totalPrice);
        payment.setMethod("COD");
        payment.setStatus("PENDING_COD");
        payment.setOrder(order);
        paymentRepo.save(payment);

        // CONFIRM ORDER
        order.setStatus(OrderStatus.CONFIRMED);
        orderItems.forEach(i -> i.setStatus(OrderStatus.CONFIRMED));
        orderRepo.save(order);

        // TIMELINE + NOTIFICATION
        orderTimelineService.addTimeline(order, "CONFIRMED", "Order confirmed");
        notificationService.createNotification(user,
                "Your order #" + order.getId() + " has been confirmed");

        return Response.builder()
                .status(200)
                .message("Order placed successfully")
                .data(entityDtoMapper.mapOrderToDto(order))
                .build();
    }

    // UPDATE ORDER ITEM STATUS
    @Override
    public Response updateOrderItemStatus(Long orderItemId, String status) {
        OrderItem orderItem = orderItemRepo.findById(orderItemId)
                .orElseThrow(() -> new NotFoundException("Order Item not found"));

        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
        orderItem.setStatus(newStatus);
        orderItemRepo.save(orderItem);

        Orders order = orderItem.getOrder();

        // TIMELINE
        orderTimelineService.addTimeline(order, newStatus.name(),
                "Order item updated to " + newStatus.name());

        // NOTIFICATION
        notificationService.createNotification(order.getUser(),
                "Your order #" + order.getId() + " is now " + newStatus.name());

        // KAFKA
        try {
            if (kafkaProducerService != null) {
                kafkaProducerService.sendOrderItemStatusUpdated(
                        entityDtoMapper.mapOrderItemToDtoPlusProductAndUser(orderItem)
                );
            }
        } catch (Exception e) {
            log.warn("Kafka publish failed: {}", e.getMessage());
        }

        return Response.builder()
                .status(200)
                .message("Order status updated successfully")
                .build();
    }

    // FILTER ORDER ITEMS
    @Override
    public Response filterOrderItems(OrderStatus status,
                                     LocalDateTime startDate,
                                     LocalDateTime endDate,
                                     Long itemId,
                                     Pageable pageable) {

        Specification<OrderItem> spec = Specification.where(OrderItemSpecification.hasStatus(status))
                .and(OrderItemSpecification.createdBetween(startDate, endDate))
                .and(OrderItemSpecification.hasItemId(itemId));

        Page<OrderItem> orderItemPage = orderItemRepo.findAll(spec, pageable);

        if (orderItemPage.isEmpty()) {
            throw new NotFoundException("No Order Found");
        }

        List<?> orderItemDtos = orderItemPage.getContent().stream()
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
