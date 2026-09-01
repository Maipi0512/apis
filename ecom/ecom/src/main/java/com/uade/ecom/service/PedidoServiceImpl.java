package com.uade.ecom.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.FacturaDTO;
import com.uade.ecom.dto.ItemFacturaDTO;
import com.uade.ecom.dto.PagoFacturaDTO;
import com.uade.ecom.dto.PedidoUpdateDTO;
import com.uade.ecom.exception.PedidoVacioException;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.exception.TransicionEstadoInvalidaException;
import com.uade.ecom.model.DetallePedido;
import com.uade.ecom.model.Pedido;
import com.uade.ecom.model.Producto;
import com.uade.ecom.repository.DetallePedidoRepository;
import com.uade.ecom.repository.PagoRepository;
import com.uade.ecom.repository.PedidoRepository;
import com.uade.ecom.repository.ProductoRepository;

@Service
public class PedidoServiceImpl implements PedidoService {

    private static final String ESTADO_PAGADO = "PAGADO";
    private static final String ESTADO_CANCELADO = "CANCELADO";

    /**
     * Transiciones de estado permitidas. Un pedido nuevo arranca en
     * PENDIENTE (ver createPedido) y de ahi solo puede avanzar por estos
     * caminos -- ENTREGADO y CANCELADO son estados finales.
     */
    private static final Map<String, Set<String>> TRANSICIONES_VALIDAS = Map.of(
            "PENDIENTE", Set.of(ESTADO_PAGADO, ESTADO_CANCELADO),
            ESTADO_PAGADO, Set.of("ENVIADO", ESTADO_CANCELADO),
            "ENVIADO", Set.of("ENTREGADO"),
            "ENTREGADO", Set.of(),
            ESTADO_CANCELADO, Set.of());

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private ProductoRepository productoRepository;

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

        String estadoActual = pedido.getEstado();
        String estadoNuevo = pedidoUpdateDTO.getEstado();

        if (!esTransicionValida(estadoActual, estadoNuevo)) {
            throw new TransicionEstadoInvalidaException(
                    "El pedido " + id + " no puede pasar de " + estadoActual + " a " + estadoNuevo);
        }

        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(id);

        if (ESTADO_PAGADO.equals(estadoNuevo) && detalles.isEmpty()) {
            throw new PedidoVacioException(
                    "No se puede marcar como PAGADO el pedido " + id + " porque no tiene ningun item cargado");
        }

        if (ESTADO_CANCELADO.equals(estadoNuevo)) {
            restaurarStock(detalles);
        }

        pedido.setEstado(estadoNuevo);
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

    private boolean esTransicionValida(String estadoActual, String estadoNuevo) {
        if (estadoNuevo == null) {
            return false;
        }
        return TRANSICIONES_VALIDAS.getOrDefault(estadoActual, Set.of()).contains(estadoNuevo);
    }

    /**
     * Al cancelar un pedido, el stock que el checkout le habia
     * descontado a cada producto tiene que volver -- si no, el
     * inventario real queda mas bajo que el disponible de verdad.
     */
    private void restaurarStock(List<DetallePedido> detalles) {
        for (DetallePedido detalle : detalles) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }
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
