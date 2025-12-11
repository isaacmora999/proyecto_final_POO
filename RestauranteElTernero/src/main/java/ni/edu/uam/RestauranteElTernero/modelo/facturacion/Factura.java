package ni.edu.uam.RestauranteElTernero.modelo.facturacion;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.openxava.annotations.*;
import org.openxava.jpa.XPersistence;

import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import ni.edu.uam.RestauranteElTernero.modelo.reservas.Reserva;
import ni.edu.uam.RestauranteElTernero.modelo.core.Cliente;
import ni.edu.uam.RestauranteElTernero.modelo.core.Empleado;
import ni.edu.uam.RestauranteElTernero.modelo.core.Mesa;
import ni.edu.uam.RestauranteElTernero.modelo.core.EstadoMesa;
import ni.edu.uam.RestauranteElTernero.modelo.inventario.Inventario;
import ni.edu.uam.RestauranteElTernero.modelo.inventario.Producto;

@Entity
@Getter
@Setter
@View(members =
        "cliente, mesa, mesero; " +
                "reserva; " +
                "fecha, estado, total; " +
                "detalles"
)
public class Factura {

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
    @DescriptionsList(
            descriptionProperties = "numero, capacidad, estado"
    )
    private Mesa mesa;

    @ManyToOne(optional = false)
    @Required(message = "El mesero es obligatorio")
    @DescriptionsList(descriptionProperties = "nombre, apellido")
    private Empleado mesero;

    @ManyToOne(optional = true)
    @DescriptionsList(
            descriptionProperties = "cliente.nombre, mesa.numero, fechaHora, estado"
    )
    private Reserva reserva;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    @Required(message = "La fecha es obligatoria")
    private Date fecha;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    @Required(message = "El estado de la factura es obligatorio")
    private EstadoFactura estado;

    // --- CAMBIO FINAL: EDITABLE MANUALMENTE ---
    // 1. Quitamos @ReadOnly: Ahora tú puedes escribir el total si se pega.
    // 2. Quitamos @Calculation: Para que no haya conflictos visuales.
    @Column(nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal total = BigDecimal.ZERO;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @ListProperties("producto.nombre, cantidad, precioUnitario, subtotal")
    private Collection<FacturaDetalle> detalles;

    // ---------- LÓGICA DE NEGOCIO ----------

    @PrePersist
    private void alCrear() {
        if (mesa == null) {
            throw new ValidationException("Debe seleccionar una mesa.");
        }
        if (estado == null) {
            estado = EstadoFactura.ABIERTA;
        }

        if (mesa.getEstado() != EstadoMesa.RESERVADA) {
            throw new ValidationException("Solo se pueden abrir facturas para mesas RESERVADAS.");
        }

        if (reserva != null) {
            if (!mesa.equals(reserva.getMesa())) {
                throw new ValidationException("La reserva seleccionada no corresponde a la misma mesa.");
            }
            this.fecha = reserva.getFechaHora();
        }

        // Calculamos al guardar para asegurar que la BD tenga el dato correcto
        recalcularTotal();
        actualizarEstadoMesaSegunEstado();
    }

    @PreUpdate
    private void alActualizar() {
        if (reserva != null) {
            if (!mesa.equals(reserva.getMesa())) {
                throw new ValidationException("La reserva seleccionada no corresponde a la misma mesa.");
            }
            this.fecha = reserva.getFechaHora();
        }

        // 1. Recalcular total (Asegura la matemática correcta al guardar)
        recalcularTotal();

        // 2. Descuento de Inventario
        if (this.estado == EstadoFactura.PAGADA) {
            actualizarInventario();
        }

        // 3. Estado Mesa
        actualizarEstadoMesaSegunEstado();
    }

    public void recalcularTotal() {
        BigDecimal suma = BigDecimal.ZERO;
        if (detalles != null) {
            for (FacturaDetalle d : detalles) {
                if (d.getSubtotal() != null) {
                    suma = suma.add(d.getSubtotal());
                }
            }
        }
        this.total = suma;
    }

    private void actualizarEstadoMesaSegunEstado() {
        if (estado == EstadoFactura.PAGADA || estado == EstadoFactura.ANULADA) {
            mesa.setEstado(EstadoMesa.DISPONIBLE);
        } else if (estado == EstadoFactura.ABIERTA) {
            mesa.setEstado(EstadoMesa.OCUPADA);
        }
    }

    private void actualizarInventario() {
        if (detalles == null) return;

        for (FacturaDetalle detalle : detalles) {
            Producto producto = detalle.getProducto();
            int cantidadVendida = detalle.getCantidad();

            try {
                // Mantenemos el FlushMode para evitar el error StackOverflow
                Query query = XPersistence.getManager()
                        .createQuery("from Inventario i where i.producto.oid = :productoOid");

                query.setParameter("productoOid", producto.getOid());
                query.setFlushMode(FlushModeType.COMMIT);

                Inventario inv = (Inventario) query.getSingleResult();
                int nuevaCantidad = inv.getCantidadActual() - cantidadVendida;

                if (nuevaCantidad < 0) {
                    throw new ValidationException("No hay suficiente stock para: " + producto.getNombre());
                }

                inv.setCantidadActual(nuevaCantidad);

            } catch (NoResultException e) {
                throw new ValidationException("No se encontró registro de inventario para el producto: " + producto.getNombre());
            }
        }
    }
}