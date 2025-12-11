package ni.edu.uam.RestauranteElTernero.acciones;

import net.sf.jasperreports.engine.JRDataSource;
import org.openxava.actions.JasperReportBaseAction;

import java.util.HashMap;
import java.util.Map;

public class ImprimirFacturaAction extends JasperReportBaseAction {

    @Override
    protected JRDataSource getDataSource() throws Exception {
        // null => Jasper usará la conexión JDBC de OpenXava
        // y el SELECT lo tienes dentro del propio reporte.
        return null;
    }

    @Override
    protected String getJRXML() throws Exception {
        // Ruta dentro de src/main/resources
        // Ejemplo: src/main/resources/reportes/Factura.jrxml
        return "reportes/Factura.jrxml";
    }

    @Override
    @SuppressWarnings("rawtypes")
    protected Map getParameters() throws Exception {
        Map<String, Object> params = new HashMap<>();

        // OID de la factura que está abierta en pantalla
        String oidFactura = getView().getValueString("oid");
        params.put("P_ID_FACTURA", oidFactura); // mismo nombre que en el reporte

        return params;
    }
}
