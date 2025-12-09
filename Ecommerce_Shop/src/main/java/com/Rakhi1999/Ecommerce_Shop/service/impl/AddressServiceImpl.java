package com.Rakhi1999.Ecommerce_Shop.service.impl;

import com.Rakhi1999.Ecommerce_Shop.dto.AddressDto;
import com.Rakhi1999.Ecommerce_Shop.dto.Response;
import com.Rakhi1999.Ecommerce_Shop.entity.Address;
import com.Rakhi1999.Ecommerce_Shop.entity.User;
import com.Rakhi1999.Ecommerce_Shop.repository.AddressRepo;

import com.Rakhi1999.Ecommerce_Shop.repository.UserRepo;
import com.Rakhi1999.Ecommerce_Shop.service.interf.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepo addressRepo;
    private final UserRepo userRepo;

    @Override
    public Response saveAndUpdateAddress(Long userId, AddressDto addressDto) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        Address address = user.getAddress();
        boolean isNew = (address == null);

        if (isNew) {
            address = new Address();
            address.setUser(user);
        }

        address.setStreet(addressDto.getStreet());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setZipCode(addressDto.getZipCode());
        address.setCountry(addressDto.getCountry());

        user.setAddress(address);
        addressRepo.save(address);

        return Response.builder()
                .status(200)
                .message(isNew ? "Address successfully created" : "Address successfully updated")
                .build();
    }
}
