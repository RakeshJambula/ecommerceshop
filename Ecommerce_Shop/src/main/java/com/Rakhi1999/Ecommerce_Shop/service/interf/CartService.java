package com.Rakhi1999.Ecommerce_Shop.service.interf;

import com.Rakhi1999.Ecommerce_Shop.entity.CartItem;

import java.util.List;

public interface CartService {
    CartItem addToCart(Long productId);
    List<CartItem> getCartItems();
    CartItem incrementItem(Long productId);
    CartItem decrementItem(Long productId);
    void removeCartItem(Long cartItemId);
    void clearCart();
}
