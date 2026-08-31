package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.ProveedorRequestDTO;
import com.uade.ecom.model.Proveedor;

public interface ProveedorService {

    List<Proveedor> getAllProveedores();

    Proveedor getProveedorById(Long id);

    Proveedor createProveedor(ProveedorRequestDTO proveedorRequestDTO);

    Proveedor updateProveedor(Long id, ProveedorRequestDTO proveedorRequestDTO);

    void deleteProveedor(Long id);
}
