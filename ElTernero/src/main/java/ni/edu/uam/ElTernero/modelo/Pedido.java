package ni.edu.uam.ElTernero.modelo;

import java.math.BigDecimal;
import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;

@Entity
@Getter @Setter
public class Pedido {

    @Id
    @Column(length = 6)
    int idPedido;

    @Column(length = 150) @Required
    @DisplaySize(50)
    String detalle; // Descripción de lo pedido (platillos, combos, etc.)

    @Money @Required
    BigDecimal total;

    @Column(length = 20) @Required
    @DescriptionsList // Pendiente, En preparación, Listo, Servido
    String estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ReferenceView("Simple")
    Reserva reserva;
}
