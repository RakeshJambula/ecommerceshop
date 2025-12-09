package com.Rakhi1999.Ecommerce_Shop.service.interf;

import com.Rakhi1999.Ecommerce_Shop.dto.AddressDto;
import com.Rakhi1999.Ecommerce_Shop.dto.Response;

public interface AddressService {
    Response saveAndUpdateAddress(Long userId, AddressDto addressDto);
}

