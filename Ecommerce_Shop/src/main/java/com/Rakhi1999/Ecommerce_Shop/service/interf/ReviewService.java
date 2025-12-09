package com.Rakhi1999.Ecommerce_Shop.service.interf;

import com.Rakhi1999.Ecommerce_Shop.dto.ReviewDTO;
import com.Rakhi1999.Ecommerce_Shop.entity.Review;

import java.util.List;

public interface ReviewService {
    Review addReview(Long userId, ReviewDTO dto);
    Review updateReview(Long userId, Long reviewId, ReviewDTO dto);
    void deleteReview(Long userId, Long reviewId);
    List<Review> getReviewsByProduct(Long productId);
}
