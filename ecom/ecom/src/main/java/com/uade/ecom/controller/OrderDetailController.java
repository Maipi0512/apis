package com.uade.ecom.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.ecom.dto.OrderDetailRequestDTO;
import com.uade.ecom.model.OrderDetail;
import com.uade.ecom.service.OrderDetailService;

@RestController
@RequestMapping("/detalles-pedido")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailService.getAllOrderDetails();
    }

    @GetMapping("/{id}")
    public OrderDetail getOrderDetailById(@PathVariable Long id) {
        return orderDetailService.getOrderDetailById(id);
    }

    @PostMapping
    public OrderDetail createOrderDetail(@RequestBody OrderDetailRequestDTO orderDetailRequestDTO) {
        return orderDetailService.createOrderDetail(orderDetailRequestDTO);
    }
}
