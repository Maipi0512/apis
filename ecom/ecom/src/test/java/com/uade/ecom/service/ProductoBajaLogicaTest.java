package com.uade.ecom.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import com.uade.ecom.model.Producto;
import com.uade.ecom.model.VarianteProducto;
import com.uade.ecom.repository.ProductoRepository;

class ProductoBajaLogicaTest {
    @Test
    void bajaConservaProductoYStock() {
        Producto producto = new Producto();
        producto.setId(1L);
        producto.setStock(12);
        ProductoRepository repository = mock(ProductoRepository.class);
        when(repository.findById(1L)).thenReturn(Optional.of(producto));
        ProductoServiceImpl service = new ProductoServiceImpl();
        ReflectionTestUtils.setField(service, "productoRepository", repository);

        service.deleteProducto(1L);

        assertFalse(producto.isActivo());
        assertEquals(12, producto.getStock());
        verify(repository).save(producto);
        verify(repository, never()).delete(any(Producto.class));
    }

    @Test
    void inactivoNoOfreceStockYReactivarloLoRecupera() {
        Producto producto = new Producto();
        producto.setStock(12);
        VarianteProducto variante = new VarianteProducto();
        variante.setStock(5);
        assertTrue(producto.isActivo());
        producto.setActivo(false);
        assertEquals(0, producto.stockDisponible(null));
        assertEquals(0, producto.stockDisponible(variante));
        producto.setActivo(true);
        assertEquals(12, producto.stockDisponible(null));
        assertEquals(5, producto.stockDisponible(variante));
    }
}
