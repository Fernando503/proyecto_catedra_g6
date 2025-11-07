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
 */
public class Conexion {
    private static final Logger logger = LogManager.getLogger(Conexion.class);
    private static Connection conexion = null;
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL ="jdbc:mysql://localhost:8501/desafio_2";
    private static final String USUARIO = "root";
    public static final String CLAVE = "root_password";
    
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
