package com.uade.ecom.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.uade.ecom.dto.ProductoRequestDTO;
import com.uade.ecom.dto.VarianteProductoRequestDTO;
import com.uade.ecom.exception.DatoInvalidoException;
import com.uade.ecom.exception.DescuentoInvalidoException;
import com.uade.ecom.exception.EntidadEnUsoException;
import com.uade.ecom.exception.ResourceNotFoundException;
import com.uade.ecom.model.Categoria;
import com.uade.ecom.model.Producto;
import com.uade.ecom.model.UnidadMedida;
import com.uade.ecom.model.VarianteProducto;
import com.uade.ecom.repository.CategoriaRepository;
import com.uade.ecom.repository.DetallePedidoRepository;
import com.uade.ecom.repository.ItemCarritoRepository;
import com.uade.ecom.repository.ProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Override
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto getProductoById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun producto con id " + id));
    }

    @Override
    public Producto createProducto(ProductoRequestDTO productoRequestDTO) {
        validarDatosProducto(productoRequestDTO);

        Categoria categoria = categoriaRepository.findById(productoRequestDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ninguna categoria con id " + productoRequestDTO.getCategoriaId()));

        Producto producto = new Producto();
        producto.setNombre(productoRequestDTO.getNombre());
        producto.setPrecio(productoRequestDTO.getPrecio());
        // Si despues se le agregan variantes, este stock se reemplaza por
        // la suma de las variantes (ver agregarVariante).
        producto.setStock(productoRequestDTO.getStock() == null ? 0 : productoRequestDTO.getStock());
        producto.setCategoria(categoria);
        producto.setDescuentoPorcentaje(validarDescuento(productoRequestDTO.getDescuentoPorcentaje()));
        producto.setUnidadMedida(validarUnidadMedida(productoRequestDTO.getUnidadMedida()));

        return productoRepository.save(producto);
    }

    @Override
    public Producto updateProducto(Long id, ProductoRequestDTO productoRequestDTO) {
        validarDatosProducto(productoRequestDTO);

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun producto con id " + id));

        Categoria categoria = categoriaRepository.findById(productoRequestDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro ninguna categoria con id " + productoRequestDTO.getCategoriaId()));

        producto.setNombre(productoRequestDTO.getNombre());
        producto.setPrecio(productoRequestDTO.getPrecio());
        // Con variantes, el stock del producto es la suma de las variantes
        // y se edita por /productos/{id}/variantes: el que venga aca se ignora.
        if (!producto.tieneVariantes() && productoRequestDTO.getStock() != null) {
            producto.setStock(productoRequestDTO.getStock());
        }
        producto.setCategoria(categoria);
        producto.setDescuentoPorcentaje(validarDescuento(productoRequestDTO.getDescuentoPorcentaje()));
        producto.setUnidadMedida(validarUnidadMedida(productoRequestDTO.getUnidadMedida()));

        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public Producto agregarVariante(Long productoId, VarianteProductoRequestDTO varianteProductoRequestDTO) {
        Producto producto = getProductoById(productoId);
        validarVariante(varianteProductoRequestDTO);
        String color = limpiar(varianteProductoRequestDTO.getColor());
        String numero = limpiar(varianteProductoRequestDTO.getNumero());
        validarNoRepetida(producto, color, numero, null);

        VarianteProducto variante = new VarianteProducto();
        variante.setColor(color);
        variante.setNumero(numero);
        variante.setStock(varianteProductoRequestDTO.getStock());
        variante.setProducto(producto);
        producto.getVariantes().add(variante);

        producto.recalcularStock();
        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public Producto actualizarVariante(Long productoId, Long varianteId,
            VarianteProductoRequestDTO varianteProductoRequestDTO) {
        Producto producto = getProductoById(productoId);
        VarianteProducto variante = getVarianteDeProducto(producto, varianteId);
        validarVariante(varianteProductoRequestDTO);
        String color = limpiar(varianteProductoRequestDTO.getColor());
        String numero = limpiar(varianteProductoRequestDTO.getNumero());
        validarNoRepetida(producto, color, numero, varianteId);

        variante.setColor(color);
        variante.setNumero(numero);
        variante.setStock(varianteProductoRequestDTO.getStock());

        producto.recalcularStock();
        return productoRepository.save(producto);
    }

    /**
     * Una variante que ya se vendio no se puede borrar (las compras la
     * siguen mostrando); si solo esta en carritos, se saca de esos
     * carritos. Si era la ultima, el producto queda sin variantes y con
     * stock 0 hasta que el ADMIN lo cargue.
     */
    @Override
    @Transactional
    public Producto eliminarVariante(Long productoId, Long varianteId) {
        Producto producto = getProductoById(productoId);
        VarianteProducto variante = getVarianteDeProducto(producto, varianteId);

        if (detallePedidoRepository.existsByVariante_Id(varianteId)) {
            throw new EntidadEnUsoException(
                    "No se puede eliminar la variante " + variante.getDescripcion() + " porque tiene pedidos asociados"
                            + " (se puede dejar con stock 0)");
        }

        itemCarritoRepository.deleteAll(itemCarritoRepository.findByVariante_Id(varianteId));

        producto.getVariantes().remove(variante);
        if (producto.tieneVariantes()) {
            producto.recalcularStock();
        } else {
            producto.setStock(0);
        }
        return productoRepository.save(producto);
    }

    private VarianteProducto getVarianteDeProducto(Producto producto, Long varianteId) {
        return producto.getVariantes().stream()
                .filter(v -> v.getId().equals(varianteId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "El producto " + producto.getId() + " no tiene ninguna variante con id " + varianteId));
    }

    private void validarVariante(VarianteProductoRequestDTO dto) {
        if (limpiar(dto.getColor()) == null && limpiar(dto.getNumero()) == null) {
            throw new DatoInvalidoException("La variante tiene que tener color, numero o ambos");
        }
        if (dto.getStock() == null || dto.getStock() < 0) {
            throw new DatoInvalidoException("El stock de la variante no puede ser negativo");
        }
    }

    /**
     * No puede haber dos variantes con el mismo color y numero en el
     * mismo producto (sin importar mayusculas). varianteIdAExcluir se usa
     * al editar, para no compararla consigo misma.
     */
    private void validarNoRepetida(Producto producto, String color, String numero, Long varianteIdAExcluir) {
        boolean repetida = producto.getVariantes().stream()
                .filter(v -> !v.getId().equals(varianteIdAExcluir))
                .anyMatch(v -> mismoTexto(v.getColor(), color) && mismoTexto(v.getNumero(), numero));
        if (repetida) {
            VarianteProducto nueva = new VarianteProducto();
            nueva.setColor(color);
            nueva.setNumero(numero);
            throw new EntidadEnUsoException(
                    "El producto " + producto.getId() + " ya tiene la variante " + nueva.getDescripcion());
        }
    }

    private boolean mismoTexto(String a, String b) {
        return a == null ? b == null : a.equalsIgnoreCase(b);
    }

    private String limpiar(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }

    /**
     * Si no mandan unidadMedida, el producto queda como UNIDAD.
     */
    private UnidadMedida validarUnidadMedida(UnidadMedida unidadMedida) {
        return unidadMedida == null ? UnidadMedida.UNIDAD : unidadMedida;
    }

    /**
     * Si no mandan descuentoPorcentaje, el producto queda sin descuento (0).
     * Si lo mandan, tiene que estar entre 0 y 100.
     */
    private BigDecimal validarDescuento(BigDecimal descuentoPorcentaje) {
        if (descuentoPorcentaje == null) {
            return BigDecimal.ZERO;
        }
        if (descuentoPorcentaje.compareTo(BigDecimal.ZERO) < 0
                || descuentoPorcentaje.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new DescuentoInvalidoException(
                    "El descuentoPorcentaje debe estar entre 0 y 100, se recibio " + descuentoPorcentaje);
        }
        return descuentoPorcentaje;
    }

    @Override
    public void deleteProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun producto con id " + id));

        if (detallePedidoRepository.existsByProductoId(id)) {
            throw new EntidadEnUsoException(
                    "No se puede eliminar el producto " + id + " porque tiene pedidos asociados");
        }

        productoRepository.delete(producto);
    }

    @Override
    public Producto actualizarImagen(Long id, MultipartFile file) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun producto con id " + id));

        if (file == null || file.isEmpty()) {
            throw new DatoInvalidoException("Hay que mandar un archivo de imagen (no puede venir vacio)");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new DatoInvalidoException(
                    "El archivo tiene que ser una imagen (jpg, png, etc.), se recibio " + contentType);
        }

        try {
            producto.setImagen(file.getBytes());
            producto.setImagenContentType(contentType);
        } catch (IOException e) {
            throw new DatoInvalidoException("No se pudo leer el archivo de imagen enviado");
        }

        return productoRepository.save(producto);
    }

    @Override
    public Producto getImagenProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro ningun producto con id " + id));

        if (producto.getImagen() == null || producto.getImagen().length == 0) {
            throw new ResourceNotFoundException("El producto " + id + " todavia no tiene una imagen cargada");
        }

        return producto;
    }

    private void validarDatosProducto(ProductoRequestDTO dto) {
        if (dto.getPrecio() == null || dto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new DatoInvalidoException("El precio del producto tiene que ser mayor a 0");
        }
        // stock es opcional: los productos con variantes lo calculan solos.
        if (dto.getStock() != null && dto.getStock() < 0) {
            throw new DatoInvalidoException("El stock del producto no puede ser negativo");
        }
    }
}
