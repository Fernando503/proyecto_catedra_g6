/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.*;
import biblioteca.udb.edu.sv.tools.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class PrestamoDAO {
    private static final Logger logger = LogManager.getLogger(PrestamoDAO.class);

    private boolean ejemplarDisponible(Integer ejemplarId) {
        String sql = "SELECT COUNT(*) FROM prestamos " +
                     "WHERE ejemplar_id = ? AND fecha_devolucion_real IS NULL AND habilitado = TRUE";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ejemplarId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error al validar disponibilidad de ejemplar: " + e.getMessage(), e);
        }
        return false;
    }

    public boolean insertar(Prestamo prestamo, Usuario admin) {
        if (!ejemplarDisponible(prestamo.getEjemplar().getEjemplarID())) {
            logger.warn("El ejemplar ya está prestado.");
            return false;
        }

        if (RoleManager.tienePermiso("GESTION_PRESTAMOS", "AGREGAR")) {
            logger.warn("");
            return false;
        }

        String sql = "INSERT INTO prestamos (usuario_id, ejemplar_id, fecha_prestamo, " +
                     "fecha_devolucion_prevista, estado_prestamo_id, observaciones, habilitado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, prestamo.getUsuario().getIdUsuario());
            ps.setInt(2, prestamo.getEjemplar().getEjemplarID());
            ps.setDate(3, java.sql.Date.valueOf(prestamo.getFechaPrestamo() != null ? prestamo.getFechaPrestamo() : LocalDate.now()));
            ps.setDate(4, java.sql.Date.valueOf(prestamo.getFechaDevolucionPrevista()));
            ps.setInt(5, prestamo.getEstadoPrestamo() != null ? prestamo.getEstadoPrestamo().getEstadoPrestamoID(): null);
            ps.setString(6, prestamo.getObservaciones());
            ps.setBoolean(7, prestamo.getHabilitado());

            ps.executeUpdate();

            AuditoriaLogger.registrar("CREAR_PRESTAMO",
                    "Admin " + admin.getNombre() + " registró préstamo para usuario " + prestamo.getUsuario().getNombre());
            return true;

        } catch (SQLException e) {
            logger.error("Error al insertar préstamo: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean registrarDevolucion(Integer prestamoId, LocalDate fechaDevolucionReal) {
        String sql = "UPDATE prestamos SET fecha_devolucion_real = ?, estado_prestamo_id = ?, observaciones = ? " +
                     "WHERE prestamo_id = ? AND habilitado = TRUE";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(fechaDevolucionReal));
            ps.setInt(2, 2); // Ejemplo: estado 2 = DEVUELTO
            ps.setString(3, "Devolución registrada en fecha " + fechaDevolucionReal);
            ps.setInt(4, prestamoId);

            ps.executeUpdate();

            AuditoriaLogger.registrar("DEVOLVER_PRESTAMO", "Se registró devolución del préstamo ID: " + prestamoId);
            return true;

        } catch (SQLException e) {
            logger.error("Error al registrar devolución: " + e.getMessage(), e);
            return false;
        }
    }

    public List<Prestamo> listarPorRol(Usuario usuario) {
        List<Prestamo> lista = new ArrayList<>();
        String sql;

        if (usuario.getRol().getRolID().equals(1)) {
            sql = "SELECT * FROM prestamos WHERE habilitado = TRUE";
        } else {
            sql = "SELECT * FROM prestamos WHERE usuario_id = ? AND habilitado = TRUE";
        }

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (!usuario.getRol().getRolID().equals(1)) {
                ps.setInt(1, usuario.getIdUsuario());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo p = new Prestamo();
                    p.setPrestamoId(rs.getInt("prestamo_id"));
                    p.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
                    p.setFechaDevolucionPrevista(rs.getDate("fecha_devolucion_prevista").toLocalDate());
                    if (rs.getDate("fecha_devolucion_real") != null) {
                        p.setFechaDevolucionReal(rs.getDate("fecha_devolucion_real").toLocalDate());
                    }
                    p.setObservaciones(rs.getString("observaciones"));
                    p.setHabilitado(rs.getBoolean("habilitado"));
                    lista.add(p);
                }
            }

        } catch (SQLException e) {
            logger.error("Error al listar préstamos: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Prestamo> listarVencidos() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE fecha_devolucion_real IS NULL " +
                     "AND fecha_devolucion_prevista < CURDATE() AND habilitado = TRUE";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Prestamo p = new Prestamo();
                p.setPrestamoId(rs.getInt("prestamo_id"));
                p.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
                p.setFechaDevolucionPrevista(rs.getDate("fecha_devolucion_prevista").toLocalDate());
                p.setObservaciones("Prestamo vencido");
                lista.add(p);
            }

        } catch (SQLException e) {
            logger.error("Error al listar préstamos vencidos: " + e.getMessage(), e);
        }
        return lista;
    }

    public List<Prestamo> listarHistorialPorUsuario(Integer usuarioId) {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM prestamos WHERE usuario_id = ? AND habilitado = TRUE";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Prestamo p = new Prestamo();
                    p.setPrestamoId(rs.getInt("prestamo_id"));
                    p.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
                    p.setFechaDevolucionPrevista(rs.getDate("fecha_devolucion_prevista").toLocalDate());
                    if (rs.getDate("fecha_devolucion_real") != null) {
                        p.setFechaDevolucionReal(rs.getDate("fecha_devolucion_real").toLocalDate());
                    }
                    p.setObservaciones(rs.getString("observaciones"));
                    lista.add(p);
                }
            }

        } catch (SQLException e) {
            logger.error("Error al listar historial de préstamos: " + e.getMessage(), e);
        }
        return lista;
    }

}
