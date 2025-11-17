/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.*;
import biblioteca.udb.edu.sv.tools.Conexion;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class AuditoriaDAO {
    private static final Logger logger = LogManager.getLogger(AuditoriaDAO.class);
    
    public List<Auditoria> listarAuditoria() {
    List<Auditoria> lista = new ArrayList<>();

    String sql = "SELECT h.hist_id, h.usuario_id, h.fecha_accion, h.tipo_accion, h.detalle_accion, " +
                 "u.nombre, u.correo " +
                 "FROM historial_acciones h " +
                 "INNER JOIN usuarios u ON h.usuario_id = u.usuario_id " +
                 "ORDER BY h.fecha_accion DESC";

    try (Connection conn = Conexion.conectar();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Auditoria a = new Auditoria();
            a.setIdAuditoria(rs.getInt("hist_id"));
            a.setFechaAccion(rs.getTimestamp("fecha_accion").toLocalDateTime());
            a.setTipoAccion(rs.getString("tipo_accion"));
            a.setDetalleAccion(rs.getString("detalle_accion"));

            Usuario u = new Usuario();
            u.setIdUsuario(rs.getInt("usuario_id"));
            u.setNombre(rs.getString("nombre"));
            u.setCorreo(rs.getString("correo"));

            a.setUsuario(u);
            lista.add(a);
        }

    } catch (SQLException e) {
        logger.error("Error al listar auditoría: " + e.getMessage());
    }

    return lista;
}
}
