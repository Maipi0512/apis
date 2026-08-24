package com.uade.ecom.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.Order;
import com.uade.ecom.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pedido con id " + id));
    }

    @Override
    public Order createOrder() {
        Order order = new Order();
        order.setFecha(LocalDate.now());
        order.setEstado("PENDIENTE");
        order.setTotal(BigDecimal.ZERO);

        return orderRepository.save(order);
    }
}
