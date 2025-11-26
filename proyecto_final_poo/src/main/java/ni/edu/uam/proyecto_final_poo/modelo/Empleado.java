package ni.edu.uam.proyecto_final_poo.modelo;

import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;

@Entity
@Getter @Setter
public class Empleado {

    @Id
    @Column(length = 6)
    int idEmpleado;

    @Column(length = 50) @Required
    String nombre;

    @Column(length = 50) @Required
    String apellido;

    @Column(length = 20) @Required
    String usuario;

    @Column(length = 100) @Required
    @Hidden // para no mostrar la contraseña directamente
    String contrasena;

    @Column(length = 20) @Required
    @DescriptionsList // combo con valores definidos desde labels o validación simple
    String rol; // Administrador, Mesero, etc.

    @Depends("nombre, apellido")
    @Hidden
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}