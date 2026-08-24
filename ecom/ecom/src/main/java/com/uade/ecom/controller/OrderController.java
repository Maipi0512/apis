package com.uade.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.model.Order;
import com.uade.ecom.service.OrderService;

@RestController
@RequestMapping("/pedidos")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    /**
     * Por ahora un pedido se crea "vacio" (sin usuario todavia). Cuando
     * tengan login, este metodo va a tomar el usuario autenticado en vez
     * de no recibir nada.
     */
    @PostMapping
    public Order createOrder() {
        return orderService.createOrder();
    }
}
