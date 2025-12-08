package com.Rakhi1999.Ecommerce_Shop.controller;

import com.Rakhi1999.Ecommerce_Shop.dto.LoginRequest;
import com.Rakhi1999.Ecommerce_Shop.dto.Response;
import com.Rakhi1999.Ecommerce_Shop.dto.UserDto;
import com.Rakhi1999.Ecommerce_Shop.service.interf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody UserDto registrationRequest) {
        Response response = userService.registerUser(registrationRequest);

        // Check if backend returned error in JSON
        if (response.getStatus() == 400) {
            return ResponseEntity
                    .status(400)
                    .body(response);
        }

        return ResponseEntity
                .status(200)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }
}
