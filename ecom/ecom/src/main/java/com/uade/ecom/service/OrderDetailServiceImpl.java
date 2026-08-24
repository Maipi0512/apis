package com.uade.ecom.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.OrderDetailRequestDTO;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.Order;
import com.uade.ecom.model.OrderDetail;
import com.uade.ecom.model.Product;
import com.uade.ecom.repository.OrderDetailRepository;
import com.uade.ecom.repository.OrderRepository;
import com.uade.ecom.repository.ProductRepository;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    @Override
    public OrderDetail getOrderDetailById(Long id) {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun detalle con id " + id));
    }

    @Override
    public OrderDetail createOrderDetail(OrderDetailRequestDTO orderDetailRequestDTO) {
        Order order = orderRepository.findById(orderDetailRequestDTO.getPedidoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun pedido con id " + orderDetailRequestDTO.getPedidoId()));

        Product product = productRepository.findById(orderDetailRequestDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun producto con id " + orderDetailRequestDTO.getProductoId()));

        OrderDetail detail = new OrderDetail();
        detail.setOrder(order);
        detail.setProduct(product);
        detail.setCantidad(orderDetailRequestDTO.getCantidad());
        // El precio unitario se toma del precio actual del producto, no
        // del usuario, para que no lo pueda manipular desde el body.
        detail.setPrecioUnitario(product.getPrecio());

        OrderDetail detalleGuardado = orderDetailRepository.save(detail);

        // Actualizamos el total del pedido sumando esta linea nueva.
        BigDecimal subtotal = product.getPrecio()
                .multiply(BigDecimal.valueOf(orderDetailRequestDTO.getCantidad()));
        order.setTotal(order.getTotal().add(subtotal));
        orderRepository.save(order);

        return detalleGuardado;
    }
}
