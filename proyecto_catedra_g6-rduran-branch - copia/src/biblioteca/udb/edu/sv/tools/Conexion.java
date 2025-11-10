package biblioteca.udb.edu.sv.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca_udb";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = ""; // si tienes contraseña, ponla aquí
    private static Connection conexion = null;

    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA);
                System.out.println("✅ Conexión establecida con la base de datos.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
        }
        return conexion;
    }
}
