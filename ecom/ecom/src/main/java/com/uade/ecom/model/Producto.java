package com.uade.ecom.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Entidad "Producto" del DER.
 *
 * Relaciones:
 *  - "Clasifica" (Categoria 1:N Producto) -> categoria (ManyToOne).
 */
@Entity
@Table(name = "producto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "precio", nullable = false)
    private BigDecimal precio;

    // Si el producto tiene variantes, es la suma del stock de sus
    // variantes (se mantiene sincronizado, ver recalcularStock/
    // descontarStock). Si no tiene, es el stock propio del producto.
    @Column(name = "stock", nullable = false)
    private Integer stock;

    // Variaciones (por color, por numero o ambos), cada una con su stock.
    // Viajan en el JSON de /productos para que el front pueda mostrar el
    // selector de color/numero.
    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("color ASC, numero ASC")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<VarianteProducto> variantes = new ArrayList<>();

    // Como se vende el producto (por unidad, por metro, por ovillo, etc.).
    // Sin "default"/"not null" en columnDefinition: con ddl-auto=update,
    // Hibernate reusa ese texto tal cual en el ALTER COLUMN ... SET DATA
    // TYPE, y "varchar(20) default 'UNIDAD'" ahi rompe (Postgres no
    // acepta DEFAULT en esa clausula). El default se aplica en Java
    // (aca y en ProductoServiceImpl) en vez de a nivel de columna.
    @Enumerated(EnumType.STRING)
    @Column(name = "unidad_medida", length = 20)
    private UnidadMedida unidadMedida = UnidadMedida.UNIDAD;


    // columnDefinition con "default 0" para que, si la tabla ya tiene
    // productos cargados, el ALTER TABLE de Hibernate (ddl-auto=update) no
    // falle por violar el NOT NULL en las filas existentes.
    @Column(name = "descuento_porcentaje", nullable = false, columnDefinition = "numeric(5,2) default 0")
    private BigDecimal descuentoPorcentaje = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Imagen del producto guardada como bytes directo en la base (bytea en
    // Postgres). @Lob + @JsonIgnore para que NO viaje en el JSON de
    // /productos (seria un base64 gigante en cada producto de la lista):
    // la imagen se sube y se descarga por endpoints propios
    // (POST/GET /productos/{id}/imagen), no como parte del Producto.
    @Lob
    @Column(name = "imagen")
    @JsonIgnore
    @ToString.Exclude
    private byte[] imagen;

    @Column(name = "imagen_content_type")
    @JsonIgnore
    private String imagenContentType;

    // No se guarda en la base (@Transient): se calcula al vuelo cada vez
    // que se serializa el producto a JSON, así el front siempre ve el
    // precio ya con el descuento aplicado sin tener que calcularlo el.
    @Transient
    public BigDecimal getPrecioFinal() {
        BigDecimal descuento = precio
                .multiply(descuentoPorcentaje)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return precio.subtract(descuento);
    }

    // Tambien @Transient: si el producto tiene imagen cargada, le decimos
    // al front donde puede pedirla (GET a esta url devuelve los bytes de
    // la imagen con su Content-Type real, lista para poner en un <img src>).
    @Transient
    public String getImagenUrl() {
        return (imagen != null && imagen.length > 0) ? "/productos/" + id + "/imagen" : null;
    }

    // Los metodos de abajo no empiezan con get/is a proposito, para que
    // Jackson no los agregue al JSON.

    public boolean tieneVariantes() {
        return variantes != null && !variantes.isEmpty();
    }

    /**
     * Stock disponible para lo que el cliente eligio: el de la variante
     * si el producto tiene variantes, el del producto si no.
     */
    public int stockDisponible(VarianteProducto variante) {
        return variante != null ? variante.getStock() : stock;
    }

    /**
     * Descuenta de la variante y del producto a la vez, asi el stock del
     * producto sigue siendo la suma de sus variantes.
     */
    public void descontarStock(VarianteProducto variante, int cantidad) {
        if (variante != null) {
            variante.setStock(variante.getStock() - cantidad);
        }
        stock = stock - cantidad;
    }

    public void reponerStock(VarianteProducto variante, int cantidad) {
        if (variante != null) {
            variante.setStock(variante.getStock() + cantidad);
        }
        stock = stock + cantidad;
    }

    /**
     * Si tiene variantes, el stock del producto pasa a ser la suma de sus
     * variantes. Se llama cada vez que el ADMIN agrega/edita/borra una.
     */
    public void recalcularStock() {
        if (tieneVariantes()) {
            stock = variantes.stream().mapToInt(VarianteProducto::getStock).sum();
        }
    }
}