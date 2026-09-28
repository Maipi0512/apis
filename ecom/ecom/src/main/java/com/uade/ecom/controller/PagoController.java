package com.uade.ecom.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.dto.PagoRequestDTO;
import com.uade.ecom.dto.PagoResponseDTO;
import com.uade.ecom.model.MetodoPago;
import com.uade.ecom.service.PagoService;

@RestController
@RequestMapping("/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public List<PagoResponseDTO> getAllPagos() {
        return pagoService.getAllPagos().stream()
                .map(PagoResponseDTO::from)
                .collect(Collectors.toList());
    }

    /**
     * Opciones para el select de "metodo de pago" del front.
     */
    @GetMapping("/metodos")
    public MetodoPago[] getMetodosPago() {
        return MetodoPago.values();
    }

    @GetMapping("/{id}")
    public PagoResponseDTO getPagoById(@PathVariable Long id) {
        return PagoResponseDTO.from(pagoService.getPagoById(id));
    }

    /**
     * Paga el carrito del usuario autenticado. Body: { "metodoPago": "..." }.
     * El monto es el total del carrito y el pedido se crea en este momento.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PagoResponseDTO pagar(@RequestBody PagoRequestDTO pagoRequestDTO) {
        return PagoResponseDTO.from(pagoService.pagar(pagoRequestDTO));
    }

    // No hay PUT ni DELETE: un pago ya registrado no se modifica. Para
    // devolver la compra se cancela el pedido (PUT /pedidos/{id}, ADMIN),
    // que ademas repone el stock.
}
