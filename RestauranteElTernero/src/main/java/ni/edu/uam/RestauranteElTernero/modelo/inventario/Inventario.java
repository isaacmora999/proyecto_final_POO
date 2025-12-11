package ni.edu.uam.RestauranteElTernero.modelo.inventario;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.DescriptionsList;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
import javax.validation.constraints.Min;
import javax.persistence.*;

@Entity
@Getter
@Setter
public class Inventario {

    @Id
    @Hidden
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String oid;

    @ManyToOne(optional = false)
    @Required(message = "El producto es obligatorio")
    @DescriptionsList(descriptionProperties = "nombre, tipo, precio")
    private Producto producto;

    @Column(nullable = false)
    @Min(value = 0, message = "La cantidad actual no puede ser negativa")
    @Required(message = "La cantidad actual es obligatoria")
    private int cantidadActual;

    @Column(nullable = false)
    @Min(value = 0, message = "El stock mínimo no puede ser negativo")
    @Required(message = "El stock mínimo es obligatorio")
    private int stockMinimo;
}