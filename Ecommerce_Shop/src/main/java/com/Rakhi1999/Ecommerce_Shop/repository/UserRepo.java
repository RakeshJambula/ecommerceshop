package com.Rakhi1999.Ecommerce_Shop.repository;

import com.Rakhi1999.Ecommerce_Shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
