package com.uade.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.dto.PedidoResponseDTO;
import com.uade.ecom.dto.PedidoUpdateDTO;
import com.uade.ecom.service.PedidoService;

/**
 * "Mis compras". Cada pedido viene con sus items (DetallePedido), el
 * metodo de pago y el total: es la factura de la compra, no hace falta
 * pedir los items aparte. No hay POST: el pedido se crea solo cuando se
 * paga el carrito (POST /pagos), igual que en Mercado Pago.
 */
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<PedidoResponseDTO> getAllPedidos() {
        return pedidoService.getAllPedidos();
    }

    @GetMapping("/{id}")
    public PedidoResponseDTO getPedidoById(@PathVariable Long id) {
        return pedidoService.getPedidoById(id);
    }

    @PutMapping("/{id}")
    public PedidoResponseDTO updatePedido(@PathVariable Long id, @RequestBody PedidoUpdateDTO pedidoUpdateDTO) {
        return pedidoService.updatePedido(id, pedidoUpdateDTO);
    }

    /**
     * El cliente cancela su propia compra mientras no se haya enviado
     * (el stock vuelve). No lleva body. El PUT de arriba, que cambia a
     * cualquier estado, sigue siendo solo para ADMIN.
     */
    @PutMapping("/{id}/cancelar")
    public PedidoResponseDTO cancelarPedido(@PathVariable Long id) {
        return pedidoService.cancelarPedido(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePedido(@PathVariable Long id) {
        pedidoService.deletePedido(id);
    }
}
