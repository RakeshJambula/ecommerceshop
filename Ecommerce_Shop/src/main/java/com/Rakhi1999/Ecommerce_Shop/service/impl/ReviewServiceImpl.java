package com.Rakhi1999.Ecommerce_Shop.service.impl;

import com.Rakhi1999.Ecommerce_Shop.dto.ReviewDTO;
import com.Rakhi1999.Ecommerce_Shop.entity.Product;
import com.Rakhi1999.Ecommerce_Shop.entity.Review;
import com.Rakhi1999.Ecommerce_Shop.entity.User;
import com.Rakhi1999.Ecommerce_Shop.repository.ProductRepo;
import com.Rakhi1999.Ecommerce_Shop.repository.ReviewRepository;
import com.Rakhi1999.Ecommerce_Shop.repository.UserRepo;
import com.Rakhi1999.Ecommerce_Shop.service.interf.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProductRepo productRepo;

    @Override
    public Review addReview(Long userId, ReviewDTO dto) {

        // Check if already reviewed
        reviewRepository.findByUserIdAndProductId(userId, dto.getProductId())
                .ifPresent(r -> {
                    throw new RuntimeException("You have already reviewed this product!");
                });

        Product product = productRepo.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = new Review();
        review.setContent(dto.getContent());
        review.setRating(dto.getRating());
        review.setProduct(product);
        review.setUser(user);

        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(Long userId, Long reviewId, ReviewDTO dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to update this review!");
        }

        review.setContent(dto.getContent());
        review.setRating(dto.getRating());

        return reviewRepository.save(review);
    }

    @Override
    public void deleteReview(Long userId, Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not allowed to delete this review!");
        }

        reviewRepository.delete(review);
    }

    @Override
    public List<Review> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProductId(productId);
    }
}
