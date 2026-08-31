package com.uade.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.dto.FacturaDTO;
import com.uade.ecom.dto.PedidoUpdateDTO;
import com.uade.ecom.model.Pedido;
import com.uade.ecom.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<Pedido> getAllPedidos() {
        return pedidoService.getAllPedidos();
    }

    @GetMapping("/{id}")
    public Pedido getPedidoById(@PathVariable Long id) {
        return pedidoService.getPedidoById(id);
    }

    /**
     * Por ahora un pedido se crea "vacio" (sin usuario todavia). Cuando
     * tengan login, este metodo va a tomar el usuario autenticado en vez
     * de no recibir nada.
     */
    @PostMapping
    public Pedido createPedido() {
        return pedidoService.createPedido();
    }

    @PutMapping("/{id}")
    public Pedido updatePedido(@PathVariable Long id, @RequestBody PedidoUpdateDTO pedidoUpdateDTO) {
        return pedidoService.updatePedido(id, pedidoUpdateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePedido(@PathVariable Long id) {
        pedidoService.deletePedido(id);
    }

    /**
     * Reporte tipo "factura" del pedido: junta el pedido, sus items
     * (DetallePedido) y sus pagos en una sola respuesta.
     */
    @GetMapping("/{id}/factura")
    public FacturaDTO getFactura(@PathVariable Long id) {
        return pedidoService.getFactura(id);
    }
}
