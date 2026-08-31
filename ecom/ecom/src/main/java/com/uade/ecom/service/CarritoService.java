package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.model.Carrito;
import com.uade.ecom.model.Pedido;

public interface CarritoService {

    List<Carrito> getAllCarritos();

    Carrito getCarritoById(Long id);

    Carrito createCarrito();

    void deleteCarrito(Long id);

    /**
     * Confirma la compra: convierte los ItemCarrito en un Pedido con sus
     * DetallePedido, descuenta el stock de cada Producto y vacia el
     * carrito.
     */
    Pedido checkout(Long carritoId);
}
