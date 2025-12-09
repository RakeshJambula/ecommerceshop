//package com.Rakhi1999.Ecommerce_Shop.controller;
//
//import com.Rakhi1999.Ecommerce_Shop.dto.AddressDto;
//import com.Rakhi1999.Ecommerce_Shop.dto.Response;
//import com.Rakhi1999.Ecommerce_Shop.service.interf.AddressService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/address")
//@RequiredArgsConstructor
//public class AddressController {
//
//    private final AddressService addressService;
//
//    @PostMapping("/save")
//    public ResponseEntity<Response> saveAndUpdateAddress(@RequestBody AddressDto addressDto){
//        return ResponseEntity.ok(addressService.saveAndUpdateAddress(addressDto));
//    }
//}
