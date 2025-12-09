package com.Rakhi1999.Ecommerce_Shop.controller;

import com.Rakhi1999.Ecommerce_Shop.dto.ReviewDTO;
import com.Rakhi1999.Ecommerce_Shop.entity.Review;
import com.Rakhi1999.Ecommerce_Shop.security.JwtUtils;
import com.Rakhi1999.Ecommerce_Shop.service.interf.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JwtUtils jwtUtil;

    private Long getUserIdFromToken(String token) {
        String jwt = token.substring(7);
        return jwtUtil.extractUserId(jwt);
    }

    @PostMapping("/add")
    public Review addReview(@RequestHeader("Authorization") String token,
                            @RequestBody ReviewDTO dto) {
        Long userId = getUserIdFromToken(token);
        return reviewService.addReview(userId, dto);
    }

    @PutMapping("/update/{reviewId}")
    public Review updateReview(@RequestHeader("Authorization") String token,
                               @PathVariable Long reviewId,
                               @RequestBody ReviewDTO dto) {
        Long userId = getUserIdFromToken(token);
        return reviewService.updateReview(userId, reviewId, dto);
    }

    @DeleteMapping("/delete/{reviewId}")
    public String deleteReview(@RequestHeader("Authorization") String token,
                               @PathVariable Long reviewId) {
        Long userId = getUserIdFromToken(token);
        reviewService.deleteReview(userId, reviewId);
        return "Review deleted successfully!";
    }

    @GetMapping("/product/{productId}")
    public List<Review> getReviewsByProduct(@PathVariable Long productId) {
        return reviewService.getReviewsByProduct(productId);
    }
}
