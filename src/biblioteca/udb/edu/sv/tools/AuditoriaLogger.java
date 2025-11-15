/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.tools;

import biblioteca.udb.edu.sv.entidades.Usuario;
import java.sql.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class AuditoriaLogger {
    private static final Logger logger = LogManager.getLogger(AuditoriaLogger.class);
    
    public static void registrar(String tipoAccion, String detalleAccion) {
        Usuario usuarioActual = obtenerUsuarioActual();
        if (usuarioActual == null) return;

        String sql = "INSERT INTO historial_acciones (usuario_id, tipo_accion, detalle_accion) VALUES (?, ?, ?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioActual.getIdUsuario());
            ps.setString(2, tipoAccion);
            ps.setString(3, detalleAccion);

            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error al registrar historial: " + e.getMessage());
        }
    }

    private static Usuario obtenerUsuarioActual() {
        SesionUsuario sesion = SesionUsuario.getInstancia();
        if (sesion.getIdUsuario() == 0) return null;

        Usuario u = new Usuario();
        u.setIdUsuario(sesion.getIdUsuario());
        u.setNombre(sesion.getNombre());
        return u;
    }

}
