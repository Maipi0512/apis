package com.uade.ecom.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.ecom.dto.ItemCarritoRequestDTO;
import com.uade.ecom.exception.DatoInvalidoException;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.exception.StockInsuficienteException;
import com.uade.ecom.model.Carrito;
import com.uade.ecom.model.ItemCarrito;
import com.uade.ecom.model.Producto;
import com.uade.ecom.model.VarianteProducto;
import com.uade.ecom.repository.CarritoRepository;
import com.uade.ecom.repository.ItemCarritoRepository;
import com.uade.ecom.repository.ProductoRepository;
import com.uade.ecom.util.SecurityUtils;

/**
 * Siempre sobre el carrito del usuario autenticado (el que se le crea al
 * registrarse), asi que no hace falta validar dueño: un cliente no puede
 * llegar a items de otro carrito.
 */
@Service
public class ItemCarritoServiceImpl implements ItemCarritoService {

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional
    public void agregar(ItemCarritoRequestDTO itemCarritoRequestDTO) {
        validarCantidad(itemCarritoRequestDTO.getCantidad());

        Carrito carrito = getMiCarrito();
        Producto producto = productoRepository.findById(itemCarritoRequestDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun producto con id " + itemCarritoRequestDTO.getProductoId()));
        VarianteProducto variante = resolverVariante(producto, itemCarritoRequestDTO.getVarianteId());

        List<ItemCarrito> lineas = getLineas(carrito, producto.getId(), itemCarritoRequestDTO.getVarianteId());

        if (lineas.isEmpty()) {
            validarStock(producto, variante, itemCarritoRequestDTO.getCantidad());
            ItemCarrito itemCarrito = new ItemCarrito();
            itemCarrito.setCarrito(carrito);
            itemCarrito.setProducto(producto);
            itemCarrito.setVariante(variante);
            itemCarrito.setCantidad(itemCarritoRequestDTO.getCantidad());
            itemCarritoRepository.save(itemCarrito);
            return;
        }

        int cantidadActual = lineas.stream().mapToInt(ItemCarrito::getCantidad).sum();
        guardarEnUnaLinea(lineas, cantidadActual + itemCarritoRequestDTO.getCantidad());
    }

    @Override
    @Transactional
    public void cambiarCantidad(Long productoId, Long varianteId, Integer cantidad) {
        validarCantidad(cantidad);
        guardarEnUnaLinea(getLineasExistentes(productoId, varianteId), cantidad);
    }

    @Override
    @Transactional
    public void quitar(Long productoId, Long varianteId) {
        itemCarritoRepository.deleteAll(getLineasExistentes(productoId, varianteId));
    }

    private Carrito getMiCarrito() {
        return carritoRepository
                .findFirstByUsuario_IdOrderByIdAsc(SecurityUtils.getUsuarioActual().getId())
                .orElseThrow(() -> new ResourceNotFoundException("El usuario autenticado no tiene ningun carrito"));
    }

    /**
     * Si el producto tiene variantes (color/numero), el cliente tiene que
     * elegir una de ESE producto; si no tiene, no se puede mandar.
     */
    private VarianteProducto resolverVariante(Producto producto, Long varianteId) {
        if (!producto.tieneVariantes()) {
            if (varianteId != null) {
                throw new DatoInvalidoException("El producto " + producto.getNombre() + " no tiene variantes");
            }
            return null;
        }

        if (varianteId == null) {
            throw new DatoInvalidoException("Hay que elegir color/numero para " + producto.getNombre()
                    + ". Opciones: " + producto.getVariantes().stream()
                            .map(v -> v.getDescripcion() + " (varianteId " + v.getId() + ")")
                            .collect(Collectors.joining(", ")));
        }
        return producto.getVariantes().stream()
                .filter(v -> v.getId().equals(varianteId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El producto " + producto.getNombre() + " no tiene ninguna variante con id " + varianteId));
    }

    /**
     * Lineas del carrito para ese producto en esa variante (varianteId
     * null = producto sin variantes). El filtro por variante se hace aca y
     * no en la query porque la variante puede ser null.
     */
    private List<ItemCarrito> getLineas(Carrito carrito, Long productoId, Long varianteId) {
        return itemCarritoRepository.findByCarritoIdAndProductoId(carrito.getId(), productoId).stream()
                .filter(item -> Objects.equals(varianteId, item.getVariante() != null ? item.getVariante().getId() : null))
                .collect(Collectors.toList());
    }

    private List<ItemCarrito> getLineasExistentes(Long productoId, Long varianteId) {
        List<ItemCarrito> lineas = getLineas(getMiCarrito(), productoId, varianteId);
        if (lineas.isEmpty()) {
            throw new ResourceNotFoundException("Ese producto" + (varianteId != null ? " en esa variante" : "")
                    + " no esta en el carrito");
        }
        return lineas;
    }

    /**
     * Deja el producto+variante en una sola linea con la cantidad indicada.
     * Si habia lineas repetidas (carritos viejos), las junta en la primera.
     */
    private void guardarEnUnaLinea(List<ItemCarrito> lineas, int cantidad) {
        ItemCarrito linea = lineas.get(0);
        validarStock(linea.getProducto(), linea.getVariante(), cantidad);

        linea.setCantidad(cantidad);
        itemCarritoRepository.save(linea);

        if (lineas.size() > 1) {
            itemCarritoRepository.deleteAll(lineas.subList(1, lineas.size()));
        }
    }

    private void validarCantidad(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new DatoInvalidoException("La cantidad tiene que ser mayor a 0");
        }
    }

    /**
     * El pago vuelve a validar esto (el stock puede cambiar entre que se
     * agrega al carrito y se paga), pero avisar aca tambien evita que el
     * carrito muestre cantidades que de entrada ya sabemos que no se van
     * a poder comprar. Con variantes, cuenta el stock de la elegida.
     */
    private void validarStock(Producto producto, VarianteProducto variante, int cantidad) {
        int disponible = producto.stockDisponible(variante);
        if (cantidad > disponible) {
            throw new StockInsuficienteException(
                    "No hay stock suficiente de " + producto.getNombre()
                            + (variante != null ? " " + variante.getDescripcion() : "")
                            + " (pedido: " + cantidad + ", disponible: " + disponible + ")");
        }
    }
}
