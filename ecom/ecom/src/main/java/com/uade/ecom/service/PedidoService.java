package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.PedidoResponseDTO;
import com.uade.ecom.dto.PedidoUpdateDTO;

/**
 * Los pedidos no se crean por aca: nacen al pagar el carrito (ver
 * PagoServiceImpl.pagar()). Cada pedido se devuelve con sus items y su
 * pago, como una factura.
 */
public interface PedidoService {

    /**
     * "Mis compras" para un CLIENTE; todos los pedidos para un ADMIN.
     */
    List<PedidoResponseDTO> getAllPedidos();

    PedidoResponseDTO getPedidoById(Long id);

    PedidoResponseDTO updatePedido(Long id, PedidoUpdateDTO pedidoUpdateDTO);

    /**
     * El CLIENTE cancela su propia compra, mientras no se haya enviado.
     */
    PedidoResponseDTO cancelarPedido(Long id);
}
