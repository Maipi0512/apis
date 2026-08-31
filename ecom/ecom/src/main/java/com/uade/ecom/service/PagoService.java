package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.PagoRequestDTO;
import com.uade.ecom.model.Pago;

public interface PagoService {

    List<Pago> getAllPagos();

    Pago getPagoById(Long id);

    Pago createPago(PagoRequestDTO pagoRequestDTO);

    Pago updatePago(Long id, PagoRequestDTO pagoRequestDTO);

    void deletePago(Long id);
}
