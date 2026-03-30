package com.Rakhi1999.Ecommerce_Shop.controller;

import com.Rakhi1999.Ecommerce_Shop.dto.CouponRequest;
import com.Rakhi1999.Ecommerce_Shop.dto.CouponValidationRequest;
import com.Rakhi1999.Ecommerce_Shop.dto.CouponValidationResponse;
import com.Rakhi1999.Ecommerce_Shop.dto.Response;
import com.Rakhi1999.Ecommerce_Shop.entity.Coupon;
import com.Rakhi1999.Ecommerce_Shop.service.interf.CouponService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;


    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> createCoupon(@RequestBody CouponRequest request) {

        Coupon coupon = couponService.createCoupon(request);

        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Coupon created successfully")
                        .data(coupon)
                        .build()
        );
    }


    @PutMapping("/toggle/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> toggleCoupon(
            @PathVariable Long id,
            @RequestParam boolean active
    ) {

        couponService.toggleCoupon(id, active);

        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .message("Coupon updated")
                        .build()
        );
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllCoupons() {

        List<Coupon> coupons = couponService.getAllCoupons();

        return ResponseEntity.ok(
                Response.builder()
                        .status(200)
                        .data(coupons)
                        .build()
        );
    }


    @PostMapping("/validate")
    public ResponseEntity<CouponValidationResponse> validateCoupon(
            @RequestBody CouponValidationRequest request) {

        return ResponseEntity.ok(
                couponService.validateCouponPreview(
                        request.getCouponCode(),
                        request.getAmount()
                )
        );
    }
}