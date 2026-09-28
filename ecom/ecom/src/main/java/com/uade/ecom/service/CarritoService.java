package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.model.ItemCarrito;
import com.uade.ecom.model.Pedido;

/**
 * El cliente siempre opera sobre SU carrito (el que se le crea al
 * registrarse, ver AutenticacionServiceImpl.registrar()), nunca elige
 * uno por id.
 */
public interface CarritoService {

    List<ItemCarrito> getItemsDeMiCarrito();

    void vaciarMiCarrito();

    /**
     * Convierte el carrito del usuario autenticado en un Pedido PAGADO
     * (con sus DetallePedido), descuenta el stock y vacia el carrito.
     * No se expone como endpoint: lo llama PagoServiceImpl, porque el
     * pedido recien existe una vez que se paga.
     */
    Pedido crearPedidoPagado();
}
