package com.uade.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uade.ecom.dto.ProductoRequestDTO;
import com.uade.ecom.model.Producto;
import com.uade.ecom.service.ProductoService;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.getAllProductos();
    }

    @GetMapping("/{id}")
    public Producto getProductoById(@PathVariable Long id) {
        return productoService.getProductoById(id);
    }

    @PostMapping
    public Producto createProducto(@RequestBody ProductoRequestDTO productoRequestDTO) {
        return productoService.createProducto(productoRequestDTO);
    }

    @PutMapping("/{id}")
    public Producto updateProducto(@PathVariable Long id, @RequestBody ProductoRequestDTO productoRequestDTO) {
        return productoService.updateProducto(id, productoRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProducto(@PathVariable Long id) {
        productoService.deleteProducto(id);
    }

    /**
     * Sube (o reemplaza) la imagen de un producto. Se manda como
     * multipart/form-data con un campo "file" -- por ejemplo, en
     * Insomnia/Postman: Body -> Multipart Form -> agregar campo "file"
     * de tipo File y elegir la imagen.
     *
     * Requiere ADMIN, igual que crear/editar/borrar el producto (ver
     * SeguridadConfig).
     */
    @PostMapping("/{id}/imagen")
    public Producto subirImagenProducto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return productoService.actualizarImagen(id, file);
    }

    /**
     * Devuelve los bytes de la imagen del producto con su Content-Type
     * real (image/jpeg, image/png, etc.), lista para usarse directo como
     * <img src="http://localhost:4002/productos/{id}/imagen">.
     */
    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> getImagenProducto(@PathVariable Long id) {
        Producto producto = productoService.getImagenProducto(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(producto.getImagenContentType()))
                .body(producto.getImagen());
    }
}
