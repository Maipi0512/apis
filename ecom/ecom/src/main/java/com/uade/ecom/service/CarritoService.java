package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.model.Carrito;
import com.uade.ecom.model.ItemCarrito;
import com.uade.ecom.model.Pedido;

public interface CarritoService {

    List<Carrito> getAllCarritos();

    Carrito getCarritoById(Long id);

    /**
     * Items de un carrito. Asume que el carrito ya se valido con
     * getCarritoById (el dueño ya tiene que estar confirmado antes de
     * llamar esto).
     */
    List<ItemCarrito> getItemsDeCarrito(Long carritoId);

    Carrito createCarrito();

    void deleteCarrito(Long id);

    /**
     * Confirma la compra: convierte los ItemCarrito en un Pedido con sus
     * DetallePedido, descuenta el stock de cada Producto y vacia el
     * carrito.
     */
    Pedido checkout(Long carritoId);
}
