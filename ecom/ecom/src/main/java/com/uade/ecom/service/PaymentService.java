package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.PaymentRequestDTO;
import com.uade.ecom.model.Payment;

public interface PaymentService {

    List<Payment> getAllPayments();

    Payment getPaymentById(Long id);

    Payment createPayment(PaymentRequestDTO paymentRequestDTO);
}
