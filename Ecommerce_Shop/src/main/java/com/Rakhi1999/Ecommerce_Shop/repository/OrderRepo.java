package com.Rakhi1999.Ecommerce_Shop.repository;


import com.Rakhi1999.Ecommerce_Shop.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepo extends JpaRepository<Orders, Long> {
    Optional<Orders> findByPaymentId(String paymentId);
}