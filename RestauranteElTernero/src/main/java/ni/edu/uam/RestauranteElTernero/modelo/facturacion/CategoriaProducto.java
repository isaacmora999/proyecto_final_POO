package ni.edu.uam.RestauranteElTernero.modelo.facturacion;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class CategoriaProducto {

    @Id
    @Hidden
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String oid;

    @Column(length = 50, nullable = false, unique = true)
    @Required(message = "El nombre de la categoría es obligatorio")
    private String nombre;

    @Column(length = 120)
    private String descripcion;

    @Override
    public String toString() {
        return nombre;
    }
}