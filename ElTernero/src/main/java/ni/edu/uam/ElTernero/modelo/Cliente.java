package ni.edu.uam.ElTernero.modelo;

import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;

@Entity
@Getter @Setter
@View(name="Simple",
        members="idCliente, nombreCompleto"
)
public class Cliente {

    @Id
    @Column(length = 6)
    int idCliente;

    @Column(length = 50) @Required
    String nombre;

    @Column(length = 50) @Required
    String apellido;

    @Depends("nombre, apellido")
    @Hidden
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    @Column(length = 15) @Required
    @DisplaySize(15)
    String telefono;

    @Column(length = 80)
    String correo;

    @Column(length = 100)
    @DisplaySize(40)
    String direccion;
}

