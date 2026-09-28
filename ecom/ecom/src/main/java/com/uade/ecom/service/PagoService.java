package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.PagoRequestDTO;
import com.uade.ecom.model.Pago;

public interface PagoService {

    List<Pago> getAllPagos();

    Pago getPagoById(Long id);

    /**
     * Paga el carrito del usuario autenticado: crea el Pedido (ya PAGADO)
     * y registra el Pago por el total del carrito.
     */
    Pago pagar(PagoRequestDTO pagoRequestDTO);
}
