package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.AddressRequestDTO;
import com.uade.ecom.model.Address;

public interface AddressService {

    List<Address> getAllAddresses();

    Address getAddressById(Long id);

    Address createAddress(AddressRequestDTO addressRequestDTO);
}
