/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;


/**
 * Entidad: Universidad Don Bosco
 * @author Fernando Flamenco
 * IMPORTANTE: En esta clase no se debe de tocar los valores, todo se configura en el archivo de variables de entorno (.env)
 */
public class Conexion {
    private static final Logger logger = LogManager.getLogger(Conexion.class);
    private static Connection conexion = null;

    private static final String DRIVER = ConfigLoader.get("DB_DRIVER");
    private static final String URL = ConfigLoader.get("DB_URL");
    private static final String USUARIO = ConfigLoader.get("DB_USER");
    public static final String CLAVE = ConfigLoader.get("DB_PASS");

    
    public static Connection conectar() {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USUARIO, CLAVE);
        } catch (ClassNotFoundException e) {
            logger.error("Error de contralador JDBC: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error: No se encontró el controlador de la base de datos.\n" + e.getMessage());
        } catch (SQLException e) {
            logger.error("Error de conexión: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos.\n" + e.getMessage());
        }
        return null;
    }

    
    public static void desconectar() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                logger.info("Conexión cerrada.");
            } catch (SQLException e) {
                logger.error("Error de cierre de conexión: " + e.getMessage());
            }
        }
    }
}