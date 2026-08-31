package com.uade.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.model.Carrito;
import com.uade.ecom.model.Pedido;
import com.uade.ecom.service.CarritoService;

@RestController
@RequestMapping("/carritos")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping
    public List<Carrito> getAllCarritos() {
        return carritoService.getAllCarritos();
    }

    @GetMapping("/{id}")
    public Carrito getCarritoById(@PathVariable Long id) {
        return carritoService.getCarritoById(id);
    }

    /**
     * Por ahora un carrito se crea "vacio" (sin usuario todavia). Cuando
     * tengan login, este metodo va a tomar el usuario autenticado.
     */
    @PostMapping
    public Carrito createCarrito() {
        return carritoService.createCarrito();
    }

    // No hay PUT: Carrito no tiene mas campos propios que el id (todavia
    // no esta asociado a un Usuario), asi que no hay nada que modificar.

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
    public Pedido checkout(@PathVariable Long id) {
        return carritoService.checkout(id);
    }
}
