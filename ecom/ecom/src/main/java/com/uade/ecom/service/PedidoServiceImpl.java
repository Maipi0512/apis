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
import com.uade.ecom.exception.AccesoDenegadoException;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.DetallePedido;
import com.uade.ecom.model.Pedido;
import com.uade.ecom.model.Usuario;
import com.uade.ecom.repository.DetallePedidoRepository;
import com.uade.ecom.repository.PagoRepository;
import com.uade.ecom.repository.PedidoRepository;
import com.uade.ecom.util.SecurityUtils;

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
        if (SecurityUtils.esAdmin()) {
            return pedidoRepository.findAll();
        }
        return pedidoRepository.findByUsuarioId(SecurityUtils.getUsuarioActual().getId());
    }

    @Override
    public Pedido getPedidoById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pedido con id " + id));
        validarDueño(pedido);
        return pedido;
    }

    @Override
    public Pedido createPedido() {
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDate.now());
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(BigDecimal.ZERO);
        pedido.setUsuario(SecurityUtils.getUsuarioActual());

        return pedidoRepository.save(pedido);
    }

    /**
     * Cambiar el estado de un pedido (ej. a "ENVIADO") es una accion
     * administrativa; el SecurityConfig ya restringe PUT /pedidos/** a
     * ADMIN, asi que aca no hace falta validarDueño de nuevo.
     */
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
        validarDueño(pedido);
        pedidoRepository.delete(pedido);
    }

    /**
     * Un CLIENTE solo puede ver/tocar sus propios pedidos; un ADMIN
     * puede con cualquiera.
     */
    private void validarDueño(Pedido pedido) {
        if (SecurityUtils.esAdmin()) {
            return;
        }
        Usuario actual = SecurityUtils.getUsuarioActual();
        Usuario dueño = pedido.getUsuario();
        if (dueño == null || !dueño.getId().equals(actual.getId())) {
            throw new AccesoDenegadoException("El pedido " + pedido.getId() + " no pertenece al usuario autenticado");
        }
    }

    @Override
    public FacturaDTO getFactura(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pedido con id " + id));
        validarDueño(pedido);

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
