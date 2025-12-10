package com.Rakhi1999.Ecommerce_Shop.repository;

import com.Rakhi1999.Ecommerce_Shop.entity.Wishlist;
import com.Rakhi1999.Ecommerce_Shop.entity.User;
import com.Rakhi1999.Ecommerce_Shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUser(User user);
    Boolean existsByUserAndProduct(User user, Product product);
    void deleteByUserAndProduct(User user, Product product);
    Wishlist findByUserAndProduct(User user, Product product);
}
