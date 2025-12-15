package ni.edu.uam.RestauranteElTernero.modelo.core;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
import org.openxava.annotations.View;

import javax.persistence.*;

import javax.validation.constraints.Pattern;


@Entity
@Getter
@Setter
@View(members = "nombre, apellido, rol; telefono, correo")
public class Empleado {

    @Id
    @Hidden
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String oid;

    @Column(length = 60, nullable = false)
    @Required(message = "El nombre del empleado es obligatorio")
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$",
            message = "El nombre del empleado solo puede contener letras y espacios"
    )
    private String nombre;

    @Column(length = 60, nullable = false)
    @Required(message = "El apellido del empleado es obligatorio")
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$",
            message = "El apellido del empleado solo puede contener letras y espacios"
    )
    private String apellido;


    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    @Required(message = "El rol es obligatorio")
    private RolEmpleado rol;

    @Column(length = 15)
    private String telefono;

    @Column(length = 80)
    private String correo;

    @Override
    public String toString() {
        return nombre + " " + apellido + " (" + rol + ")";
    }
}