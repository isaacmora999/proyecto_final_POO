package ni.edu.uam.ElTernero.modelo;

import java.time.*;
import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;

@Entity
@Getter @Setter
public class Reporte {

    @Id
    @Column(length = 6)
    int idReporte;

    @Column(length = 30) @Required
    String tipo; // Ocupación, Ventas, Actividad, etc.

    @Required
    LocalDateTime fechaGeneracion;

    @TextArea
    String contenido;
}
