/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.EstadoPrestamo;
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
public class EstadoPrestamoDAO {
    private static final Logger logger = LogManager.getLogger(EstadoPrestamoDAO.class);

    public boolean insertar(EstadoPrestamo estado) {
        String sql = "INSERT INTO estados_prestamo (nombre_estado, descripcion, habilitado) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado.getNombre());
            ps.setString(2, estado.getDescripcion());
            ps.setBoolean(3, estado.getHabilitado());

            ps.executeUpdate();
            AuditoriaLogger.registrar("CREAR_ESTADO_PRESTAMO", "Se creó el estado de préstamo: " + estado.getNombre());
            return true;

        } catch (SQLException e) {
            logger.error("Error al insertar estado de préstamo: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean actualizar(EstadoPrestamo estado) {
        String sql = "UPDATE estados_prestamo SET nombre_estado = ?, descripcion = ?, habilitado = ? WHERE estado_prestamo_id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado.getNombre());
            ps.setString(2, estado.getDescripcion());
            ps.setBoolean(3, estado.getHabilitado());
            ps.setInt(4, estado.getEstadoPrestamoID());

            ps.executeUpdate();
            AuditoriaLogger.registrar("MODIFICAR_ESTADO_PRESTAMO", "Se modificó el estado de préstamo ID: " + estado.getEstadoPrestamoID());
            return true;

        } catch (SQLException e) {
            logger.error("Error al actualizar estado de préstamo: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean eliminar(Integer id) {
        String sql = "UPDATE estados_prestamo SET habilitado = FALSE WHERE estado_prestamo_id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            AuditoriaLogger.registrar("ELIMINAR_ESTADO_PRESTAMO", "Se deshabilitó el estado de préstamo ID: " + id);
            return true;

        } catch (SQLException e) {
            logger.error("Error al eliminar estado de préstamo: " + e.getMessage(), e);
            return false;
        }
    }

    public List<EstadoPrestamo> listar() {
        List<EstadoPrestamo> lista = new ArrayList<>();
        String sql = "SELECT estado_prestamo_id, nombre_estado, descripcion, habilitado FROM estados_prestamo";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EstadoPrestamo estado = new EstadoPrestamo();
                estado.setEstadoPrestamoID(rs.getInt("estado_prestamo_id"));
                estado.setNombre(rs.getString("nombre_estado"));
                estado.setDescripcion(rs.getString("descripcion"));
                estado.setHabilitado(rs.getBoolean("habilitado"));
                lista.add(estado);
            }

        } catch (SQLException e) {
            logger.error("Error al listar estados de préstamo: " + e.getMessage(), e);
        }
        return lista;
    }
    
    public List<EstadoPrestamo> buscar(String filter) {
        List<EstadoPrestamo> lista = new ArrayList<>();
        String sql = "SELECT estado_prestamo_id, nombre_estado, descripcion, habilitado " +
                     "FROM estados_prestamo " +
                     "WHERE CAST(estado_prestamo_id AS CHAR) LIKE ? " +
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
                    EstadoPrestamo estado = new EstadoPrestamo();
                    estado.setEstadoPrestamoID(rs.getInt("estado_prestamo_id"));
                    estado.setNombre(rs.getString("nombre_estado"));
                    estado.setDescripcion(rs.getString("descripcion"));
                    estado.setHabilitado(rs.getBoolean("habilitado"));
                    lista.add(estado);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar estados de préstamo con filtro '" + filter + "': " + e.getMessage(), e);
        }
        return lista;
    }


    public List<EstadoPrestamo> listarActivos() {
        List<EstadoPrestamo> lista = new ArrayList<>();
        String sql = "SELECT estado_prestamo_id, nombre_estado, descripcion, habilitado " +
                     "FROM estados_prestamo WHERE habilitado = TRUE";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EstadoPrestamo estado = new EstadoPrestamo();
                estado.setEstadoPrestamoID(rs.getInt("estado_prestamo_id"));
                estado.setNombre(rs.getString("nombre_estado"));
                estado.setDescripcion(rs.getString("descripcion"));
                estado.setHabilitado(rs.getBoolean("habilitado"));
                lista.add(estado);
            }

        } catch (SQLException e) {
            logger.error("Error al listar estados de préstamo activos: " + e.getMessage(), e);
        }
        return lista;
    }

}
