package ni.edu.uam.proyecto_final_poo.modelo;

import java.time.*;
import java.util.Collection;
import javax.persistence.*;
import org.openxava.annotations.*;
import lombok.*;

@Entity
@Getter @Setter
@View(members =
        "fecha, hora, estado;" +
                "cliente;" +
                "empleado;" +
                "mesa;" +
                "pedidos"
)
public class Reserva {

    @Id
    @Column(length = 6)
    int idReserva;

    @Required
    LocalDate fecha;

    @Required
    LocalTime hora;

    @Column(length = 20) @Required
    @DescriptionsList // Activa, Cancelada, Completada
    String estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @ReferenceView("Simple")
    Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    Empleado empleado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    Mesa mesa;

    @OneToMany(mappedBy = "reserva")
    @ListProperties("idPedido, detalle, total, estado")
    Collection<Pedido> pedidos;
}