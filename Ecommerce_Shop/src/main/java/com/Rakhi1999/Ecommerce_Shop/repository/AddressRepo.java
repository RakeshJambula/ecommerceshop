package com.Rakhi1999.Ecommerce_Shop.repository;


import com.Rakhi1999.Ecommerce_Shop.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<Address, Long> {
    Optional<Address> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}