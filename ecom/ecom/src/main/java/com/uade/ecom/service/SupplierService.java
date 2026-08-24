package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.SupplierRequestDTO;
import com.uade.ecom.model.Supplier;

public interface SupplierService {

    List<Supplier> getAllSuppliers();

    Supplier getSupplierById(Long id);

    Supplier createSupplier(SupplierRequestDTO supplierRequestDTO);
}
