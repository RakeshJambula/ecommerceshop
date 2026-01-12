package com.Rakhi1999.Ecommerce_Shop.repository;

import com.Rakhi1999.Ecommerce_Shop.entity.OrderTimeline;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderTimelineRepository extends JpaRepository<OrderTimeline, Long> {
    List<OrderTimeline> findByOrderIdOrderByTimestampAsc(Long orderId);
}
