package com.uade.ecom.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.ecom.exception.CarritoVacioException;
import com.uade.ecom.exception.DatoInvalidoException;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.exception.StockInsuficienteException;
import com.uade.ecom.model.Carrito;
import com.uade.ecom.model.DetallePedido;
import com.uade.ecom.model.ItemCarrito;
import com.uade.ecom.model.Pedido;
import com.uade.ecom.model.Producto;
import com.uade.ecom.model.VarianteProducto;
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

    // readOnly: el producto trae la imagen como @Lob, y Postgres solo deja
    // leerla dentro de una transaccion (si no: "Unable to access lob stream")
    @Transactional(readOnly = true)
    @Override
    public List<ItemCarrito> getItemsDeMiCarrito() {
        return itemCarritoRepository.findByCarritoId(getMiCarrito().getId());
    }

    @Override
    @Transactional
    public void vaciarMiCarrito() {
        itemCarritoRepository.deleteAll(getItemsDeMiCarrito());
    }

    /**
     * El carrito "principal" del usuario autenticado: el que se le crea
     * automaticamente al registrarse.
     */
    private Carrito getMiCarrito() {
        return carritoRepository
                .findFirstByUsuario_IdOrderByIdAsc(SecurityUtils.getUsuarioActual().getId())
                .orElseThrow(() -> new ResourceNotFoundException("El usuario autenticado no tiene ningun carrito"));
    }

    @Override
    @Transactional
    public Pedido crearPedidoPagado() {
        Carrito carrito = getMiCarrito();

        List<ItemCarrito> items = itemCarritoRepository.findByCarritoId(carrito.getId());
        if (items.isEmpty()) {
            throw new CarritoVacioException("El carrito no tiene items para pagar");
        }

        // Primero validamos el stock de todos los items, antes de
        // modificar nada: si uno solo no alcanza, no queremos dejar el
        // pedido a medio armar ni haber descontado stock de otro item.
        // Con variantes, cuenta el stock de la variante elegida.
        for (ItemCarrito item : items) {
            Producto producto = item.getProducto();
            VarianteProducto variante = item.getVariante();
            if (producto.tieneVariantes() && variante == null) {
                // Item agregado antes de que el producto tuviera variantes.
                throw new DatoInvalidoException("Hay que elegir color/numero para " + producto.getNombre()
                        + ": sacalo del carrito y volve a agregarlo eligiendo la variante");
            }
            int disponible = producto.stockDisponible(variante);
            if (item.getCantidad() > disponible) {
                throw new StockInsuficienteException(
                        "No hay stock suficiente de " + producto.getNombre()
                                + (variante != null ? " " + variante.getDescripcion() : "")
                                + " (pedido: " + item.getCantidad() + ", disponible: " + disponible + ")");
            }
        }

        // El pedido nace ya PAGADO: solo se crea cuando se registra el
        // pago (ver PagoServiceImpl.pagar()).
        Pedido pedido = new Pedido();
        pedido.setFecha(LocalDate.now());
        pedido.setEstado("PAGADO");
        pedido.setTotal(BigDecimal.ZERO);
        pedido.setUsuario(carrito.getUsuario());
        pedido = pedidoRepository.save(pedido);

        BigDecimal total = BigDecimal.ZERO;
        for (ItemCarrito item : items) {
            Producto producto = item.getProducto();

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setVariante(item.getVariante());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecioFinal());
            detallePedidoRepository.save(detalle);

            total = total.add(producto.getPrecioFinal().multiply(BigDecimal.valueOf(item.getCantidad())));

            // Descuenta de la variante y del producto (el stock del
            // producto es la suma de sus variantes). La variante se guarda
            // en cascada.
            producto.descontarStock(item.getVariante(), item.getCantidad());
            productoRepository.save(producto);
        }

        pedido.setTotal(total);
        pedido = pedidoRepository.save(pedido);

        // El carrito queda vacio: los items ya se convirtieron en pedido.
        itemCarritoRepository.deleteAll(items);

        return pedido;
    }
}
