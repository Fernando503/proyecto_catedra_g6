/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.EstadoEjemplar;
import biblioteca.udb.edu.sv.tools.AuditoriaLogger;
import biblioteca.udb.edu.sv.tools.Conexion;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class EstadoEjemplarDAO {
    private static final Logger logger = LogManager.getLogger(EstadoEjemplarDAO.class);

    public boolean insertar(EstadoEjemplar estado) {
        String sql = "INSERT INTO estados_ejemplar (nombre_estado, descripcion, habilitado) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado.getNombre());
            ps.setString(2, estado.getDescripcion());
            ps.setBoolean(3, estado.getHabilitado());

            ps.executeUpdate();

            AuditoriaLogger.registrar("CREAR_ESTADO_EJEMPLAR", "Se creó el estado de ejemplar: " + estado.getNombre());
            return true;

        } catch (SQLException e) {
            logger.error("Error al insertar estado de ejemplar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(EstadoEjemplar estado) {
        String sql = "UPDATE estados_ejemplar SET nombre_estado = ?, descripcion = ?, habilitado = ? WHERE estado_ejemplar_id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado.getNombre());
            ps.setString(2, estado.getDescripcion());
            ps.setBoolean(3, estado.getHabilitado());
            ps.setInt(4, estado.getEstadoEjemplarID());

            ps.executeUpdate();

            AuditoriaLogger.registrar("MODIFICAR_ESTADO_EJEMPLAR", "Se modificó el estado de ejemplar ID: " + estado.getEstadoEjemplarID());
            return true;

        } catch (SQLException e) {
            logger.error("Error al actualizar estado de ejemplar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(Integer id) {
        String sql = "UPDATE estados_ejemplar SET habilitado = FALSE WHERE estado_ejemplar_id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            AuditoriaLogger.registrar("ELIMINAR_ESTADO_EJEMPLAR", "Se deshabilitó el estado de ejemplar ID: " + id);
            return true;

        } catch (SQLException e) {
            logger.error("Error al eliminar estado de ejemplar: " + e.getMessage());
            return false;
        }
    }

    public List<EstadoEjemplar> listar() {
        List<EstadoEjemplar> lista = new ArrayList<>();
        String sql = "SELECT estado_ejemplar_id, nombre_estado, descripcion, habilitado FROM estados_ejemplar";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EstadoEjemplar estado = new EstadoEjemplar();
                estado.setEstadoEjemplarID(rs.getInt("estado_ejemplar_id"));
                estado.setNombre(rs.getString("nombre_estado"));
                estado.setDescripcion(rs.getString("descripcion"));
                estado.setHabilitado(rs.getBoolean("habilitado"));
                lista.add(estado);
            }

        } catch (SQLException e) {
            logger.error("Error al listar estados de ejemplar: " + e.getMessage());
        }
        return lista;
    }

    public List<EstadoEjemplar> buscar(String filter) {
        List<EstadoEjemplar> lista = new ArrayList<>();
        String sql = "SELECT estado_ejemplar_id, nombre_estado, descripcion, habilitado " +
                     "FROM estados_ejemplar " +
                     "WHERE CAST(estado_ejemplar_id AS CHAR) LIKE ? " +
                     "   OR nombre_estado LIKE ? " +
                     "   OR descripcion LIKE ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String filtro = "%" + filter + "%";
            ps.setString(1, filtro);
            ps.setString(2, filtro);
            ps.setString(3, filtro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    EstadoEjemplar estado = new EstadoEjemplar();
                    estado.setEstadoEjemplarID(rs.getInt("estado_ejemplar_id"));
                    estado.setNombre(rs.getString("nombre_estado"));
                    estado.setDescripcion(rs.getString("descripcion"));
                    estado.setHabilitado(rs.getBoolean("habilitado"));
                    lista.add(estado);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar estado de ejemplar con filtro '" + filter + "': " + e.getMessage());
        }
        return lista;
    }

    public List<EstadoEjemplar> listarActivos() {
        List<EstadoEjemplar> lista = new ArrayList<>();
        String sql = "SELECT estado_ejemplar_id, nombre_estado, descripcion, habilitado FROM estados_ejemplar WHERE habilitado = TRUE";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EstadoEjemplar estado = new EstadoEjemplar();
                estado.setEstadoEjemplarID(rs.getInt("estado_ejemplar_id"));
                estado.setNombre(rs.getString("nombre_estado"));
                estado.setDescripcion(rs.getString("descripcion"));
                estado.setHabilitado(rs.getBoolean("habilitado"));
                lista.add(estado);
            }

        } catch (SQLException e) {
            logger.error("Error al listar estados de ejemplar activos: " + e.getMessage());
        }
        return lista;
    }

}
