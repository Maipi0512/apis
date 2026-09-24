package com.uade.ecom.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import com.uade.ecom.dto.PedidoResponseDTO;
import com.uade.ecom.dto.PedidoUpdateDTO;
import com.uade.ecom.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public List<PedidoResponseDTO> getAllPedidos() {
        return pedidoService.getAllPedidos().stream()
                .map(PedidoResponseDTO::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public PedidoResponseDTO getPedidoById(@PathVariable Long id) {
        return PedidoResponseDTO.from(pedidoService.getPedidoById(id));
    }

    /**
     * El pedido se crea vacio (estado PENDIENTE, total 0) para el usuario
     * autenticado; se le van agregando DetallePedido despues.
     */
    @PostMapping
    public PedidoResponseDTO createPedido() {
        return PedidoResponseDTO.from(pedidoService.createPedido());
    }

    @PutMapping("/{id}")
    public PedidoResponseDTO updatePedido(@PathVariable Long id, @RequestBody PedidoUpdateDTO pedidoUpdateDTO) {
        return PedidoResponseDTO.from(pedidoService.updatePedido(id, pedidoUpdateDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePedido(@PathVariable Long id) {
        pedidoService.deletePedido(id);
    }

    /**
     * Reporte tipo "factura" del pedido: junta el pedido, sus items
     * (DetallePedido) y sus pagos en una sola respuesta. El precio de
     * cada item y la fecha del pedido los carga el sistema siempre (ver
     * DetallePedidoRequestDTO y PedidoServiceImpl.createPedido), nunca
     * vienen del body de un request.
     */
    @GetMapping("/{id}/factura")
    public FacturaDTO getFactura(@PathVariable Long id) {
        return pedidoService.getFactura(id);
    }
}
