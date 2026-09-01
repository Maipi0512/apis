package com.uade.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.ProveedorRequestDTO;
import com.uade.ecom.exception.EntidadEnUsoException;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.Proveedor;
import com.uade.ecom.repository.ProductoRepository;
import com.uade.ecom.repository.ProveedorRepository;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Proveedor> getAllProveedores() {
        return proveedorRepository.findAll();
    }

    @Override
    public Proveedor getProveedorById(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun proveedor con id " + id));
    }

    @Override
    public Proveedor createProveedor(ProveedorRequestDTO proveedorRequestDTO) {
        Proveedor proveedor = new Proveedor();
        proveedor.setNombre(proveedorRequestDTO.getNombre());
        proveedor.setContacto(proveedorRequestDTO.getContacto());
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor updateProveedor(Long id, ProveedorRequestDTO proveedorRequestDTO) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun proveedor con id " + id));

        proveedor.setNombre(proveedorRequestDTO.getNombre());
        proveedor.setContacto(proveedorRequestDTO.getContacto());
        return proveedorRepository.save(proveedor);
    }

    @Override
    public void deleteProveedor(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun proveedor con id " + id));

        if (productoRepository.existsByProveedorId(id)) {
            throw new EntidadEnUsoException(
                    "No se puede eliminar el proveedor " + id + " porque tiene productos asociados");
        }

        proveedorRepository.delete(proveedor);
    }
}
