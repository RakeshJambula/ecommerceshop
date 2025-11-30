package com.Rakhi1999.Ecommerce_Shop.repository;

import com.Rakhi1999.Ecommerce_Shop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Payment findByOrderId(Long orderId);
}
