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

import com.uade.ecom.dto.DetallePedidoRequestDTO;
import com.uade.ecom.model.DetallePedido;
import com.uade.ecom.service.DetallePedidoService;

@RestController
@RequestMapping("/detallesPedido")
public class DetallePedidoController {

    @Autowired
    private DetallePedidoService detallePedidoService;

    @GetMapping
    public List<DetallePedido> getAllDetallesPedido() {
        return detallePedidoService.getAllDetallesPedido();
    }

    @GetMapping("/{id}")
    public DetallePedido getDetallePedidoById(@PathVariable Long id) {
        return detallePedidoService.getDetallePedidoById(id);
    }

    @PostMapping
    public DetallePedido createDetallePedido(@RequestBody DetallePedidoRequestDTO detallePedidoRequestDTO) {
        return detallePedidoService.createDetallePedido(detallePedidoRequestDTO);
    }

    @PutMapping("/{id}")
    public DetallePedido updateDetallePedido(@PathVariable Long id,
            @RequestBody DetallePedidoRequestDTO detallePedidoRequestDTO) {
        return detallePedidoService.updateDetallePedido(id, detallePedidoRequestDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDetallePedido(@PathVariable Long id) {
        detallePedidoService.deleteDetallePedido(id);
    }
}
