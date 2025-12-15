package ni.edu.uam.RestauranteElTernero.modelo.reservas;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.DescriptionsList;
import org.openxava.annotations.Hidden;
import org.openxava.annotations.Required;

import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

import ni.edu.uam.RestauranteElTernero.modelo.core.Cliente;
import ni.edu.uam.RestauranteElTernero.modelo.core.Mesa;
import ni.edu.uam.RestauranteElTernero.modelo.core.EstadoMesa;

import javax.validation.constraints.FutureOrPresent;


@Entity
@Getter
@Setter
public class Reserva {

    @Id
    @Hidden
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    private String oid;

    @ManyToOne(optional = false)
    @Required(message = "El cliente es obligatorio")
    @DescriptionsList(descriptionProperties = "nombre, apellido")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @Required(message = "La mesa es obligatoria")
    @DescriptionsList(descriptionProperties = "numero, capacidad, estado")
    private Mesa mesa;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "La fecha y hora de la reserva son obligatorias")
    @FutureOrPresent(message = "No se permiten reservas en el pasado")
    @Required
    private Date fechaHora;

    @Column(nullable = false)
    @Min(value = 1, message = "Debe haber al menos 1 persona en la reserva")
    @Required(message = "El número de personas es obligatorio")
    private int numeroPersonas;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    @Required(message = "El estado de la reserva es obligatorio")
    private EstadoReserva estado;

    @PrePersist
    @PreUpdate
    private void validarLogicaNegocio() {
        if (mesa == null) {
            throw new ValidationException("Debe seleccionar una mesa.");
        }

        // 1) Capacidad
        if (numeroPersonas > mesa.getCapacidad()) {
            throw new ValidationException(
                    "El número de personas (" + numeroPersonas +
                            ") excede la capacidad de la mesa (" + mesa.getCapacidad() + ")."
            );
        }

        // 2) Disponibilidad
        // Permitimos reservar si está disponible O si ya está reservada (probablemente por nosotros mismos en edición)
        if ((estado == EstadoReserva.PENDIENTE || estado == EstadoReserva.CONFIRMADA) &&
                mesa.getEstado() != EstadoMesa.DISPONIBLE &&
                mesa.getEstado() != EstadoMesa.RESERVADA) {
            throw new ValidationException("La mesa seleccionada no está disponible para reservar.");
        }

        // 3) Fecha Futura (CORREGIDO)
        // Solo validamos que sea futura si es una reserva NUEVA (oid es nulo)
        Date ahora = new Date();
        if (this.oid == null && fechaHora.before(ahora)) {
            throw new ValidationException("La fecha de la reserva debe ser posterior al momento actual.");
        }

        // 4) Actualizar estado mesa
        if (estado == EstadoReserva.CANCELADA) {
            mesa.setEstado(EstadoMesa.DISPONIBLE);
        } else {
            mesa.setEstado(EstadoMesa.RESERVADA);
        }
    }
}
