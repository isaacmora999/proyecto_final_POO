package ni.edu.uam.proyecto_final_poo.modelo;

import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;

@Entity
@Getter @Setter
public class Mesa {

    @Id
    @Column(length = 4)
    int idMesa; // número o código de mesa

    @Required
    int capacidad;

    @Column(length = 15) @Required
    @DescriptionsList // Libre, Ocupada, Reservada
    String estado;
}

