package com.uade.ecom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.ecom.dto.ItemCarritoRequestDTO;
import com.uade.ecom.exception.AccesoDenegadoException;
import com.uade.ecom.exception.DatoInvalidoException;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.exception.StockInsuficienteException;
import com.uade.ecom.model.Carrito;
import com.uade.ecom.model.ItemCarrito;
import com.uade.ecom.model.Producto;
import com.uade.ecom.model.Usuario;
import com.uade.ecom.repository.CarritoRepository;
import com.uade.ecom.repository.ItemCarritoRepository;
import com.uade.ecom.repository.ProductoRepository;
import com.uade.ecom.util.SecurityUtils;

@Service
public class ItemCarritoServiceImpl implements ItemCarritoService {

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<ItemCarrito> getAllItemsCarrito() {
        if (SecurityUtils.esAdmin()) {
            return itemCarritoRepository.findAll();
        }
        return itemCarritoRepository.findByCarrito_Usuario_Id(SecurityUtils.getUsuarioActual().getId());
    }

    @Override
    public ItemCarrito getItemCarritoById(Long id) {
        ItemCarrito itemCarrito = itemCarritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun item de carrito con id " + id));
        validarDueño(itemCarrito);
        return itemCarrito;
    }

    @Override
    public ItemCarrito createItemCarrito(ItemCarritoRequestDTO itemCarritoRequestDTO) {
        validarCantidad(itemCarritoRequestDTO.getCantidad());

        // El item siempre se agrega al carrito del usuario autenticado
        // (el que se le crea automaticamente al registrarse), no a uno
        // que el cliente elija por id.
        Carrito carrito = carritoRepository
                .findFirstByUsuario_IdOrderByIdAsc(SecurityUtils.getUsuarioActual().getId())
                .orElseThrow(() -> new ResourceNotFoundException("El usuario autenticado no tiene ningun carrito"));

        Producto producto = productoRepository.findById(itemCarritoRequestDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun producto con id " + itemCarritoRequestDTO.getProductoId()));
        validarStock(producto, itemCarritoRequestDTO.getCantidad());

        ItemCarrito itemCarrito = new ItemCarrito();
        itemCarrito.setCarrito(carrito);
        itemCarrito.setProducto(producto);
        itemCarrito.setCantidad(itemCarritoRequestDTO.getCantidad());

        return itemCarritoRepository.save(itemCarrito);
    }

    @Override
    public ItemCarrito updateItemCarrito(Long id, ItemCarritoRequestDTO itemCarritoRequestDTO) {
        validarCantidad(itemCarritoRequestDTO.getCantidad());

        ItemCarrito itemCarrito = itemCarritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun item de carrito con id " + id));
        validarDueño(itemCarrito);

        Producto producto = productoRepository.findById(itemCarritoRequestDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ningun producto con id " + itemCarritoRequestDTO.getProductoId()));
        validarStock(producto, itemCarritoRequestDTO.getCantidad());

        itemCarrito.setProducto(producto);
        itemCarrito.setCantidad(itemCarritoRequestDTO.getCantidad());

        return itemCarritoRepository.save(itemCarrito);
    }

    @Override
    public void deleteItemCarrito(Long id) {
        ItemCarrito itemCarrito = itemCarritoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun item de carrito con id " + id));
        validarDueño(itemCarrito);
        itemCarritoRepository.delete(itemCarrito);
    }

    /**
     * Un CLIENTE solo puede ver/tocar items de SU carrito; un ADMIN
     * puede con cualquiera (mismo criterio que CarritoServiceImpl).
     */
    private void validarDueño(ItemCarrito itemCarrito) {
        if (SecurityUtils.esAdmin()) {
            return;
        }
        Usuario actual = SecurityUtils.getUsuarioActual();
        Usuario dueño = itemCarrito.getCarrito().getUsuario();
        if (dueño == null || !dueño.getId().equals(actual.getId())) {
            throw new AccesoDenegadoException(
                    "El item de carrito " + itemCarrito.getId() + " no pertenece al usuario autenticado");
        }
    }

    private void validarCantidad(Integer cantidad) {
        if (cantidad == null || cantidad <= 0) {
            throw new DatoInvalidoException("La cantidad tiene que ser mayor a 0");
        }
    }

    /**
     * El checkout vuelve a validar esto (el stock puede cambiar entre que
     * se agrega al carrito y se confirma la compra), pero avisar aca
     * tambien evita que el carrito muestre cantidades que de entrada ya
     * sabemos que no se van a poder comprar.
     */
    private void validarStock(Producto producto, Integer cantidad) {
        if (cantidad > producto.getStock()) {
            throw new StockInsuficienteException(
                    "No hay stock suficiente de " + producto.getNombre()
                            + " (pedido: " + cantidad + ", disponible: " + producto.getStock() + ")");
        }
    }
}
