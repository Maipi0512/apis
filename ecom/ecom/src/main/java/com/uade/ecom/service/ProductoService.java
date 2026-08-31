package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.ProductoRequestDTO;
import com.uade.ecom.model.Producto;

public interface ProductoService {

    List<Producto> getAllProductos();

    Producto getProductoById(Long id);

    Producto createProducto(ProductoRequestDTO productoRequestDTO);

    Producto updateProducto(Long id, ProductoRequestDTO productoRequestDTO);

    void deleteProducto(Long id);
}
