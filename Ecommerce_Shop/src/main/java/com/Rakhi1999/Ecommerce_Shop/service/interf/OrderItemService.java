package com.Rakhi1999.Ecommerce_Shop.service.interf;

import com.Rakhi1999.Ecommerce_Shop.dto.OrderRequest;
import com.Rakhi1999.Ecommerce_Shop.dto.Response;
import com.Rakhi1999.Ecommerce_Shop.enums.OrderStatus;

//import java.awt.print.Pageable;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;

public interface OrderItemService {

    Response placeOrder(OrderRequest orderRequest);
    Response updateOrderItemStatus(Long orderItemId, String status);
    Response filterOrderItems(OrderStatus status, LocalDateTime startDate, LocalDateTime endDate, Long itemId, Pageable pageable);



}
