package com.Rakhi1999.Ecommerce_Shop.controller;

import com.Rakhi1999.Ecommerce_Shop.dto.AddressDto;
import com.Rakhi1999.Ecommerce_Shop.dto.Response;
import com.Rakhi1999.Ecommerce_Shop.security.JwtUtils;
import com.Rakhi1999.Ecommerce_Shop.service.interf.AddressService;
import com.Rakhi1999.Ecommerce_Shop.service.interf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AddressService addressService;
    private final JwtUtils jwtUtils;

    private Long extractUserId(String token) {
        return jwtUtils.extractUserId(token.substring(7));
    }

    @GetMapping("/get-all")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/my-info")
    public ResponseEntity<Response> getUserInfoAndOrderHistory(){
        return ResponseEntity.ok(userService.getUserInfoAndOrderHistory());
    }

    @PostMapping("/save-address")
    public ResponseEntity<Response> saveAddress(
            @RequestHeader("Authorization") String token,
            @RequestBody AddressDto addressDto) {

        Long userId = extractUserId(token);
        return ResponseEntity.ok(addressService.saveAndUpdateAddress(userId, addressDto));
    }

    @PutMapping("/update-address")
    public ResponseEntity<Response> updateAddress(
            @RequestHeader("Authorization") String token,
            @RequestBody AddressDto addressDto) {

        Long userId = extractUserId(token);
        return ResponseEntity.ok(addressService.saveAndUpdateAddress(userId, addressDto));
    }
}
