package com.Rakhi1999.Ecommerce_Shop.service.impl;

import com.Rakhi1999.Ecommerce_Shop.dto.CartItemDTO;
import com.Rakhi1999.Ecommerce_Shop.entity.CartItem;
import com.Rakhi1999.Ecommerce_Shop.entity.Product;
import com.Rakhi1999.Ecommerce_Shop.entity.User;
import com.Rakhi1999.Ecommerce_Shop.exceptions.NotFoundException;
import com.Rakhi1999.Ecommerce_Shop.repository.CartItemRepository;
import com.Rakhi1999.Ecommerce_Shop.repository.ProductRepo;
import com.Rakhi1999.Ecommerce_Shop.service.interf.CartService;
import com.Rakhi1999.Ecommerce_Shop.service.interf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepo productRepo;
    private final UserService userService;

    @Override
    public CartItemDTO addToCartDTO(Long productId) {
        User user = userService.getCurrentUser();
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product)
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setUser(user);
                    ci.setProduct(product);
                    ci.setQuantity(0);
                    return ci;
                });

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        CartItem saved = cartItemRepository.save(cartItem);

        return new CartItemDTO(
                saved.getId(),
                product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice(),
                saved.getQuantity()
        );
    }

    @Override
    public List<CartItemDTO> getCartItems() {
        User user = userService.getCurrentUser();

        return cartItemRepository.findByUser(user).stream()
                .map(ci -> new CartItemDTO(
                        ci.getId(),
                        ci.getProduct().getId(),
                        ci.getProduct().getName(),
                        ci.getProduct().getImageUrl(),
                        ci.getProduct().getPrice(),
                        ci.getQuantity()
                ))
                .toList();
    }

    public CartItemDTO incrementItem(Long productId) {
        User user = userService.getCurrentUser();
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new NotFoundException("Cart item not found"));

        cartItem.setQuantity(cartItem.getQuantity() + 1);
        CartItem saved = cartItemRepository.save(cartItem);

        return new CartItemDTO(
                saved.getId(),
                saved.getProduct().getId(),
                saved.getProduct().getName(),
                saved.getProduct().getImageUrl(),
                saved.getProduct().getPrice(),
                saved.getQuantity()
        );
    }

    @Override
    public CartItemDTO decrementItem(Long productId) {
        User user = userService.getCurrentUser();

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        CartItem cartItem = cartItemRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new NotFoundException("Cart item not found"));

        if (cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            CartItem saved = cartItemRepository.save(cartItem);

            return new CartItemDTO(
                    saved.getId(),
                    saved.getProduct().getId(),
                    saved.getProduct().getName(),
                    saved.getProduct().getImageUrl(),
                    saved.getProduct().getPrice(),
                    saved.getQuantity()
            );
        } else {
            cartItemRepository.delete(cartItem);
            return null; // item removed
        }
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void clearCart() {
        User user = userService.getCurrentUser();
        cartItemRepository.deleteByUser(user);
    }
}
