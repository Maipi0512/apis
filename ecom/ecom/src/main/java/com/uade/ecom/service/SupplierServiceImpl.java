package com.uade.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.SupplierRequestDTO;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.Supplier;
import com.uade.ecom.repository.SupplierRepository;

@Service
public class SupplierServiceImpl implements SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    @Override
    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun proveedor con id " + id));
    }

    @Override
    public Supplier createSupplier(SupplierRequestDTO supplierRequestDTO) {
        Supplier supplier = new Supplier();
        supplier.setNombre(supplierRequestDTO.getNombre());
        supplier.setContacto(supplierRequestDTO.getContacto());
        return supplierRepository.save(supplier);
    }
}
