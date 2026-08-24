package com.uade.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.PaymentRequestDTO;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.Order;
import com.uade.ecom.model.Payment;
import com.uade.ecom.repository.OrderRepository;
import com.uade.ecom.repository.PaymentRepository;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pago con id " + id));
    }

    @Override
    public Payment createPayment(PaymentRequestDTO paymentRequestDTO) {
        Order order = orderRepository.findById(paymentRequestDTO.getPedidoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun pedido con id " + paymentRequestDTO.getPedidoId()));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMetodoPago(paymentRequestDTO.getMetodoPago());
        payment.setMonto(paymentRequestDTO.getMonto());

        return paymentRepository.save(payment);
    }
}
