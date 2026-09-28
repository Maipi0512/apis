package com.uade.ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.dto.CarritoResponseDTO;
import com.uade.ecom.service.CarritoService;

/**
 * Siempre sobre el carrito del usuario autenticado, sin ids en la URL:
 * cada CLIENTE tiene un unico carrito, creado al registrarse. Los items
 * se agregan/modifican por /carritos/items y la compra se confirma pagando
 * en POST /pagos (ahi se crea el pedido).
 */
@RestController
@RequestMapping("/carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public CarritoResponseDTO getMiCarrito() {
        return CarritoResponseDTO.from(carritoService.getItemsDeMiCarrito());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vaciarMiCarrito() {
        carritoService.vaciarMiCarrito();
    }
}
