package com.Rakhi1999.Ecommerce_Shop.service.interf;

import com.Rakhi1999.Ecommerce_Shop.dto.OrderDto;
import com.Rakhi1999.Ecommerce_Shop.dto.OrderItemDto;
import com.Rakhi1999.Ecommerce_Shop.dto.Response;

public interface KafkaProducerService {

    /**
     * Publish a new order placed event
     */
    void publishOrderPlaced(OrderDto orderDto);

    /**
     * Publish order item status update
     */
    void sendOrderItemStatusUpdated(OrderItemDto orderItemDto);

    /**
     * Publish payment related event
     */
    void publishPaymentEvent(Response paymentResponse);
}
