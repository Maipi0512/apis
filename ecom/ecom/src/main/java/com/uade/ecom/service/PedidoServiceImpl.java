package com.uade.ecom.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.ecom.dto.ClientePedidoDTO;
import com.uade.ecom.dto.ItemFacturaDTO;
import com.uade.ecom.dto.PedidoResponseDTO;
import com.uade.ecom.dto.PedidoUpdateDTO;
import com.uade.ecom.exception.AccesoDenegadoException;
import com.uade.ecom.exception.PedidoVacioException;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.exception.TransicionEstadoInvalidaException;
import com.uade.ecom.model.DetallePedido;
import com.uade.ecom.model.Pago;
import com.uade.ecom.model.Pedido;
import com.uade.ecom.model.Producto;
import com.uade.ecom.model.Usuario;
import com.uade.ecom.repository.DetallePedidoRepository;
import com.uade.ecom.repository.PagoRepository;
import com.uade.ecom.repository.PedidoRepository;
import com.uade.ecom.repository.ProductoRepository;
import com.uade.ecom.util.SecurityUtils;

@Service
public class PedidoServiceImpl implements PedidoService {

    private static final String ESTADO_PAGADO = "PAGADO";
    private static final String ESTADO_CANCELADO = "CANCELADO";

    /**
     * Transiciones de estado permitidas. Un pedido nuevo nace PAGADO (se
     * crea al pagar, ver CarritoServiceImpl.crearPedidoPagado) y de ahi
     * solo puede avanzar por estos caminos -- ENTREGADO y CANCELADO son
     * estados finales. PENDIENTE queda por los pedidos viejos de la base.
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

    // readOnly: el producto trae la imagen como @Lob, y Postgres solo deja
    // leerla dentro de una transaccion (si no: "Unable to access lob stream")
    @Transactional(readOnly = true)
    @Override
    public List<PedidoResponseDTO> getAllPedidos() {
        List<Pedido> pedidos = SecurityUtils.esAdmin()
                ? pedidoRepository.findAll()
                : pedidoRepository.findByUsuario_Id(SecurityUtils.getUsuarioActual().getId());
        return pedidos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public PedidoResponseDTO getPedidoById(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pedido con id " + id));
        validarDueño(pedido);
        return toResponse(pedido);
    }

    /**
     * Cambiar el estado de un pedido (ej. a "ENVIADO") es una accion
     * administrativa; el SecurityConfig ya restringe PUT /pedidos/** a
     * ADMIN, asi que aca no hace falta validarDueño de nuevo.
     */
    @Override
    @Transactional
    public PedidoResponseDTO updatePedido(Long id, PedidoUpdateDTO pedidoUpdateDTO) {
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
        return toResponse(pedidoRepository.save(pedido));
    }

    /**
     * Cancelacion hecha por el comprador (o un ADMIN): solo su propio
     * pedido y solo mientras no se haya enviado (PENDIENTE o PAGADO, las
     * mismas transiciones que valen para el ADMIN). Repone el stock.
     */
    @Override
    @Transactional
    public PedidoResponseDTO cancelarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun pedido con id " + id));
        validarDueño(pedido);

        if (!esTransicionValida(pedido.getEstado(), ESTADO_CANCELADO)) {
            throw new TransicionEstadoInvalidaException(
                    "El pedido " + id + " esta " + pedido.getEstado() + " y ya no se puede cancelar");
        }

        restaurarStock(detallePedidoRepository.findByPedidoId(id));
        pedido.setEstado(ESTADO_CANCELADO);
        return toResponse(pedidoRepository.save(pedido));
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

    /**
     * Arma la compra completa (la "factura"): el pedido con sus
     * DetallePedido y el metodo con que se pago.
     */
    private PedidoResponseDTO toResponse(Pedido pedido) {
        List<ItemFacturaDTO> items = detallePedidoRepository.findByPedidoId(pedido.getId()).stream()
                .map(this::toItemFactura)
                .collect(Collectors.toList());

        // Los pedidos nuevos tienen un solo pago; los viejos de la base
        // pueden tener varios (pagos parciales) o ninguno.
        String metodoPago = pagoRepository.findByPedidoId(pedido.getId()).stream()
                .map(Pago::getMetodoPago)
                .distinct()
                .collect(Collectors.joining(", "));

        // Los datos del comprador solo los ve el ADMIN (ver PedidoResponseDTO).
        ClientePedidoDTO cliente = SecurityUtils.esAdmin() && pedido.getUsuario() != null
                ? ClientePedidoDTO.from(pedido.getUsuario())
                : null;

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getFecha(),
                pedido.getEstado(),
                cliente,
                items,
                metodoPago.isEmpty() ? null : metodoPago,
                pedido.getTotal());
    }

    private boolean esTransicionValida(String estadoActual, String estadoNuevo) {
        if (estadoNuevo == null) {
            return false;
        }
        return TRANSICIONES_VALIDAS.getOrDefault(estadoActual, Set.of()).contains(estadoNuevo);
    }

    /**
     * Al cancelar un pedido, el stock que se le desconto a cada producto
     * al pagar tiene que volver -- si no, el inventario real queda mas
     * bajo que el disponible de verdad.
     */
    private void restaurarStock(List<DetallePedido> detalles) {
        for (DetallePedido detalle : detalles) {
            Producto producto = detalle.getProducto();
            producto.reponerStock(detalle.getVariante(), detalle.getCantidad());
            productoRepository.save(producto);
        }
    }

    private ItemFacturaDTO toItemFactura(DetallePedido detalle) {
        BigDecimal subtotal = detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()));
        return new ItemFacturaDTO(
                detalle.getProducto().getId(),
                detalle.getProducto().getNombre(),
                detalle.getVariante() != null ? detalle.getVariante().getColor() : null,
                detalle.getVariante() != null ? detalle.getVariante().getNumero() : null,
                detalle.getCantidad(),
                detalle.getProducto().getUnidadMedida(),
                detalle.getPrecioUnitario(),
                subtotal);
    }
}
