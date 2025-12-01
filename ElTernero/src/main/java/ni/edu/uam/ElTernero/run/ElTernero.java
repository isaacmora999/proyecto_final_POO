package ni.edu.uam.ElTernero.run;

import org.openxava.util.*;

/**
 * Ejecuta esta clase para arrancar la aplicación.
 *
 * Con OpenXava Studio/Eclipse/IntelliJ:
 * Botón derecho sobre la clase > Run As / Run 'ElTernero.main()'
 */
public class ElTernero {

    public static void main(String[] args) throws Exception {
        // Base de datos embebida (similar a Facturacion-db)
        DBServer.start("ElTernero-db");
        // Nombre de la aplicación OpenXava (lo usaremos también en aplicacion.xml)
        AppServer.run("ElTernero");
    }
}

