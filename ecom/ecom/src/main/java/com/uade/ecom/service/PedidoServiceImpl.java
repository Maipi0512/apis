package com.uade.ecom.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.FacturaDTO;
import com.uade.ecom.dto.ItemFacturaDTO;
import com.uade.ecom.dto.PagoFacturaDTO;
import com.uade.ecom.dto.PedidoUpdateDTO;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.DetallePedido;
import com.uade.ecom.model.Pedido;
import com.uade.ecom.repository.DetallePedidoRepository;
import com.uade.ecom.repository.PagoRepository;
import com.uade.ecom.repository.PedidoRepository;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Override
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    @Override
    public Pedido getPedidoById(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pedido con id " + id));
    }

    @Override
    public Pedido createPedido() {
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDate.now());
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(BigDecimal.ZERO);

        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido updatePedido(Long id, PedidoUpdateDTO pedidoUpdateDTO) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pedido con id " + id));

        pedido.setEstado(pedidoUpdateDTO.getEstado());
        return pedidoRepository.save(pedido);
    }

    @Override
    public void deletePedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pedido con id " + id));
        pedidoRepository.delete(pedido);
    }

    @Override
    public FacturaDTO getFactura(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pedido con id " + id));

        List<ItemFacturaDTO> items = detallePedidoRepository.findByPedidoId(id).stream()
                .map(this::toItemFactura)
                .collect(Collectors.toList());

        List<PagoFacturaDTO> pagos = pagoRepository.findByPedidoId(id).stream()
                .map(pago -> new PagoFacturaDTO(pago.getMetodoPago(), pago.getMonto()))
                .collect(Collectors.toList());

        BigDecimal totalPagado = pagos.stream()
                .map(PagoFacturaDTO::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        FacturaDTO factura = new FacturaDTO();
        factura.setNumeroPedido(pedido.getId());
        factura.setFecha(pedido.getFecha());
        factura.setEstado(pedido.getEstado());
        factura.setItems(items);
        factura.setTotal(pedido.getTotal());
        factura.setPagos(pagos);
        factura.setTotalPagado(totalPagado);
        factura.setSaldoPendiente(pedido.getTotal().subtract(totalPagado));
        return factura;
    }

    private ItemFacturaDTO toItemFactura(DetallePedido detalle) {
        BigDecimal subtotal = detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()));
        return new ItemFacturaDTO(
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario(),
                subtotal);
    }
}
