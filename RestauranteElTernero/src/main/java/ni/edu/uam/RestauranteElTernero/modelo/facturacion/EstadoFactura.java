package ni.edu.uam.RestauranteElTernero.modelo.facturacion;

public enum EstadoFactura {
    ABIERTA,    // Clientes sentados, consumiendo
    PAGADA,     // Cuenta pagada
    ANULADA     // Se anuló por error, devolución, etc.
}
