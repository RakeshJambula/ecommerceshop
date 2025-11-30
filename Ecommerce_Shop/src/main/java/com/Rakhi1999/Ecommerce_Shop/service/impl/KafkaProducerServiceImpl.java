package com.Rakhi1999.Ecommerce_Shop.service.impl;

import com.Rakhi1999.Ecommerce_Shop.dto.OrderDto;
import com.Rakhi1999.Ecommerce_Shop.dto.OrderItemDto;
import com.Rakhi1999.Ecommerce_Shop.dto.Response;
import com.Rakhi1999.Ecommerce_Shop.service.interf.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topic.order:order-topic}")
    private String orderTopic;

    @Value("${app.kafka.topic.order-item:order-item-topic}")
    private String orderItemTopic;

    @Value("${app.kafka.topic.payment:payment-topic}")
    private String paymentTopic;

    @Override
    public void publishOrderPlaced(OrderDto orderDto) {
        kafkaTemplate.send(orderTopic, orderDto);
        log.info("Published order placed event for order id={}", orderDto.getId());
    }

    @Override
    public void sendOrderItemStatusUpdated(OrderItemDto orderItemDto) {
        kafkaTemplate.send(orderItemTopic, orderItemDto);
        log.info("Published order item status updated for orderItem id={}", orderItemDto.getId());
    }

    @Override
    public void publishPaymentEvent(Response paymentResponse) {
        kafkaTemplate.send(paymentTopic, paymentResponse);
        log.info("Published payment event: {}", paymentResponse.getMessage());
    }
}
