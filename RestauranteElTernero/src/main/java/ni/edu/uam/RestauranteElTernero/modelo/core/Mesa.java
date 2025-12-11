package ni.edu.uam.RestauranteElTernero.modelo.core;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
import org.openxava.annotations.View;

import javax.persistence.*;
import javax.validation.constraints.Min;

@Entity
@Getter
@Setter
@View(members = "numero, capacidad, ubicacion, estado")
public class Mesa {

    @Id
    @Hidden
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String oid;

    @Column(length = 10, nullable = false, unique = true)
    @Required(message = "El número de mesa es obligatorio")
    private String numero;

    @Column(nullable = false)
    @Min(value = 1, message = "La capacidad mínima de la mesa es 1 persona")
    @Required(message = "La capacidad es obligatoria")
    private int capacidad;

    @Column(length = 40)
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    @Required(message = "El estado de la mesa es obligatorio")
    private EstadoMesa estado;

    @Override
    public String toString() {
        return numero + " (" + ubicacion + ")";
    }
}