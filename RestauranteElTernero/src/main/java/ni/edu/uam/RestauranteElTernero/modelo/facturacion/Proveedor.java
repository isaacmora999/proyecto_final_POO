package ni.edu.uam.RestauranteElTernero.modelo.facturacion;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Proveedor {

    @Id
    @Hidden
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String oid;

    @Column(length = 80, nullable = false)
    @Required(message = "El nombre del proveedor es obligatorio")
    private String nombre;

    @Column(length = 15)
    private String telefono;

    @Column(length = 100)
    private String direccion;

    @Override
    public String toString() {
        return nombre;
    }
}