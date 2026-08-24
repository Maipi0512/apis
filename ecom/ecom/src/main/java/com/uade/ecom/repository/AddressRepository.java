package com.uade.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.ecom.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
