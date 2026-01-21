package com.Rakhi1999.Ecommerce_Shop.service.interf;

import com.Rakhi1999.Ecommerce_Shop.dto.CartItemDTO;

import java.util.List;

public interface CartService {
    CartItemDTO addToCartDTO(Long productId);
    List<CartItemDTO> getCartItems();
    CartItemDTO incrementItem(Long productId);
    CartItemDTO decrementItem(Long productId);
    void removeCartItem(Long cartItemId);
    void clearCart();
}
