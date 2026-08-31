package com.uade.ecom.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.DetallePedidoRequestDTO;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.DetallePedido;
import com.uade.ecom.model.Pedido;
import com.uade.ecom.model.Producto;
import com.uade.ecom.repository.DetallePedidoRepository;
import com.uade.ecom.repository.PedidoRepository;
import com.uade.ecom.repository.ProductoRepository;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<DetallePedido> getAllDetallesPedido() {
        return detallePedidoRepository.findAll();
    }

    @Override
    public DetallePedido getDetallePedidoById(Long id) {
        return detallePedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun detalle con id " + id));
    }

    @Override
    public DetallePedido createDetallePedido(DetallePedidoRequestDTO detallePedidoRequestDTO) {
        Pedido pedido = pedidoRepository.findById(detallePedidoRequestDTO.getPedidoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun pedido con id " + detallePedidoRequestDTO.getPedidoId()));

        Producto producto = productoRepository.findById(detallePedidoRequestDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun producto con id " + detallePedidoRequestDTO.getProductoId()));

        DetallePedido detalle = new DetallePedido();
        detalle.setPedido(pedido);
        detalle.setProducto(producto);
        detalle.setCantidad(detallePedidoRequestDTO.getCantidad());
        // El precio unitario se toma del precio actual del producto, no
        // del usuario, para que no lo pueda manipular desde el body.
        detalle.setPrecioUnitario(producto.getPrecio());

        DetallePedido detalleGuardado = detallePedidoRepository.save(detalle);

        // Actualizamos el total del pedido sumando esta linea nueva.
        BigDecimal subtotal = producto.getPrecio()
                .multiply(BigDecimal.valueOf(detallePedidoRequestDTO.getCantidad()));
        pedido.setTotal(pedido.getTotal().add(subtotal));
        pedidoRepository.save(pedido);

        return detalleGuardado;
    }

    @Override
    public DetallePedido updateDetallePedido(Long id, DetallePedidoRequestDTO detallePedidoRequestDTO) {
        DetallePedido detalle = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun detalle con id " + id));

        Pedido nuevoPedido = pedidoRepository.findById(detallePedidoRequestDTO.getPedidoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun pedido con id " + detallePedidoRequestDTO.getPedidoId()));

        Producto nuevoProducto = productoRepository.findById(detallePedidoRequestDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun producto con id " + detallePedidoRequestDTO.getProductoId()));

        // Sacamos el subtotal viejo del total del pedido original (puede
        // ser el mismo pedido o uno distinto si cambio el pedidoId).
        Pedido pedidoOriginal = detalle.getPedido();
        BigDecimal subtotalViejo = detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()));
        pedidoOriginal.setTotal(pedidoOriginal.getTotal().subtract(subtotalViejo));

        // El precio unitario se vuelve a tomar del precio actual del
        // producto, no del body, por la misma razon que en el alta.
        detalle.setPedido(nuevoPedido);
        detalle.setProducto(nuevoProducto);
        detalle.setCantidad(detallePedidoRequestDTO.getCantidad());
        detalle.setPrecioUnitario(nuevoProducto.getPrecio());

        BigDecimal subtotalNuevo = nuevoProducto.getPrecio()
                .multiply(BigDecimal.valueOf(detallePedidoRequestDTO.getCantidad()));
        nuevoPedido.setTotal(nuevoPedido.getTotal().add(subtotalNuevo));

        pedidoRepository.save(pedidoOriginal);
        if (!pedidoOriginal.getId().equals(nuevoPedido.getId())) {
            pedidoRepository.save(nuevoPedido);
        }

        return detallePedidoRepository.save(detalle);
    }

    @Override
    public void deleteDetallePedido(Long id) {
        DetallePedido detalle = detallePedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun detalle con id " + id));

        Pedido pedido = detalle.getPedido();
        BigDecimal subtotal = detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()));
        pedido.setTotal(pedido.getTotal().subtract(subtotal));
        pedidoRepository.save(pedido);

        detallePedidoRepository.delete(detalle);
    }
}
