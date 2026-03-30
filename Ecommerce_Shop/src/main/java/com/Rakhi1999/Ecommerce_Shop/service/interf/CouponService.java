package com.Rakhi1999.Ecommerce_Shop.service.interf;

import com.Rakhi1999.Ecommerce_Shop.dto.CouponRequest;
import com.Rakhi1999.Ecommerce_Shop.dto.CouponValidationResponse;
import com.Rakhi1999.Ecommerce_Shop.entity.Coupon;

import java.math.BigDecimal;
import java.util.List;

public interface CouponService {

    Coupon createCoupon(CouponRequest request);

    void toggleCoupon(Long couponId, boolean active);

    List<Coupon> getAllCoupons();

    Coupon validateCoupon(String code, BigDecimal totalAmount);

    CouponValidationResponse validateCouponPreview(String code, BigDecimal amount);

    BigDecimal applyDiscount(Coupon coupon, BigDecimal totalAmount);

    void incrementUsage(Coupon coupon);
}