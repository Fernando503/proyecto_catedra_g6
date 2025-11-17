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

    public void verificarMorasPendientes() {
        LocalDate hoy = LocalDate.now();

        String sql = "SELECT p.*, ep.nombre_estado " +
                     "FROM prestamos p " +
                     "INNER JOIN estados_prestamo ep ON p.estado_prestamo_id = ep.estado_prestamo_id " +
                     "WHERE ep.nombre_estado = 'En curso' AND p.habilitado = TRUE";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int prestamoId = rs.getInt("prestamo_id");
                int usuarioId = rs.getInt("usuario_id");
                LocalDate fechaPrevista = rs.getDate("fecha_devolucion_prevista").toLocalDate();
                java.sql.Date fechaRealSql = rs.getDate("fecha_devolucion_real");
                LocalDate fechaReal = (fechaRealSql != null) ? fechaRealSql.toLocalDate() : null;

                if (fechaReal == null && hoy.isAfter(fechaPrevista)) {
                    gestionarMora(prestamoId, usuarioId, fechaPrevista, hoy, conn);
                }
            }

        } catch (SQLException e) {
            logger.error("Error al verificar moras pendientes: " + e.getMessage(), e);
        }
    }
    
    private void gestionarMora(int prestamoId, int usuarioId, LocalDate fechaPrevista, LocalDate hoy, Connection conn) throws SQLException {
        long diasRetraso = ChronoUnit.DAYS.between(fechaPrevista, hoy);

        String sqlCheck = "SELECT mora_id FROM moras WHERE prestamo_id = ? AND habilitado = TRUE";
        try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
            psCheck.setInt(1, prestamoId);

            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    int moraId = rs.getInt("mora_id");
                    String sqlUpdate = "UPDATE moras SET dias_retraso = ?, monto = ?, fecha_fin = ? WHERE mora_id = ?";
                    try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                        psUpdate.setInt(1, (int) diasRetraso);
                        psUpdate.setBigDecimal(2, calcularMonto(diasRetraso, conn));
                        psUpdate.setDate(3, java.sql.Date.valueOf(hoy));
                        psUpdate.setInt(4, moraId);
                        psUpdate.executeUpdate();
                    }
                } else {
                    String sqlInsert = "INSERT INTO moras (prestamo_id, usuario_id, fecha_inicio, dias_retraso, monto, pagado, habilitado) " +
                                       "VALUES (?, ?, ?, ?, ?, FALSE, TRUE)";
                    try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                        psInsert.setInt(1, prestamoId);
                        psInsert.setInt(2, usuarioId);
                        psInsert.setDate(3, java.sql.Date.valueOf(fechaPrevista));
                        psInsert.setInt(4, (int) diasRetraso);
                        psInsert.setBigDecimal(5, calcularMonto(diasRetraso, conn));
                        psInsert.executeUpdate();
                    }
                }
            }
        }
    }
    
    private BigDecimal obtenerTarifaMora(Connection conn) throws SQLException {
        String sql = "SELECT valor_parametro FROM configuraciones_sistema " +
                     "WHERE nombre_parametro = 'MoraDiaria' AND habilitado = TRUE";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String valor = rs.getString("valor_parametro");
                return new BigDecimal(valor);
            } else {
                return TARIFA_DIARIA_DEFAULT;
            }
        }
    }
    
    private BigDecimal calcularMonto(long diasRetraso, Connection conn) throws SQLException {
        BigDecimal tarifaPorDia = obtenerTarifaMora(conn);
        return tarifaPorDia.multiply(BigDecimal.valueOf(diasRetraso));
    }


    
    public List<Mora> listarPendientes(Usuario actor) {
        List<Mora> lista = new ArrayList<>();
        String sql = "SELECT m.*, " +
                "       p.prestamo_id, p.fecha_prestamo, p.fecha_devolucion_prevista, p.fecha_devolucion_real, p.observaciones AS prestamo_observaciones, p.habilitado AS prestamo_habilitado, " +
                "       u.usuario_id, u.nombre AS usuario_nombre, u.correo AS usuario_correo, u.habilitado AS usuario_habilitado, " +
                "       e.ejemplar_id, e.codigo_barra, e.fecha_adquisicion, e.observaciones AS ejemplar_observaciones, e.habilitado AS ejemplar_habilitado, " +
                "       d.documento_id, d.titulo " +
                "FROM moras m " +
                "INNER JOIN prestamos p ON m.prestamo_id = p.prestamo_id " +
                "INNER JOIN usuarios u ON m.usuario_id = u.usuario_id " +
                "INNER JOIN ejemplares e ON p.ejemplar_id = e.ejemplar_id " +
                "INNER JOIN documentos d ON e.documento_id = d.documento_id " +
                "WHERE m.pagado = FALSE AND m.habilitado = TRUE";

        boolean actorEsAdmin = actor.getRol() != null && actor.getRol().getNombreRol().equals("Administrador");
        if (!actorEsAdmin) {
            sql += " AND u.usuario_id = ?";
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

    public boolean registrarPago(Integer moraId, BigDecimal montoPagado, Usuario actor) {

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
    
    private Mora mapMora(ResultSet rs) throws SQLException {
        Mora mora = new Mora();

        // --- Mora ---
        mora.setMoraID(rs.getInt("mora_id"));
        mora.setFechaInicio(rs.getDate("fecha_inicio").toLocalDate());
        mora.setFechaFin(rs.getDate("fecha_fin") != null ? rs.getDate("fecha_fin").toLocalDate() : null);
        mora.setDiasRetraso(rs.getInt("dias_retraso"));
        mora.setMonto(rs.getBigDecimal("monto"));
        mora.setPagado(rs.getBoolean("pagado"));
        mora.setObservaciones(rs.getString("observaciones"));
        mora.setHabilitado(rs.getBoolean("habilitado"));

        // --- Usuario ---
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("usuario_id"));
        usuario.setNombre(rs.getString("usuario_nombre"));
        usuario.setCorreo(rs.getString("usuario_correo"));
        usuario.setHabilitado(rs.getBoolean("usuario_habilitado"));
        // Rol y fechaRegistro si los incluyes en el SELECT
        mora.setUsuario(usuario);

        // --- Prestamo ---
        Prestamo prestamo = new Prestamo();
        prestamo.setPrestamoId(rs.getInt("prestamo_id"));
        prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
        prestamo.setFechaDevolucionPrevista(rs.getDate("fecha_devolucion_prevista").toLocalDate());
        if (rs.getDate("fecha_devolucion_real") != null) {
            prestamo.setFechaDevolucionReal(rs.getDate("fecha_devolucion_real").toLocalDate());
        }
        prestamo.setObservaciones(rs.getString("prestamo_observaciones"));
        prestamo.setHabilitado(rs.getBoolean("prestamo_habilitado"));

        // --- Ejemplar ---
        Ejemplar ejemplar = new Ejemplar();
        ejemplar.setEjemplarID(rs.getInt("ejemplar_id"));
        ejemplar.setCodigoBarra(rs.getString("codigo_barra"));
        ejemplar.setFechaAdquisicion(rs.getDate("fecha_adquisicion") != null ? rs.getDate("fecha_adquisicion").toLocalDate() : null);
        ejemplar.setObservaciones(rs.getString("ejemplar_observaciones"));
        ejemplar.setHabilitado(rs.getBoolean("ejemplar_habilitado"));

        // --- Documento ---
        Documento documento = new Documento();
        documento.setDocumentoID(rs.getInt("documento_id"));
        documento.setTitulo(rs.getString("titulo"));

        ejemplar.setDocumento(documento);
        prestamo.setEjemplar(ejemplar);
        prestamo.setUsuario(usuario);

        mora.setPrestamo(prestamo);

        return mora;
    }

    
}
