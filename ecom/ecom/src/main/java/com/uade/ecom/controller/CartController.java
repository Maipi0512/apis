package com.uade.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.model.Cart;
import com.uade.ecom.service.CartService;

@RestController
@RequestMapping("/carritos")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/{id}")
    public Cart getCartById(@PathVariable Long id) {
        return cartService.getCartById(id);
    }

    /**
     * Por ahora un carrito se crea "vacio" (sin usuario todavia). Cuando
     * tengan login, este metodo va a tomar el usuario autenticado.
     */
    @PostMapping
    public Cart createCart() {
        return cartService.createCart();
    }
}
