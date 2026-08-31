package com.uade.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.ProductoRequestDTO;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.Categoria;
import com.uade.ecom.model.Producto;
import com.uade.ecom.model.Proveedor;
import com.uade.ecom.repository.CategoriaRepository;
import com.uade.ecom.repository.ProductoRepository;
import com.uade.ecom.repository.ProveedorRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun producto con id " + id));
    }

    @Override
    public Producto createProducto(ProductoRequestDTO productoRequestDTO) {
        Categoria categoria = categoriaRepository.findById(productoRequestDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ninguna categoria con id " + productoRequestDTO.getCategoriaId()));

        Producto producto = new Producto();
        producto.setNombre(productoRequestDTO.getNombre());
        producto.setPrecio(productoRequestDTO.getPrecio());
        producto.setStock(productoRequestDTO.getStock());
        producto.setCategoria(categoria);

        if (productoRequestDTO.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepository.findById(productoRequestDTO.getProveedorId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No se encontro ningun proveedor con id " + productoRequestDTO.getProveedorId()));
            producto.setProveedor(proveedor);
        }

        return productoRepository.save(producto);
    }

    @Override
    public Producto updateProducto(Long id, ProductoRequestDTO productoRequestDTO) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun producto con id " + id));

        Categoria categoria = categoriaRepository.findById(productoRequestDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ninguna categoria con id " + productoRequestDTO.getCategoriaId()));

        producto.setNombre(productoRequestDTO.getNombre());
        producto.setPrecio(productoRequestDTO.getPrecio());
        producto.setStock(productoRequestDTO.getStock());
        producto.setCategoria(categoria);

        if (productoRequestDTO.getProveedorId() != null) {
            Proveedor proveedor = proveedorRepository.findById(productoRequestDTO.getProveedorId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "No se encontro ningun proveedor con id " + productoRequestDTO.getProveedorId()));
            producto.setProveedor(proveedor);
        } else {
            producto.setProveedor(null);
        }

        return productoRepository.save(producto);
    }

    @Override
    public void deleteProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun producto con id " + id));
        productoRepository.delete(producto);
    }
}
