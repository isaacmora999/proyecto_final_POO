package ni.edu.uam.RestauranteElTernero.modelo.core;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;
import org.openxava.annotations.View;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


import javax.validation.constraints.Pattern;


@Entity
@Getter
@Setter
@View(name = "Simple", members = "nombre, apellido")
public class Cliente {

    @Id
    @Hidden
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String oid;

    @Column(length = 60, nullable = false)
    @Required(message = "El nombre del cliente es obligatorio")
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$",
            message = "El nombre del cliente solo puede contener letras y espacios"
    )
    private String nombre;

    @Column(length = 60, nullable = false)
    @Required(message = "El apellido del cliente es obligatorio")
    @Pattern(
            regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$",
            message = "El apellido del cliente solo puede contener letras y espacios"
    )
    private String apellido;

    @Column(length = 16, nullable = false, unique = true)
    @Required(message = "La cédula es obligatoria")
    private String cedula;

    @Column(length = 15)
    private String telefono;

    @Column(length = 100)
    private String direccion;

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}