package com.Rakhi1999.Ecommerce_Shop.service.impl;


import com.Rakhi1999.Ecommerce_Shop.dto.LoginRequest;
import com.Rakhi1999.Ecommerce_Shop.dto.Response;
import com.Rakhi1999.Ecommerce_Shop.dto.UserDto;
import com.Rakhi1999.Ecommerce_Shop.entity.User;
import com.Rakhi1999.Ecommerce_Shop.enums.UserRole;
import com.Rakhi1999.Ecommerce_Shop.exceptions.InvalidCredentialsException;
import com.Rakhi1999.Ecommerce_Shop.exceptions.NotFoundException;
import com.Rakhi1999.Ecommerce_Shop.mapper.EntityDtoMapper;
import com.Rakhi1999.Ecommerce_Shop.repository.UserRepo;
import com.Rakhi1999.Ecommerce_Shop.security.JwtUtils;
import com.Rakhi1999.Ecommerce_Shop.service.interf.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final EntityDtoMapper entityDtoMapper;


//    @Override
//    public Response registerUser(UserDto registrationRequest) {
//        UserRole role = UserRole.USER;
//
//        if (registrationRequest.getRole() != null && registrationRequest.getRole().equalsIgnoreCase("admin")) {
//            role = UserRole.ADMIN;
//        }
//
//        User user = User.builder()
//                .name(registrationRequest.getName())
//                .email(registrationRequest.getEmail())
//                .password(passwordEncoder.encode(registrationRequest.getPassword()))
//                .phoneNumber(registrationRequest.getPhoneNumber())
//                .role(role)
//                .build();
//
//        User savedUser = userRepo.save(user);
//        System.out.println(savedUser);
//
//        UserDto userDto = entityDtoMapper.mapUserToDtoBasic(savedUser);
//        return Response.builder()
//                .status(200)
//                .message("User Successfully Added")
//                .user(userDto)
//                .build();
//    }

    @Override
    public Response registerUser(UserDto registrationRequest) {

        // Check if email already exists
        if (userRepo.existsByEmail(registrationRequest.getEmail())) {
            return Response.builder()
                    .status(400)
                    .message("User already registered with this email!")
                    .build();
        }

        UserRole role = UserRole.USER;

        if (registrationRequest.getRole() != null &&
                registrationRequest.getRole().equalsIgnoreCase("admin")) {
            role = UserRole.ADMIN;
        }

        User user = User.builder()
                .name(registrationRequest.getName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .phoneNumber(registrationRequest.getPhoneNumber())
                .role(role)
                .build();

        User savedUser = userRepo.save(user);

        UserDto userDto = entityDtoMapper.mapUserToDtoBasic(savedUser);
        return Response.builder()
                .status(200)
                .message("User Successfully Registered")
                .user(userDto)
                .build();
    }



    @Override
    public Response loginUser(LoginRequest loginRequest) {

        User user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new NotFoundException("Email not found"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Password does not match");
        }
        String token = jwtUtils.generateToken(user);

        return Response.builder()
                .status(200)
                .message("User Successfully Logged In")
                .token(token)
                .expirationTime("6 Month")
                .role(user.getRole().name())
                .build();
    }

    @Override
    public Response getAllUsers() {

        List<User> users = userRepo.findAll();
        List<UserDto> userDtos = users.stream()
                .map(entityDtoMapper::mapUserToDtoBasic)
                .toList();

        return Response.builder()
                .status(200)
                .userList(userDtos)
                .build();
    }

    @Override
    public User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String  email = authentication.getName();
        log.info("User Email is: " + email);
        return userRepo.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User Not found"));
    }

    @Override
    public Response getUserInfoAndOrderHistory() {
        User user = getLoginUser();
        UserDto userDto = entityDtoMapper.mapUserToDtoPlusAddressAndOrderHistory(user);

        return Response.builder()
                .status(200)
                .user(userDto)
                .build();
    }
}