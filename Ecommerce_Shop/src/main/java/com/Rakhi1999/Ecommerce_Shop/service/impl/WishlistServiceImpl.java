package com.Rakhi1999.Ecommerce_Shop.service.impl;

import com.Rakhi1999.Ecommerce_Shop.dto.Response;
import com.Rakhi1999.Ecommerce_Shop.entity.Product;
import com.Rakhi1999.Ecommerce_Shop.entity.User;
import com.Rakhi1999.Ecommerce_Shop.entity.Wishlist;
import com.Rakhi1999.Ecommerce_Shop.repository.ProductRepo;
import com.Rakhi1999.Ecommerce_Shop.repository.WishlistRepository;
import com.Rakhi1999.Ecommerce_Shop.service.interf.UserService;
import com.Rakhi1999.Ecommerce_Shop.service.interf.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepo productRepo;
    private final UserService userService;

    @Override
    public Response addToWishlist(Long productId) {
        User user = userService.getCurrentUser();
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (wishlistRepository.existsByUserAndProduct(user, product)) {
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Product already exists in wishlist")
                    .build();
        }

        Wishlist wishlist = new Wishlist(null, user, product);
        wishlistRepository.save(wishlist);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Product added to wishlist")
                .build();
    }

    @Override
    public Response removeFromWishlist(Long productId) {
        User user = userService.getCurrentUser();
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist wishlist = wishlistRepository.findByUserAndProduct(user, product);

        if (wishlist == null) {
            return Response.builder()
                    .status(HttpStatus.BAD_REQUEST.value())
                    .message("Product not found in wishlist")
                    .build();
        }

        wishlistRepository.delete(wishlist);

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Product removed from wishlist")
                .build();
    }

    @Override
    public Response getMyWishlist() {
        User user = userService.getCurrentUser();

        List<Product> products = wishlistRepository.findByUser(user)
                .stream()
                .map(Wishlist::getProduct)
                .toList();

        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Wishlist fetched successfully")
                .data(products)
                .build();
    }
}
