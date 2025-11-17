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
                     "WHERE ejemplar_id = ? AND fecha_devolucion_real IS NULL AND habilitado = TRUE AND estado_prestamo_id = 1";
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

    private boolean insertar(Prestamo prestamo, Usuario admin, Connection conn) {
        if (!ejemplarDisponible(prestamo.getEjemplar().getEjemplarID())) {
            logger.warn("El ejemplar ya está prestado.");
            return false;
        }
        String sql = "INSERT INTO prestamos (usuario_id, ejemplar_id, fecha_prestamo, " +
                     "fecha_devolucion_prevista, estado_prestamo_id, observaciones, habilitado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, prestamo.getUsuario().getIdUsuario());
            ps.setInt(2, prestamo.getEjemplar().getEjemplarID());
            ps.setDate(3, java.sql.Date.valueOf(prestamo.getFechaPrestamo() != null ? prestamo.getFechaPrestamo() : LocalDate.now()));
            ps.setDate(4, java.sql.Date.valueOf(prestamo.getFechaDevolucionPrevista()));
            ps.setInt(5, prestamo.getEstadoPrestamo() != null ? prestamo.getEstadoPrestamo().getEstadoPrestamoID(): null);
            ps.setString(6, prestamo.getObservaciones());
            ps.setBoolean(7, prestamo.getHabilitado());

            ps.executeUpdate();

            String msgAudi = "";
            if(admin != null){
                msgAudi =  "Admin " + admin.getNombre() + " registró préstamo para usuario " + prestamo.getUsuario().getNombre();
            }else{
                msgAudi = "Alumno registró préstamo para usuario " + prestamo.getUsuario().getNombre();
            }
            
            AuditoriaLogger.registrar("CREAR_PRESTAMO",msgAudi);
            return true;

        } catch (SQLException e) {
            logger.error("Error al insertar préstamo: " + e.getMessage(), e);
            return false;
        }
    }

    public boolean registrarDevolucion(int prestamoId, LocalDate fechaDevolucionReal, int estadoPrestamo) {
        String sqlPrestamo = "UPDATE prestamos SET fecha_devolucion_real = ?, estado_prestamo_id = ?, observaciones = ? " +
                             "WHERE prestamo_id = ? AND habilitado = TRUE";

        String sqlGetEjemplar = "SELECT ejemplar_id FROM prestamos WHERE prestamo_id = ? AND habilitado = TRUE";
        String sqlUpdateEjemplar = "UPDATE ejemplares SET estado_ejemplar_id = ? WHERE ejemplar_id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement psPrestamo = conn.prepareStatement(sqlPrestamo);
             PreparedStatement psGetEjemplar = conn.prepareStatement(sqlGetEjemplar);
             PreparedStatement psUpdateEjemplar = conn.prepareStatement(sqlUpdateEjemplar)) {

            conn.setAutoCommit(false);

            psPrestamo.setDate(1, java.sql.Date.valueOf(fechaDevolucionReal));
            psPrestamo.setInt(2, estadoPrestamo);
            psPrestamo.setString(3, "Devolución registrada en fecha " + fechaDevolucionReal);
            psPrestamo.setInt(4, prestamoId);
            psPrestamo.executeUpdate();

            int idEjemplar = -1;
            psGetEjemplar.setInt(1, prestamoId);
            try (ResultSet rs = psGetEjemplar.executeQuery()) {
                if (rs.next()) {
                    idEjemplar = rs.getInt("ejemplar_id");
                }
            }

            if (idEjemplar == -1) {
                conn.rollback();
                logger.error("No se encontró ejemplar asociado al préstamo ID: " + prestamoId);
                return false;
            }

            psUpdateEjemplar.setInt(1, 1);
            psUpdateEjemplar.setInt(2, idEjemplar);
            psUpdateEjemplar.executeUpdate();

            conn.commit();
            AuditoriaLogger.registrar("DEVOLVER_PRESTAMO", 
                "Se registró devolución del préstamo ID: " + prestamoId + " y se actualizó ejemplar ID: " + idEjemplar);
            return true;

        } catch (SQLException e) {
            logger.error("Error al registrar devolución: " + e.getMessage(), e);
            return false;
        }
    }

    public List<Prestamo> listarPorRol(Usuario usuario) {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT p.*, u.usuario_id, u.nombre AS usuario_nombre, u.correo AS usuario_correo, " +
                "       r.rol_id, r.nombre_rol, e.ejemplar_id, e.ubicacion_id, e.estado_ejemplar_id, e.fecha_adquisicion," +
                "       d.documento_id, d.titulo, d.autor, d.tipo_documento_id, d.categoria_id, d.editorial_id," +
                "       d.idioma, d.formato, d.anio_publicacion, d.numero_paginas," +
                "       ub.sala, ub.estanteria, ub.nivel, ub.codigo_rack, ub.descripcion AS ubicacion_descripcion, ub.habilitado AS ubicacion_habilitado," +
                "       es.nombre_estado AS estado_ejemplar_nombre, es.descripcion AS estado_ejemplar_descripcion, es.habilitado AS estado_ejemplar_habilitado," +
                "       ep.nombre_estado AS estado_prestamo_nombre, ep.descripcion AS estado_prestamo_descripcion, ep.habilitado AS estado_prestamo_habilitado " +
                "FROM prestamos p " +
                "JOIN usuarios u ON p.usuario_id = u.usuario_id " +
                "LEFT JOIN roles r ON u.rol_id = r.rol_id " +
                "JOIN ejemplares e ON p.ejemplar_id = e.ejemplar_id " +
                "JOIN documentos d ON e.documento_id = d.documento_id " +
                "LEFT JOIN ubicaciones ub ON e.ubicacion_id = ub.ubicacion_id " +
                "LEFT JOIN estados_ejemplar es ON e.estado_ejemplar_id = es.estado_ejemplar_id " +
                "LEFT JOIN estados_prestamo ep ON p.estado_prestamo_id = ep.estado_prestamo_id";

        boolean actorEsAdmin = usuario.getRol() != null && usuario.getRol().getNombreRol().equals("Administrador");
        if (!actorEsAdmin) {
            sql += " WHERE u.usuario_id = ?";
        }

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (!actorEsAdmin) {
                ps.setInt(1, usuario.getIdUsuario());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                     lista.add(mapResultSet(rs));
                }
            }

        } catch (SQLException e) {
            logger.error("Error al listar préstamos: " + e.getMessage(), e);
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
                                  "FROM prestamos p " +
                                  "JOIN estados_prestamo ep ON p.estado_prestamo_id = ep.estado_prestamo_id " +
                                  "WHERE p.usuario_id = ? AND p.habilitado = TRUE AND ep.nombre_estado = 'En curso'";
            
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
            
            StoreUserPrestamo.getInstancia().DatosPrestamosSesion(rsUsuario.getInt("usuario_id"), rsUsuario.getString("nombre"), rsUsuario.getString("correo"));

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
                                  "WHERE p.usuario_id = ? AND p.habilitado = TRUE AND ep.nombre_estado = 'En curso'";

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
    
    public String verificarPrestamosByCorreo(String correo) {
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
            String nombreRol = rsUsuario.getString("nombre_rol");

            String sqlPrestamos = "SELECT COUNT(*) AS activos " +
                                  "FROM prestamos p " +
                                  "JOIN estados_prestamo ep ON p.estado_prestamo_id = ep.estado_prestamo_id " +
                                  "WHERE p.usuario_id = ? AND p.habilitado = TRUE AND ep.nombre_estado = 'En curso'";

            int prestamosActivos = 0;
            try (PreparedStatement psPrestamos = conn.prepareStatement(sqlPrestamos)) {
                psPrestamos.setInt(1, usuarioId);
                ResultSet rsPrestamos = psPrestamos.executeQuery();
                if (rsPrestamos.next()) {
                    prestamosActivos = rsPrestamos.getInt("activos");
                }
            }

            int maxPrestamosPermitidos = 0;
            String sqlConfig = null;

            if (nombreRol.equalsIgnoreCase("Alumno")) {
                sqlConfig = "SELECT valor_parametro FROM configuraciones_sistema WHERE nombre_parametro = 'MaxPrestamosAlumno'";
            } else if (nombreRol.equalsIgnoreCase("Profesor")) {
                sqlConfig = "SELECT valor_parametro FROM configuraciones_sistema WHERE nombre_parametro = 'MaxPrestamoProfesor'";
            }

            if (sqlConfig != null) {
                try (PreparedStatement psConfig = conn.prepareStatement(sqlConfig)) {
                    ResultSet rsConfig = psConfig.executeQuery();
                    if (rsConfig.next()) {
                        maxPrestamosPermitidos = Integer.parseInt(rsConfig.getString("valor_parametro"));
                    }
                }
            }

            if (prestamosActivos >= maxPrestamosPermitidos && maxPrestamosPermitidos > 0) {
                return "El usuario con correo " + correo + " ya alcanzó el máximo de préstamos permitidos (" +
                       maxPrestamosPermitidos + ") para el " + nombreRol + ".";
            }

        } catch (SQLException e) {
            logger.error("Error al insertar préstamo: " + e.getMessage(), e);
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
            logger.error("Error al verificar préstamo: " + e.getMessage(), e);
            mensaje = "Error al verificar mora: " + e.getMessage();
        }

        return mensaje;
    }
    
    public boolean prestarEjemplarConPrestamo(Prestamo p, Usuario adminData, int idEjemplar) {
        try (Connection conn = Conexion.conectar()) {
            conn.setAutoCommit(false);

            boolean exitoPrestamo = insertar(p, adminData, conn);
            boolean exitoEjemplar = marcarPrestado(idEjemplar, conn);

            if (exitoPrestamo && exitoEjemplar) {
                conn.commit();
                AuditoriaLogger.registrar("PRESTAR_EJEMPLAR",
                    "Se generó préstamo y se cambió estado a 'Prestado' para ejemplar ID: " + idEjemplar);
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error en transacción de préstamo: " + e.getMessage(), e);
            return false;
        }
    }
    
    private boolean marcarPrestado(int idEjemplar, Connection conn) {
        String sqlEstado = "SELECT estado_ejemplar_id FROM estados_ejemplar WHERE nombre_estado = ? AND habilitado = TRUE";
        String sqlUpdate = "UPDATE ejemplares SET estado_ejemplar_id = ? WHERE ejemplar_id = ?";

        try (PreparedStatement psEstado = conn.prepareStatement(sqlEstado);
             PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {

            psEstado.setString(1, "Prestado");
            ResultSet rs = psEstado.executeQuery();

            if (rs.next()) {
                int estadoPrestadoId = rs.getInt("estado_ejemplar_id");

                psUpdate.setInt(1, estadoPrestadoId);
                psUpdate.setInt(2, idEjemplar);

                int filas = psUpdate.executeUpdate();

                if (filas > 0) {
                    AuditoriaLogger.registrar("PRESTAR_EJEMPLAR", "Se cambió el estado a 'Prestado' el ejemplar ID: " + idEjemplar);
                    return true;
                } else {
                    return false;
                }
            } else {
                logger.warn("No se encontró el estado 'Prestado' en la tabla estados_ejemplar.");
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error al modificar ejemplar: " + e.getMessage(), e);
            return false;
        }
    }
    
    private Prestamo mapResultSet(ResultSet rs) throws SQLException {
        Prestamo prestamo = new Prestamo();
        prestamo.setPrestamoId(rs.getInt("prestamo_id"));
        prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo").toLocalDate());
        prestamo.setFechaDevolucionPrevista(rs.getDate("fecha_devolucion_prevista").toLocalDate());
        prestamo.setFechaDevolucionReal(rs.getDate("fecha_devolucion_real") != null ? rs.getDate("fecha_devolucion_real").toLocalDate() : null);
        prestamo.setObservaciones(rs.getString("observaciones"));
        prestamo.setHabilitado(rs.getBoolean("habilitado"));

        Usuario usr = new Usuario();
        usr.setIdUsuario(rs.getInt("usuario_id"));
        usr.setCorreo(rs.getString("usuario_correo"));
        prestamo.setUsuario(usr);
        
        Ejemplar ej = new Ejemplar();
        ej.setEjemplarID(rs.getInt("ejemplar_id"));
        java.sql.Date sqlDate = rs.getDate("fecha_adquisicion");
        if (sqlDate != null) {
            ej.setFechaAdquisicion(sqlDate.toLocalDate());
        }

        // Documento
        Documento doc = new Documento();
        doc.setDocumentoID(rs.getInt("documento_id"));
        doc.setTitulo(rs.getString("titulo"));
        doc.setAutor(rs.getString("autor"));
        doc.setTipo(rs.getString("tipo_documento_id"));
        doc.setCategoria(rs.getString("categoria_id"));
        doc.setEditorial(rs.getString("editorial_id"));
        doc.setIdioma(rs.getString("idioma"));
        doc.setFormato(rs.getString("formato"));
        doc.setAñoPublicacion(rs.getInt("anio_publicacion"));
        doc.setPaginas(rs.getInt("numero_paginas"));
        ej.setDocumento(doc);

        // Ubicación
        Ubicacion ub = new Ubicacion();
        ub.setUbicacionID(rs.getInt("ubicacion_id"));
        ub.setSala(rs.getString("sala"));
        ub.setEstanteria(rs.getString("estanteria"));
        ub.setNivel(rs.getString("nivel"));
        ub.setCodigoRack(rs.getString("codigo_rack"));
        ub.setDescripcion(rs.getString("ubicacion_descripcion"));
        ub.setHabilitado(rs.getBoolean("ubicacion_habilitado"));
        ej.setUbicacion(ub);

        // EstadoEjemplar
        EstadoEjemplar est = new EstadoEjemplar();
        est.setEstadoEjemplarID(rs.getInt("estado_ejemplar_id"));
        est.setNombre(rs.getString("estado_ejemplar_nombre"));

        ej.setEstadoEjemplar(est);
        prestamo.setEjemplar(ej);
        
        EstadoPrestamo esp = new EstadoPrestamo();
        esp.setEstadoPrestamoID(rs.getInt("estado_prestamo_id"));
        esp.setNombre(rs.getString("estado_prestamo_nombre"));
        prestamo.setEstadoPrestamo(esp);

        return prestamo;
    }
    
    public boolean verificarMoraPorPrestamo(int prestamoId) {
        try (Connection conn = Conexion.conectar()) {
            boolean existeMora = false;
            String sql = "SELECT COUNT(*) FROM moras WHERE prestamo_id = ? AND habilitado = TRUE";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, prestamoId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        existeMora = rs.getInt(1) > 0;
                    }
                }
            }
            if (existeMora) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error al verificar mora para préstamo: " + e.getMessage(), e);
            return false;
        }
    }

}
