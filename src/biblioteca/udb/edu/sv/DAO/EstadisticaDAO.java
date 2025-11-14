/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.Estadistica;
import biblioteca.udb.edu.sv.tools.Conexion;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.sql.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class EstadisticaDAO {
    private static final Logger logger = LogManager.getLogger(EstadisticaDAO.class);
    public Estadistica obtenerEstadisticas(Integer usuarioId, boolean esAdmin) {
        Estadistica estadistica = new Estadistica();

        String sqlDocumentos = "SELECT COUNT(*) FROM documentos WHERE habilitado = TRUE";
        String sqlEjemplares = "SELECT COUNT(*) FROM ejemplares WHERE habilitado = TRUE";
        String sqlPrestamos = esAdmin
            ? "SELECT COUNT(*) FROM prestamos WHERE habilitado = TRUE AND estado_prestamo_id IN (SELECT estado_prestamo_id FROM estados_prestamo WHERE nombre_estado = 'Activo')"
            : "SELECT COUNT(*) FROM prestamos WHERE habilitado = TRUE AND usuario_id = ? AND estado_prestamo_id IN (SELECT estado_prestamo_id FROM estados_prestamo WHERE nombre_estado = 'Activo')";
        String sqlMoras = esAdmin
            ? "SELECT COUNT(*) FROM moras WHERE habilitado = TRUE AND pagado = FALSE"
            : "SELECT COUNT(*) FROM moras WHERE habilitado = TRUE AND pagado = FALSE AND usuario_id = ?";

        try (Connection conn = Conexion.conectar()) {

            try (PreparedStatement ps = conn.prepareStatement(sqlDocumentos);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) estadistica.setTotalDocumentos(rs.getInt(1));
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlEjemplares);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) estadistica.setTotalEjemplares(rs.getInt(1));
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlPrestamos)) {
                if (!esAdmin) ps.setInt(1, usuarioId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) estadistica.setPrestamosActivos(rs.getInt(1));
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlMoras)) {
                if (!esAdmin) ps.setInt(1, usuarioId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) estadistica.setMorasPendientes(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            logger.error("Error al obtener estadísticas: " + e.getMessage());
        }

        return estadistica;
    }

}
