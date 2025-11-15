/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.*;
import biblioteca.udb.edu.sv.tools.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class MoraDAO {
    private static final Logger logger = LogManager.getLogger(MoraDAO.class);
    
    private static final BigDecimal TARIFA_DIARIA_DEFAULT = new BigDecimal("0.50");

    public boolean insertar(Mora mora, Usuario actor) {
        if (!RoleManager.tienePermiso("GESTION_MORAS", "AGREGAR")) {
            System.out.println("No tiene permisos para agregar moras.");
            AuditoriaLogger.registrar("PERMISO_DENEGADO", "Intento de crear mora sin permisos por " + actor.getNombre());
            return false;
        }

        // Calcular días y monto si no vienen
        if (mora.getFechaInicio() != null && mora.getFechaFin() != null) {
            int dias = calcularDiasRetraso(mora.getFechaInicio(), mora.getFechaFin());
            mora.setDiasRetraso(dias);
            if (mora.getMonto() == null || mora.getMonto().compareTo(BigDecimal.ZERO) == 0) {
                mora.setMonto(calcularMonto(dias, TARIFA_DIARIA_DEFAULT));
            }
        }

        String sql = "INSERT INTO moras (prestamo_id, usuario_id, fecha_inicio, fecha_fin, dias_retraso, monto, pagado, observaciones, habilitado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, mora.getPrestamo().getPrestamoId());
            ps.setInt(2, mora.getUsuario().getIdUsuario());
            ps.setDate(3, mora.getFechaInicio() != null ? java.sql.Date.valueOf(mora.getFechaInicio()) : null);
            ps.setDate(4, mora.getFechaFin() != null ? java.sql.Date.valueOf(mora.getFechaFin()) : null);
            ps.setInt(5, mora.getDiasRetraso() != null ? mora.getDiasRetraso() : 0);
            ps.setBigDecimal(6, mora.getMonto() != null ? mora.getMonto() : BigDecimal.ZERO);
            ps.setBoolean(7, mora.getPagado() != null ? mora.getPagado() : false);
            ps.setString(8, mora.getObservaciones());
            ps.setBoolean(9, mora.getHabilitado() != null ? mora.getHabilitado() : true);

            ps.executeUpdate();
            AuditoriaLogger.registrar("CREAR_MORA", "Mora creada para usuario " + mora.getUsuario().getIdUsuario() +
                    " por actor " + actor.getNombre());
            return true;

        } catch (SQLException e) {
            logger.error("Error al insertar mora: " + e.getMessage(), e);
            return false;
        }
    }

    // --- ACTUALIZAR ---
    public boolean actualizar(Mora mora, Usuario actor) {
        if (!RoleManager.tienePermiso("GESTION_MORAS", "MODIFICAR")) {
            System.out.println("No tiene permisos para modificar moras.");
            AuditoriaLogger.registrar("PERMISO_DENEGADO", "Intento de modificar mora sin permisos por " + actor.getNombre());
            return false;
        }

        // Recalcular si aplica
        if (mora.getFechaInicio() != null && mora.getFechaFin() != null) {
            int dias = calcularDiasRetraso(mora.getFechaInicio(), mora.getFechaFin());
            mora.setDiasRetraso(dias);
            if (mora.getMonto() == null || mora.getMonto().compareTo(BigDecimal.ZERO) == 0) {
                mora.setMonto(calcularMonto(dias, TARIFA_DIARIA_DEFAULT));
            }
        }

        String sql = "UPDATE moras SET fecha_inicio = ?, fecha_fin = ?, dias_retraso = ?, monto = ?, pagado = ?, observaciones = ?, habilitado = ? WHERE mora_id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, mora.getFechaInicio() != null ? java.sql.Date.valueOf(mora.getFechaInicio()) : null);
            ps.setDate(2, mora.getFechaFin() != null ? java.sql.Date.valueOf(mora.getFechaFin()) : null);
            ps.setInt(3, mora.getDiasRetraso() != null ? mora.getDiasRetraso() : 0);
            ps.setBigDecimal(4, mora.getMonto() != null ? mora.getMonto() : BigDecimal.ZERO);
            ps.setBoolean(5, mora.getPagado() != null ? mora.getPagado() : false);
            ps.setString(6, mora.getObservaciones());
            ps.setBoolean(7, mora.getHabilitado() != null ? mora.getHabilitado() : true);
            ps.setInt(8, mora.getMoraID());

            ps.executeUpdate();
            AuditoriaLogger.registrar("MODIFICAR_MORA", "Se modificó mora ID: " + mora.getMoraID() + " por " + actor.getNombre());
            return true;

        } catch (SQLException e) {
            logger.error("Error al actualizar mora: " + e.getMessage(), e);
            return false;
        }
    }

    // --- ELIMINAR (soft delete) ---
    public boolean eliminar(Integer id, Usuario actor) {
        if (!RoleManager.tienePermiso("GESTION_MORAS", "ELIMINAR")) {
            System.out.println("No tiene permisos para eliminar moras.");
            AuditoriaLogger.registrar("PERMISO_DENEGADO", "Intento de eliminar mora sin permisos por " + actor.getNombre());
            return false;
        }

        String sql = "UPDATE moras SET habilitado = FALSE WHERE mora_id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            AuditoriaLogger.registrar("ELIMINAR_MORA", "Se deshabilitó mora ID: " + id + " por " + actor.getNombre());
            return true;

        } catch (SQLException e) {
            logger.error("Error al eliminar mora: " + e.getMessage(), e);
            return false;
        }
    }

    // --- OBTENER POR ID ---
    public Mora obtenerPorId(Integer moraId) {
        String sql = "SELECT * FROM moras WHERE mora_id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, moraId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapMora(rs);
                }
            }

        } catch (SQLException e) {
            logger.error("Error al obtener mora por ID: " + e.getMessage(), e);
        }
        return null;
    }

    // --- LISTAR TODAS ---
    public List<Mora> listar() {
        List<Mora> lista = new ArrayList<>();
        String sql = "SELECT * FROM moras";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapMora(rs));
            }

        } catch (SQLException e) {
            logger.error("Error al listar moras: " + e.getMessage(), e);
        }
        return lista;
    }

    // --- LISTAR ACTIVAS ---
    public List<Mora> listarActivas() {
        List<Mora> lista = new ArrayList<>();
        String sql = "SELECT * FROM moras WHERE habilitado = TRUE";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapMora(rs));
            }

        } catch (SQLException e) {
            logger.error("Error al listar moras activas: " + e.getMessage(), e);
        }
        return lista;
    }

    // --- LISTAR POR USUARIO (con control por rol) ---
    public List<Mora> listarPorUsuario(Integer usuarioIdSolicitado, Usuario actor) {
        List<Mora> lista = new ArrayList<>();

        boolean actorEsAdmin = actor.getRol() != null && actor.getRol().equals("ADMIN");
        if (!actorEsAdmin && actor.getIdUsuario() != usuarioIdSolicitado) {
            System.out.println("No tiene permisos para ver moras de otros usuarios.");
            return lista;
        }

        String sql = "SELECT * FROM moras WHERE usuario_id = ? AND habilitado = TRUE";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioIdSolicitado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapMora(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("Error al listar moras por usuario: " + e.getMessage(), e);
        }
        return lista;
    }

    // --- LISTAR PENDIENTES ---
    public List<Mora> listarPendientes(Usuario actor) {
        List<Mora> lista = new ArrayList<>();
        String sql;

        // Admin ve todas las pendientes; alumno/profesor solo las propias
        boolean actorEsAdmin = actor.getRol() != null && actor.getRol().equals("ADMIN");
        if (actorEsAdmin) {
            sql = "SELECT * FROM moras WHERE pagado = FALSE AND habilitado = TRUE";
        } else {
            sql = "SELECT * FROM moras WHERE pagado = FALSE AND habilitado = TRUE AND usuario_id = ?";
        }

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (!actorEsAdmin) {
                ps.setInt(1, actor.getIdUsuario());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapMora(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("Error al listar moras pendientes: " + e.getMessage(), e);
        }
        return lista;
    }

    // --- REGISTRAR PAGO ---
    public boolean registrarPago(Integer moraId, BigDecimal montoPagado, Usuario actor) {
        if (!RoleManager.tienePermiso("GESTION_MORAS", "MODIFICAR")) {
            System.out.println("No tiene permisos para registrar pagos de moras.");
            AuditoriaLogger.registrar("PERMISO_DENEGADO", "Intento de registrar pago de mora sin permisos por " + actor.getNombre());
            return false;
        }

        String sql = "UPDATE moras SET pagado = TRUE, monto = ?, observaciones = CONCAT(COALESCE(observaciones,''), ?) WHERE mora_id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setBigDecimal(1, montoPagado != null ? montoPagado : BigDecimal.ZERO);
            ps.setString(2, " | Pago registrado por " + actor.getNombre() + " en " + LocalDate.now());
            ps.setInt(3, moraId);

            ps.executeUpdate();
            AuditoriaLogger.registrar("PAGAR_MORA", "Se registró pago de mora ID: " + moraId + " por " + actor.getNombre());
            return true;

        } catch (SQLException e) {
            logger.error("Error al registrar pago de mora: " + e.getMessage(), e);
            return false;
        }
    }

    // --- LISTAR POR PERIODO ---
    public List<Mora> listarPorPeriodo(LocalDate inicio, LocalDate fin, Usuario actor) {
        List<Mora> lista = new ArrayList<>();
        boolean actorEsAdmin = actor.getRol() != null && actor.getRol().equals("ADMIN");

        String sql = actorEsAdmin
                ? "SELECT * FROM moras WHERE fecha_inicio >= ? AND fecha_fin <= ? AND habilitado = TRUE"
                : "SELECT * FROM moras WHERE fecha_inicio >= ? AND fecha_fin <= ? AND habilitado = TRUE AND usuario_id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDate(1, inicio != null ? java.sql.Date.valueOf(inicio) : null);
            ps.setDate(2, fin != null ? java.sql.Date.valueOf(fin) : null);
            if (!actorEsAdmin) {
                ps.setInt(3, actor.getIdUsuario());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapMora(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("Error al listar moras por periodo: " + e.getMessage(), e);
        }
        return lista;
    }

    // --- LISTAR MORAS VENCIDAS (no pagadas y fecha_fin pasada) ---
    public List<Mora> listarMorasVencidas(Usuario actor) {
        List<Mora> lista = new ArrayList<>();
        boolean actorEsAdmin = actor.getRol() != null && actor.getRol().equals("ADMIN");

        String sql = actorEsAdmin
                ? "SELECT * FROM moras WHERE pagado = FALSE AND fecha_fin IS NOT NULL AND fecha_fin < CURDATE() AND habilitado = TRUE"
                : "SELECT * FROM moras WHERE pagado = FALSE AND fecha_fin IS NOT NULL AND fecha_fin < CURDATE() AND habilitado = TRUE AND usuario_id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (!actorEsAdmin) {
                ps.setInt(1, actor.getIdUsuario());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapMora(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("Error al listar moras vencidas: " + e.getMessage(), e);
        }
        return lista;
    }

    // --- LISTAR POR PRÉSTAMO ---
    public List<Mora> listarPorPrestamo(Integer prestamoId) {
        List<Mora> lista = new ArrayList<>();
        String sql = "SELECT * FROM moras WHERE prestamo_id = ? AND habilitado = TRUE";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, prestamoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapMora(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("Error al listar moras por préstamo: " + e.getMessage(), e);
        }
        return lista;
    }

    // --- TOTAL MORAS PENDIENTES POR USUARIO ---
    public BigDecimal totalMorasPendientesPorUsuario(Integer usuarioId) {
        String sql = "SELECT COALESCE(SUM(monto), 0) AS total FROM moras WHERE usuario_id = ? AND pagado = FALSE AND habilitado = TRUE";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, usuarioId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }

        } catch (SQLException e) {
            logger.error("Error al calcular total de moras pendientes por usuario: " + e.getMessage(), e);
        }
        return BigDecimal.ZERO;
    }

    // --- UTILIDAD: calcular días de retraso ---
    public int calcularDiasRetraso(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null || !fin.isAfter(inicio)) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(inicio, fin);
    }

    // --- UTILIDAD: calcular monto por días y tarifa ---
    public BigDecimal calcularMonto(int diasRetraso, BigDecimal tarifaDiaria) {
        if (diasRetraso <= 0) return BigDecimal.ZERO;
        if (tarifaDiaria == null || tarifaDiaria.compareTo(BigDecimal.ZERO) <= 0) {
            tarifaDiaria = TARIFA_DIARIA_DEFAULT;
        }
        return tarifaDiaria.multiply(BigDecimal.valueOf(diasRetraso)).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    // --- Mapper de ResultSet a Mora (modelo expresivo) ---
    private Mora mapMora(ResultSet rs) throws SQLException {
        Mora m = new Mora();
        m.setMoraID(rs.getInt("mora_id"));

        // Relaciones (solo IDs aquí; puedes cargar objetos completos en la capa de servicio usando DAOs correspondientes)
        Prestamo p = new Prestamo();
        p.setPrestamoId(rs.getInt("prestamo_id"));
        m.setPrestamo(p);

        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("usuario_id"));
        m.setUsuario(u);

        java.sql.Date fi = rs.getDate("fecha_inicio");
        java.sql.Date ff = rs.getDate("fecha_fin");
        m.setFechaInicio(fi != null ? fi.toLocalDate() : null);
        m.setFechaFin(ff != null ? ff.toLocalDate() : null);

        m.setDiasRetraso(rs.getInt("dias_retraso"));
        m.setMonto(rs.getBigDecimal("monto"));
        m.setPagado(rs.getBoolean("pagado"));
        m.setObservaciones(rs.getString("observaciones"));
        m.setHabilitado(rs.getBoolean("habilitado"));
        return m;
    }

    
}
