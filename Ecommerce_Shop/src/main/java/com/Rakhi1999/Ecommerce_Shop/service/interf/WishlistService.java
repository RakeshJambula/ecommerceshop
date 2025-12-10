package com.Rakhi1999.Ecommerce_Shop.service.interf;

import com.Rakhi1999.Ecommerce_Shop.dto.Response;

public interface WishlistService {
    Response addToWishlist(Long productId);
    Response removeFromWishlist(Long productId);
    Response getMyWishlist();
}
