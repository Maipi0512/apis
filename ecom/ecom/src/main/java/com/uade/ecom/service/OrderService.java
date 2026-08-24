package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.model.Order;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderById(Long id);

    Order createOrder();
}
