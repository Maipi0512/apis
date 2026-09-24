package com.uade.ecom.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.uade.ecom.dto.ProductoRequestDTO;
import com.uade.ecom.model.Producto;

public interface ProductoService {

    List<Producto> getAllProductos();

    Producto getProductoById(Long id);

    Producto createProducto(ProductoRequestDTO productoRequestDTO);

    Producto updateProducto(Long id, ProductoRequestDTO productoRequestDTO);

    void deleteProducto(Long id);

    /**
     * Guarda (o reemplaza) la imagen del producto. Devuelve el producto
     * actualizado.
     */
    Producto actualizarImagen(Long id, MultipartFile file);

    /**
     * Devuelve el producto con su imagen cargada, para que el controller
     * pueda leer los bytes y el content type y armar la respuesta.
     * Tira ResourceNotFoundException si el producto no existe o si
     * todavia no tiene ninguna imagen cargada.
     */
    Producto getImagenProducto(Long id);
}
