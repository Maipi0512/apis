package com.uade.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.AddressRequestDTO;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.Address;
import com.uade.ecom.repository.AddressRepository;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ninguna direccion con id " + id));
    }

    @Override
    public Address createAddress(AddressRequestDTO addressRequestDTO) {
        Address address = new Address();
        address.setCalle(addressRequestDTO.getCalle());
        address.setCiudad(addressRequestDTO.getCiudad());
        return addressRepository.save(address);
    }
}
