package com.Rakhi1999.Ecommerce_Shop.service.impl;

import com.Rakhi1999.Ecommerce_Shop.dto.CouponRequest;
import com.Rakhi1999.Ecommerce_Shop.dto.CouponValidationResponse;
import com.Rakhi1999.Ecommerce_Shop.entity.Coupon;
import com.Rakhi1999.Ecommerce_Shop.exceptions.NotFoundException;
import com.Rakhi1999.Ecommerce_Shop.repository.CouponRepo;
import com.Rakhi1999.Ecommerce_Shop.service.interf.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepo couponRepo;

    @Override
    public Coupon createCoupon(CouponRequest request) {

        Coupon coupon = new Coupon();

        coupon.setCode(request.getCode());
        coupon.setDiscountType(request.getDiscountType());
        coupon.setDiscountValue(request.getDiscountValue());
        coupon.setMinOrderAmount(request.getMinOrderAmount());
        coupon.setExpiryDate(request.getExpiryDate());
        coupon.setUsageLimit(request.getUsageLimit());
        coupon.setUsedCount(0);
        coupon.setActive(true);

        return couponRepo.save(coupon);
    }

    @Override
    public void toggleCoupon(Long couponId, boolean active) {

        Coupon coupon = couponRepo.findById(couponId)
                .orElseThrow(() -> new NotFoundException("Coupon not found"));

        coupon.setActive(active);
        couponRepo.save(coupon);
    }

    @Override
    public List<Coupon> getAllCoupons() {
        return couponRepo.findAll();
    }

    @Override
    public Coupon validateCoupon(String code, BigDecimal totalAmount) {

        Coupon coupon = couponRepo.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Invalid coupon"));

        if (!coupon.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coupon is inactive");
        }

        if (coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Coupon expired");
        }

        if (coupon.getMinOrderAmount() != null &&
                totalAmount.compareTo(coupon.getMinOrderAmount()) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Minimum order amount not met"
            );
        }

        if (coupon.getUsageLimit() != null &&
                coupon.getUsedCount() >= coupon.getUsageLimit()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Coupon usage limit exceeded"
            );
        }

        return coupon;
    }

    @Override
    public CouponValidationResponse validateCouponPreview(String code, BigDecimal amount) {

        Coupon coupon = couponRepo.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid coupon"));

        // Expiry check
        if (coupon.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Coupon expired");
        }

        // Active check
        if (!coupon.isActive()) {
            throw new RuntimeException("Coupon inactive");
        }

        // Min amount check
        if (amount.compareTo(coupon.getMinOrderAmount()) < 0) {
            throw new RuntimeException("Minimum order amount not met");
        }

        BigDecimal discount;

        if ("PERCENTAGE".equals(coupon.getDiscountType())) {
            discount = amount.multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100));
        } else {
            discount = coupon.getDiscountValue();
        }

        BigDecimal finalAmount = amount.subtract(discount);

        return CouponValidationResponse.builder()
                .valid(true)
                .discount(discount)
                .finalAmount(finalAmount)
                .message("Coupon applied successfully")
                .build();
    }

    @Override
    public BigDecimal applyDiscount(Coupon coupon, BigDecimal totalAmount) {

        if ("PERCENTAGE".equalsIgnoreCase(coupon.getDiscountType())) {

            return totalAmount.subtract(
                    totalAmount.multiply(coupon.getDiscountValue())
                            .divide(BigDecimal.valueOf(100))
            );

        } else {

            return totalAmount.subtract(coupon.getDiscountValue());
        }
    }

    @Override
    public void incrementUsage(Coupon coupon) {
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepo.save(coupon);
    }
}