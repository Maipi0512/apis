package com.uade.ecom.service;

import java.util.List;

import com.uade.ecom.dto.DetallePedidoRequestDTO;
import com.uade.ecom.model.DetallePedido;

public interface DetallePedidoService {

    List<DetallePedido> getAllDetallesPedido();

    DetallePedido getDetallePedidoById(Long id);

    DetallePedido createDetallePedido(DetallePedidoRequestDTO detallePedidoRequestDTO);

    DetallePedido updateDetallePedido(Long id, DetallePedidoRequestDTO detallePedidoRequestDTO);

    void deleteDetallePedido(Long id);
}
