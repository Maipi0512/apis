package com.uade.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.ItemCarritoRequestDTO;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.Carrito;
import com.uade.ecom.model.ItemCarrito;
import com.uade.ecom.model.Producto;
import com.uade.ecom.repository.CarritoRepository;
import com.uade.ecom.repository.ItemCarritoRepository;
import com.uade.ecom.repository.ProductoRepository;

@Service
public class ItemCarritoServiceImpl implements ItemCarritoService {

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<ItemCarrito> getAllItemsCarrito() {
        return itemCarritoRepository.findAll();
    }

    @Override
    public ItemCarrito getItemCarritoById(Long id) {
        return itemCarritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun item de carrito con id " + id));
    }

    @Override
    public ItemCarrito createItemCarrito(ItemCarritoRequestDTO itemCarritoRequestDTO) {
        Carrito carrito = carritoRepository.findById(itemCarritoRequestDTO.getCarritoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun carrito con id " + itemCarritoRequestDTO.getCarritoId()));

        Producto producto = productoRepository.findById(itemCarritoRequestDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun producto con id " + itemCarritoRequestDTO.getProductoId()));

        ItemCarrito itemCarrito = new ItemCarrito();
        itemCarrito.setCarrito(carrito);
        itemCarrito.setProducto(producto);
        itemCarrito.setCantidad(itemCarritoRequestDTO.getCantidad());

        return itemCarritoRepository.save(itemCarrito);
    }

    @Override
    public ItemCarrito updateItemCarrito(Long id, ItemCarritoRequestDTO itemCarritoRequestDTO) {
        ItemCarrito itemCarrito = itemCarritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun item de carrito con id " + id));

        Carrito carrito = carritoRepository.findById(itemCarritoRequestDTO.getCarritoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun carrito con id " + itemCarritoRequestDTO.getCarritoId()));

        Producto producto = productoRepository.findById(itemCarritoRequestDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun producto con id " + itemCarritoRequestDTO.getProductoId()));

        itemCarrito.setCarrito(carrito);
        itemCarrito.setProducto(producto);
        itemCarrito.setCantidad(itemCarritoRequestDTO.getCantidad());

        return itemCarritoRepository.save(itemCarrito);
    }

    @Override
    public void deleteItemCarrito(Long id) {
        ItemCarrito itemCarrito = itemCarritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun item de carrito con id " + id));
        itemCarritoRepository.delete(itemCarrito);
    }
}
