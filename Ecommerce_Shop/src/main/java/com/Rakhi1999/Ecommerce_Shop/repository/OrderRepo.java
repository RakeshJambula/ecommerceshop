package com.Rakhi1999.Ecommerce_Shop.repository;


import com.Rakhi1999.Ecommerce_Shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
}