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

    public String validarUsuarioPrestamo(String correo) {
        String mensajeError = null;

        String sqlUsuario = "SELECT u.usuario_id, u.nombre, u.correo, u.habilitado, r.rol_id, r.nombre_rol " +
                            "FROM usuarios u " +
                            "JOIN roles r ON u.rol_id = r.rol_id " +
                            "WHERE u.correo = ? AND u.habilitado = TRUE";

        try (Connection conn = Conexion.conectar();
             PreparedStatement psUsuario = conn.prepareStatement(sqlUsuario)) {

            psUsuario.setString(1, correo);
            ResultSet rsUsuario = psUsuario.executeQuery();

            if (!rsUsuario.next()) {
                return "No se encontró el usuario con el correo: " + correo;
            }

            int usuarioId = rsUsuario.getInt("usuario_id");
            String rolNombre = rsUsuario.getString("nombre_rol");

            String sqlMora = "SELECT COUNT(*) AS moras_activas " +
                             "FROM moras WHERE usuario_id = ? AND pagado = FALSE AND habilitado = TRUE";
            try (PreparedStatement psMora = conn.prepareStatement(sqlMora)) {
                psMora.setInt(1, usuarioId);
                ResultSet rsMora = psMora.executeQuery();
                if (rsMora.next() && rsMora.getInt("moras_activas") > 0) {
                    return "El usuario con correo " + correo + " tiene mora pendiente.";
                }
            }

            String sqlPrestamos = "SELECT COUNT(*) AS activos " +
                                  "FROM prestamos WHERE usuario_id = ? AND habilitado = TRUE " +
                                  "AND estado_prestamo_id IN (SELECT estado_prestamo_id FROM estados_prestamo WHERE nombre_estado = 'ACTIVO')";
            int prestamosActivos = 0;
            try (PreparedStatement psPrestamos = conn.prepareStatement(sqlPrestamos)) {
                psPrestamos.setInt(1, usuarioId);
                ResultSet rsPrestamos = psPrestamos.executeQuery();
                if (rsPrestamos.next()) {
                    prestamosActivos = rsPrestamos.getInt("activos");
                }
            }

            String parametro = rolNombre.equalsIgnoreCase("Alumno") ? "MaxPrestamosAlumno" : "MaxPrestamoProfesor";
            String sqlConfig = "SELECT valor_parametro FROM configuraciones_sistema WHERE nombre_parametro = ? AND habilitado = TRUE";
            int maxPrestamos = 0;
            try (PreparedStatement psConfig = conn.prepareStatement(sqlConfig)) {
                psConfig.setString(1, parametro);
                ResultSet rsConfig = psConfig.executeQuery();
                if (rsConfig.next()) {
                    maxPrestamos = Integer.parseInt(rsConfig.getString("valor_parametro"));
                }
            }

            if (prestamosActivos >= maxPrestamos) {
                return "El usuario con correo " + correo + " ya alcanzó el máximo de préstamos permitidos (" + maxPrestamos + ").";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mensajeError = "Error al validar usuario: " + e.getMessage();
        }

        return mensajeError;
    }
    
    public String verificarPrestamosActivosPorCorreo(String correo) {
        String mensajeError = null;

        String sqlUsuario = "SELECT u.usuario_id, u.nombre, r.nombre_rol " +
                            "FROM usuarios u " +
                            "JOIN roles r ON u.rol_id = r.rol_id " +
                            "WHERE u.correo = ? AND u.habilitado = TRUE";

        try (Connection conn = Conexion.conectar();
             PreparedStatement psUsuario = conn.prepareStatement(sqlUsuario)) {

            psUsuario.setString(1, correo);
            ResultSet rsUsuario = psUsuario.executeQuery();

            if (!rsUsuario.next()) {
                return "No se encontró el usuario con el correo: " + correo;
            }

            int usuarioId = rsUsuario.getInt("usuario_id");

            // 🔹 Verificar préstamos activos
            String sqlPrestamos = "SELECT COUNT(*) AS activos " +
                                  "FROM prestamos p " +
                                  "JOIN estados_prestamo ep ON p.estado_prestamo_id = ep.estado_prestamo_id " +
                                  "WHERE p.usuario_id = ? AND p.habilitado = TRUE AND ep.nombre_estado = 'ACTIVO'";

            int prestamosActivos = 0;
            try (PreparedStatement psPrestamos = conn.prepareStatement(sqlPrestamos)) {
                psPrestamos.setInt(1, usuarioId);
                ResultSet rsPrestamos = psPrestamos.executeQuery();
                if (rsPrestamos.next()) {
                    prestamosActivos = rsPrestamos.getInt("activos");
                }
            }

            if (prestamosActivos == 0) {
                return "El usuario con correo " + correo + " no tiene préstamos activos.";
            }

            // 🔹 Verificar mora activa
            String sqlMora = "SELECT COUNT(*) AS moras_activas " +
                             "FROM moras m " +
                             "WHERE m.usuario_id = ? AND m.pagado = FALSE AND m.habilitado = TRUE";

            try (PreparedStatement psMora = conn.prepareStatement(sqlMora)) {
                psMora.setInt(1, usuarioId);
                ResultSet rsMora = psMora.executeQuery();
                if (rsMora.next() && rsMora.getInt("moras_activas") > 0) {
                    return "El usuario con correo " + correo + " tiene mora pendiente.";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mensajeError = "Error al verificar préstamos activos: " + e.getMessage();
        }

        return mensajeError; // null si todo está correcto
    }
    
    public String verificarMoraPorCorreo(String correo) {
        String mensaje = null;

        String sqlUsuario = "SELECT u.usuario_id, u.nombre " +
                            "FROM usuarios u " +
                            "WHERE u.correo = ? AND u.habilitado = TRUE";

        try (Connection conn = Conexion.conectar();
             PreparedStatement psUsuario = conn.prepareStatement(sqlUsuario)) {

            psUsuario.setString(1, correo);
            ResultSet rsUsuario = psUsuario.executeQuery();

            if (!rsUsuario.next()) {
                return "No se encontró el usuario con el correo: " + correo;
            }

            int usuarioId = rsUsuario.getInt("usuario_id");

            // 🔹 Verificar mora activa
            String sqlMora = "SELECT COUNT(*) AS moras_activas " +
                             "FROM moras m " +
                             "WHERE m.usuario_id = ? AND m.pagado = FALSE AND m.habilitado = TRUE";

            try (PreparedStatement psMora = conn.prepareStatement(sqlMora)) {
                psMora.setInt(1, usuarioId);
                ResultSet rsMora = psMora.executeQuery();
                if (rsMora.next() && rsMora.getInt("moras_activas") == 0) {
                    return "El usuario con correo " + correo + " no tiene mora activa.";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            mensaje = "Error al verificar mora: " + e.getMessage();
        }

        return mensaje;
    }
    
    
}
