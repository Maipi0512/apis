package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.FacturaDTO;
import com.uade.ecom.dto.PedidoUpdateDTO;
import com.uade.ecom.model.Pedido;

public interface PedidoService {

    List<Pedido> getAllPedidos();

    Pedido getPedidoById(Long id);

    Pedido createPedido();

    Pedido updatePedido(Long id, PedidoUpdateDTO pedidoUpdateDTO);

    void deletePedido(Long id);

    /**
     * Reporte tipo "factura" de un pedido: el pedido, sus items
     * (DetallePedido) y sus pagos, con los totales ya calculados.
     */
    FacturaDTO getFactura(Long id);
}
