package com.uade.ecom.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uade.ecom.dto.ProductoRequestDTO;
import com.uade.ecom.exception.DatoInvalidoException;
import com.uade.ecom.exception.DescuentoInvalidoException;
import com.uade.ecom.exception.EntidadEnUsoException;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.Categoria;
import com.uade.ecom.model.Producto;
import com.uade.ecom.model.Proveedor;
import com.uade.ecom.repository.CategoriaRepository;
import com.uade.ecom.repository.DetallePedidoRepository;
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

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

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
        validarDatosProducto(productoRequestDTO);

        Categoria categoria = categoriaRepository.findById(productoRequestDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ninguna categoria con id " + productoRequestDTO.getCategoriaId()));

        Producto producto = new Producto();
        producto.setNombre(productoRequestDTO.getNombre());
        producto.setPrecio(productoRequestDTO.getPrecio());
        producto.setStock(productoRequestDTO.getStock());
        producto.setCategoria(categoria);
        producto.setDescuentoPorcentaje(validarDescuento(productoRequestDTO.getDescuentoPorcentaje()));

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
        validarDatosProducto(productoRequestDTO);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun producto con id " + id));

        Categoria categoria = categoriaRepository.findById(productoRequestDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ninguna categoria con id " + productoRequestDTO.getCategoriaId()));

        producto.setNombre(productoRequestDTO.getNombre());
        producto.setPrecio(productoRequestDTO.getPrecio());
        producto.setStock(productoRequestDTO.getStock());
        producto.setCategoria(categoria);
        producto.setDescuentoPorcentaje(validarDescuento(productoRequestDTO.getDescuentoPorcentaje()));

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

    /**
     * Si no mandan descuentoPorcentaje, el producto queda sin descuento (0).
     * Si lo mandan, tiene que estar entre 0 y 100.
     */
    private BigDecimal validarDescuento(BigDecimal descuentoPorcentaje) {
        if (descuentoPorcentaje == null) {
            return BigDecimal.ZERO;
        }
        if (descuentoPorcentaje.compareTo(BigDecimal.ZERO) < 0
                || descuentoPorcentaje.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new DescuentoInvalidoException(
                    "El descuentoPorcentaje debe estar entre 0 y 100, se recibio " + descuentoPorcentaje);
        }
        return descuentoPorcentaje;
    }

    @Override
    public void deleteProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun producto con id " + id));

        if (detallePedidoRepository.existsByProductoId(id)) {
            throw new EntidadEnUsoException(
                    "No se puede eliminar el producto " + id + " porque tiene pedidos asociados");
        }

        productoRepository.delete(producto);
    }

    @Override
    public Producto actualizarImagen(Long id, MultipartFile file) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun producto con id " + id));

        if (file == null || file.isEmpty()) {
            throw new DatoInvalidoException("Hay que mandar un archivo de imagen (no puede venir vacio)");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new DatoInvalidoException(
                    "El archivo tiene que ser una imagen (jpg, png, etc.), se recibio " + contentType);
        }

        try {
            producto.setImagen(file.getBytes());
            producto.setImagenContentType(contentType);
        } catch (IOException e) {
            throw new DatoInvalidoException("No se pudo leer el archivo de imagen enviado");
        }

        return productoRepository.save(producto);
    }

    @Override
    public Producto getImagenProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun producto con id " + id));

        if (producto.getImagen() == null || producto.getImagen().length == 0) {
            throw new ResourceNotFoundException("El producto " + id + " todavia no tiene una imagen cargada");
        }

        return producto;
    }

    private void validarDatosProducto(ProductoRequestDTO dto) {
        if (dto.getPrecio() == null || dto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DatoInvalidoException("El precio del producto tiene que ser mayor a 0");
        }
        if (dto.getStock() == null || dto.getStock() < 0) {
            throw new DatoInvalidoException("El stock del producto no puede ser negativo");
        }
    }
}
