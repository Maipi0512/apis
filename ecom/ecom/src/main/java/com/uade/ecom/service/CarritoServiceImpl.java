package com.uade.ecom.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.ecom.exception.AccesoDenegadoException;
import com.uade.ecom.exception.CarritoVacioException;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.exception.StockInsuficienteException;
import com.uade.ecom.model.Carrito;
import com.uade.ecom.model.DetallePedido;
import com.uade.ecom.model.ItemCarrito;
import com.uade.ecom.model.Pedido;
import com.uade.ecom.model.Producto;
import com.uade.ecom.model.Usuario;
import com.uade.ecom.repository.CarritoRepository;
import com.uade.ecom.repository.DetallePedidoRepository;
import com.uade.ecom.repository.ItemCarritoRepository;
import com.uade.ecom.repository.PedidoRepository;
import com.uade.ecom.repository.ProductoRepository;
import com.uade.ecom.util.SecurityUtils;

@Service
public class CarritoServiceImpl implements CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Override
    public List<Carrito> getAllCarritos() {
        if (SecurityUtils.esAdmin()) {
            return carritoRepository.findAll();
        }
        return carritoRepository.findByUsuarioId(SecurityUtils.getUsuarioActual().getId());
    }

    @Override
    public Carrito getCarritoById(Long id) {
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun carrito con id " + id));
        validarDueño(carrito);
        return carrito;
    }

    @Override
    public Carrito createCarrito() {
        Carrito carrito = new Carrito();
        carrito.setUsuario(SecurityUtils.getUsuarioActual());
        return carritoRepository.save(carrito);
    }

    @Override
    public void deleteCarrito(Long id) {
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun carrito con id " + id));
        validarDueño(carrito);
        carritoRepository.delete(carrito);
    }

    /**
     * Un CLIENTE solo puede ver/tocar sus propios carritos; un ADMIN
     * puede con cualquiera.
     */
    private void validarDueño(Carrito carrito) {
        if (SecurityUtils.esAdmin()) {
            return;
        }
        Usuario actual = SecurityUtils.getUsuarioActual();
        Usuario dueño = carrito.getUsuario();
        if (dueño == null || !dueño.getId().equals(actual.getId())) {
            throw new AccesoDenegadoException("El carrito " + carrito.getId() + " no pertenece al usuario autenticado");
        }
    }

    @Override
    @Transactional
    public Pedido checkout(Long carritoId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun carrito con id " + carritoId));
        validarDueño(carrito);

        List<ItemCarrito> items = itemCarritoRepository.findByCarritoId(carrito.getId());
        if (items.isEmpty()) {
            throw new CarritoVacioException("El carrito " + carritoId + " no tiene items para confirmar la compra");
        }

        // Primero validamos el stock de todos los items, antes de
        // modificar nada: si uno solo no alcanza, no queremos dejar el
        // pedido a medio armar ni haber descontado stock de otro item.
        for (ItemCarrito item : items) {
            Producto producto = item.getProducto();
            if (item.getCantidad() > producto.getStock()) {
                throw new StockInsuficienteException(
                        "No hay stock suficiente de " + producto.getNombre()
                                + " (pedido: " + item.getCantidad() + ", disponible: " + producto.getStock() + ")");
            }
        }

        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDate.now());
        pedido.setEstado("PENDIENTE");
        pedido.setTotal(BigDecimal.ZERO);
        pedido.setUsuario(carrito.getUsuario());
        pedido = pedidoRepository.save(pedido);

        BigDecimal total = BigDecimal.ZERO;
        for (ItemCarrito item : items) {
            Producto producto = item.getProducto();

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detallePedidoRepository.save(detalle);

            total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad())));

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);
        }

        pedido.setTotal(total);
        pedido = pedidoRepository.save(pedido);

        // El carrito queda vacio: los items ya se convirtieron en pedido.
        itemCarritoRepository.deleteAll(items);

        return pedido;
    }
}
