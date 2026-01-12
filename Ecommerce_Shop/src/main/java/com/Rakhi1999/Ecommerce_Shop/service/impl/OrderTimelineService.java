package com.Rakhi1999.Ecommerce_Shop.service.impl;

import com.Rakhi1999.Ecommerce_Shop.dto.OrderTimelineDTO;
import com.Rakhi1999.Ecommerce_Shop.entity.OrderTimeline;
import com.Rakhi1999.Ecommerce_Shop.entity.Orders;
import com.Rakhi1999.Ecommerce_Shop.repository.OrderTimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderTimelineService {

    private final OrderTimelineRepository repository;

    public void addTimeline(Orders order, String status, String comment) {
        OrderTimeline timeline = new OrderTimeline();
        timeline.setOrder(order);
        timeline.setStatus(status);
        timeline.setComment(comment);
        timeline.setTimestamp(LocalDateTime.now());
        repository.save(timeline);
    }

    public List<OrderTimelineDTO> getTimeline(Long orderId) {
        return repository.findByOrderIdOrderByTimestampAsc(orderId)
                .stream()
                .map(t -> new OrderTimelineDTO(
                        t.getId(),
                        t.getStatus(),
                        t.getComment(),
                        t.getTimestamp()
                ))
                .toList();
    }

}
