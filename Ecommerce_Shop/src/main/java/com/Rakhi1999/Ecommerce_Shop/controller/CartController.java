package com.Rakhi1999.Ecommerce_Shop.controller;

import com.Rakhi1999.Ecommerce_Shop.dto.CartItemDTO;
import com.Rakhi1999.Ecommerce_Shop.service.interf.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add/{productId}")
    public ResponseEntity<CartItemDTO> addToCart(@PathVariable Long productId) {
        return ResponseEntity.ok(cartService.addToCartDTO(productId));
    }

    @GetMapping("/my")
    public ResponseEntity<List<CartItemDTO>> getCartItems() {
        return ResponseEntity.ok(cartService.getCartItems());
    }

    @PutMapping("/increment/{productId}")
    public ResponseEntity<CartItemDTO> increment(@PathVariable Long productId) {
        return ResponseEntity.ok(cartService.incrementItem(productId));
    }

    @PutMapping("/decrement/{productId}")
    public ResponseEntity<?> decrement(@PathVariable Long productId) {
        CartItemDTO ci = cartService.decrementItem(productId);
        return ci == null ? ResponseEntity.ok().build() : ResponseEntity.ok(ci);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long cartItemId) {
        cartService.removeCartItem(cartItemId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok().build();
    }
}
