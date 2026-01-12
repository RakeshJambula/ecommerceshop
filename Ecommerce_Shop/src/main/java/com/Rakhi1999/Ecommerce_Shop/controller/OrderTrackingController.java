package com.Rakhi1999.Ecommerce_Shop.controller;

import com.Rakhi1999.Ecommerce_Shop.dto.OrderTimelineDTO;
import com.Rakhi1999.Ecommerce_Shop.entity.OrderTimeline;
import com.Rakhi1999.Ecommerce_Shop.service.impl.OrderTimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderTrackingController {

    private final OrderTimelineService timelineService;

    @GetMapping("/{orderId}/timeline")
    public List<OrderTimelineDTO> getOrderTimeline(@PathVariable Long orderId) {
        return timelineService.getTimeline(orderId);
    }
}
