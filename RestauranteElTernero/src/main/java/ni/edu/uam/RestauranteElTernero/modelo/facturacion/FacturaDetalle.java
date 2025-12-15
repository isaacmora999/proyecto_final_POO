package ni.edu.uam.RestauranteElTernero.modelo.facturacion;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
import org.openxava.annotations.DescriptionsList;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import ni.edu.uam.RestauranteElTernero.modelo.inventario.Producto;

import javax.validation.constraints.DecimalMin;


@Entity
@Getter
@Setter
public class FacturaDetalle {

    @Id
    @Hidden
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String oid;

    @ManyToOne(optional = false)
    @Required(message = "La factura es obligatoria")
    private Factura factura;

    @ManyToOne(optional = false)
    @Required(message = "El producto es obligatorio")
    @DescriptionsList(
            descriptionProperties = "nombre, tipo, precio"
    )
    private Producto producto;

    @Min(value = 1, message = "La cantidad mnima es 1")
    @Required(message = "La cantidad es obligatoria")
    private int cantidad;

    @Column(nullable = false, precision = 12, scale = 2)
    @NotNull(message = "El precio unitario no puede ser nulo")
    @DecimalMin(value = "0.01", message = "El precio unitario debe ser mayor que 0")
    private BigDecimal precioUnitario;

    @Column(nullable = false, precision = 12, scale = 2)
    @NotNull(message = "El subtotal no puede ser nulo")
    @DecimalMin(value = "0.00", message = "El subtotal no puede ser negativo")
    private BigDecimal subtotal;


    @PrePersist
    @PreUpdate
    private void recalcularSubtotal() {
        // Auto-asignar precio si viene nulo del formulario
        if (precioUnitario == null && producto != null) {
            this.precioUnitario = producto.getPrecio();
        }

        validarCantidadYPrecio();

        this.subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

        if (factura != null) {
            factura.recalcularTotal();
        }
    }

    private void validarCantidadYPrecio() {
        if (precioUnitario == null
                || cantidad <= 0
                || precioUnitario.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(
                    "Cantidad y precio unitario deben ser vlidos y mayores que 0."
            );
        }
    }

}