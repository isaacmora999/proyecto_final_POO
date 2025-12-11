package ni.edu.uam.RestauranteElTernero.modelo.inventario;

import lombok.Getter;
import lombok.Setter;
import ni.edu.uam.RestauranteElTernero.modelo.facturacion.CategoriaProducto;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.DescriptionsList;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
import org.openxava.annotations.View;
import org.openxava.annotations.Tab;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@View(name="Simple", members="nombre, tipo, precio; descripcion")
@Tab(properties="nombre, tipo, precio, descripcion")
public class Producto {

    @Id
    @Hidden
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String oid;

    @Column(length = 80, nullable = false)
    @Required(message = "El nombre del producto es obligatorio")
    private String nombre;

    @Column(length = 120)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    @Required(message = "El tipo es obligatorio")
    private TipoProducto tipo;

    @Column(nullable = false, precision = 12, scale = 2)
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor que 0")
    @Required
    private BigDecimal precio;

    @ManyToOne(optional = false)
    @Required(message = "La categoría es obligatoria")
    @DescriptionsList(descriptionProperties = "nombre")
    private CategoriaProducto categoria;

    @Override
    public String toString() {
        return nombre;
    }
}