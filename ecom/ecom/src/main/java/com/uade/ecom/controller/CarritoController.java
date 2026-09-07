package com.uade.ecom.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.dto.CarritoResponseDTO;
import com.uade.ecom.dto.PedidoResponseDTO;
import com.uade.ecom.service.CarritoService;

@RestController
@RequestMapping("/carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public List<CarritoResponseDTO> getAllCarritos() {
        return carritoService.getAllCarritos().stream()
                .map(CarritoResponseDTO::from)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CarritoResponseDTO getCarritoById(@PathVariable Long id) {
        return CarritoResponseDTO.from(carritoService.getCarritoById(id));
    }

    /**
     * En general no hace falta llamar esto: todo CLIENTE ya se registra
     * con su carrito creado (ver AutenticacionServiceImpl.registrar()).
     * Se deja disponible por si un ADMIN necesita armar uno a mano.
     */
    @PostMapping
    public CarritoResponseDTO createCarrito() {
        return CarritoResponseDTO.from(carritoService.createCarrito());
    }

    // No hay PUT: Carrito no tiene mas campos propios que el id y el
    // dueño, asi que no hay nada que modificar.

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCarrito(@PathVariable Long id) {
        carritoService.deleteCarrito(id);
    }

    /**
     * Confirma la compra: convierte el carrito en un Pedido (con sus
     * DetallePedido), descuenta el stock y vacia el carrito.
     */
    @PostMapping("/{id}/checkout")
    public PedidoResponseDTO checkout(@PathVariable Long id) {
        return PedidoResponseDTO.from(carritoService.checkout(id));
    }
}
