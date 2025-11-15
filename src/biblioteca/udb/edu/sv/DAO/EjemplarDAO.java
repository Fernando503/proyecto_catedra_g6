/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.*;
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
public class EjemplarDAO {
    private static final Logger logger = LogManager.getLogger(EjemplarDAO.class);

    public boolean insertar(Ejemplar ejemplar) {
        String sql = "INSERT INTO ejemplares (documento_id, codigo_barra, ubicacion_id, estado_ejemplar_id, fecha_adquisicion, observaciones, habilitado) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ejemplar.getDocumento().getDocumentoID());
            ps.setString(2, ejemplar.getCodigoBarra());
            ps.setInt(3, ejemplar.getUbicacion().getUbicacionID());
            ps.setInt(4, ejemplar.getEstadoEjemplar().getEstadoEjemplarID());
            ps.setDate(5, java.sql.Date.valueOf(ejemplar.getFechaAdquisicion()));
            ps.setString(6, ejemplar.getObservaciones());
            ps.setBoolean(7, ejemplar.getHabilitado());

            ps.executeUpdate();

            AuditoriaLogger.registrar("CREAR_EJEMPLAR", "Se creó el ejemplar con código: " + ejemplar.getCodigoBarra());
            return true;

        } catch (SQLException e) {
            logger.error("Error al insertar ejemplar: " + e.getMessage());
            return false;
        }
    }

    public boolean modificar(Ejemplar ejemplar) {
        String sql = "UPDATE ejemplares SET documento_id=?, codigo_barra=?, ubicacion_id=?, estado_ejemplar_id=?, fecha_adquisicion=?, observaciones=?, habilitado=? "
                   + "WHERE ejemplar_id=?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ejemplar.getDocumento().getDocumentoID());
            ps.setString(2, ejemplar.getCodigoBarra());
            ps.setInt(3, ejemplar.getUbicacion().getUbicacionID());
            ps.setInt(4, ejemplar.getEstadoEjemplar().getEstadoEjemplarID());
            ps.setDate(5, java.sql.Date.valueOf(ejemplar.getFechaAdquisicion()));
            ps.setString(6, ejemplar.getObservaciones());
            ps.setBoolean(7, ejemplar.getHabilitado());
            ps.setInt(8, ejemplar.getEjemplarID());

            ps.executeUpdate();

            AuditoriaLogger.registrar("MODIFICAR_EJEMPLAR", "Se modificó el ejemplar ID: " + ejemplar.getEjemplarID());
            return true;

        } catch (SQLException e) {
            logger.error("Error al modificar ejemplar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int ejemplarId) {
        String sql = "UPDATE ejemplares SET habilitado = FALSE WHERE ejemplar_id=?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ejemplarId);
            ps.executeUpdate();

            AuditoriaLogger.registrar("ELIMINAR_EJEMPLAR", "Se eliminó el ejemplar ID: " + ejemplarId);
            return true;

        } catch (SQLException e) {
            logger.error("Error al eliminar ejemplar: " + e.getMessage());
            return false;
        }
    }

    public List<Ejemplar> listar() {
        List<Ejemplar> lista = new ArrayList<>();
        String sql = "SELECT e.*, " +
                     "d.titulo, d.autor, d.tipo, d.categoria, d.editorial, d.idioma, d.formato, d.estado, d.año_publicacion, d.paginas, d.observaciones AS doc_observaciones, " +
                     "u.sala, u.estanteria, u.nivel, u.codigo_rack, u.descripcion AS ubicacion_descripcion, u.habilitado AS ubicacion_habilitado, " +
                     "es.nombre AS estado_nombre, es.descripcion AS estado_descripcion, es.habilitado AS estado_habilitado " +
                     "FROM ejemplares e " +
                     "JOIN documentos d ON e.documento_id = d.documento_id " +
                     "LEFT JOIN ubicaciones u ON e.ubicacion_id = u.ubicacion_id " +
                     "LEFT JOIN estados_ejemplar es ON e.estado_ejemplar_id = es.estado_ejemplar_id";
        try (Connection conn = Conexion.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al listar ejemplares: " + e.getMessage());
        }
        return lista;
    }
    
    public List<Ejemplar> buscar(String filter) {
        List<Ejemplar> lista = new ArrayList<>();
        String sql = "SELECT e.ejemplar_id, e.codigo_barra, e.fecha_adquisicion, e.observaciones, e.habilitado, " +
                     "d.documento_id, d.titulo, d.autor, d.tipo, d.categoria, d.editorial, d.idioma, d.formato, d.estado, d.año_publicacion, d.paginas, d.observaciones AS doc_observaciones, " +
                     "u.ubicacion_id, u.sala, u.estanteria, u.nivel, u.codigo_rack, u.descripcion AS ubicacion_descripcion, u.habilitado AS ubicacion_habilitado, " +
                     "es.estado_ejemplar_id, es.nombre AS estado_nombre, es.descripcion AS estado_descripcion, es.habilitado AS estado_habilitado " +
                     "FROM ejemplares e " +
                     "JOIN documentos d ON e.documento_id = d.documento_id " +
                     "LEFT JOIN ubicaciones u ON e.ubicacion_id = u.ubicacion_id " +
                     "LEFT JOIN estados_ejemplar es ON e.estado_ejemplar_id = es.estado_ejemplar_id " +
                     "WHERE CAST(e.ejemplar_id AS CHAR) LIKE ? " +
                     "   OR e.codigo_barra LIKE ? " +
                     "   OR d.titulo LIKE ? " +
                     "   OR d.autor LIKE ? " +
                     "   OR u.sala LIKE ? " +
                     "   OR u.estanteria LIKE ? " +
                     "   OR es.nombre LIKE ? " +
                     "   OR e.observaciones LIKE ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String filtro = "%" + filter + "%";
            ps.setString(1, filtro);
            ps.setString(2, filtro);
            ps.setString(3, filtro);
            ps.setString(4, filtro);
            ps.setString(5, filtro);
            ps.setString(6, filtro);
            ps.setString(7, filtro);
            ps.setString(8, filtro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar ejemplares con filtro '" + filter + "': " + e.getMessage());
        }
        return lista;
    }

    public List<Ejemplar> listarActivos() {
        List<Ejemplar> lista = new ArrayList<>();
        String sql = "SELECT e.*, " +
                     "d.titulo, d.autor, d.tipo, d.categoria, d.editorial, d.idioma, d.formato, d.estado, d.año_publicacion, d.paginas, d.observaciones AS doc_observaciones, " +
                     "u.sala, u.estanteria, u.nivel, u.codigo_rack, u.descripcion AS ubicacion_descripcion, u.habilitado AS ubicacion_habilitado, " +
                     "es.nombre AS estado_nombre, es.descripcion AS estado_descripcion, es.habilitado AS estado_habilitado " +
                     "FROM ejemplares e " +
                     "JOIN documentos d ON e.documento_id = d.documento_id " +
                     "LEFT JOIN ubicaciones u ON e.ubicacion_id = u.ubicacion_id " +
                     "LEFT JOIN estados_ejemplar es ON e.estado_ejemplar_id = es.estado_ejemplar_id " +
                     "WHERE e.habilitado=TRUE";
        try (Connection conn = Conexion.conectar();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            logger.error("Error al listar ejemplares activos: " + e.getMessage());
        }
        return lista;
    }


    private Ejemplar mapResultSet(ResultSet rs) throws SQLException {
        Ejemplar ej = new Ejemplar();
        ej.setEjemplarID(rs.getInt("ejemplar_id"));
        ej.setCodigoBarra(rs.getString("codigo_barra"));
        java.sql.Date sqlDate = rs.getDate("fecha_adquisicion");
        if (sqlDate != null) {
            ej.setFechaAdquisicion(sqlDate.toLocalDate());
        }
        ej.setObservaciones(rs.getString("observaciones"));
        ej.setHabilitado(rs.getBoolean("habilitado"));

        // Documento
        Documento doc = new Documento();
        doc.setDocumentoID(rs.getInt("documento_id"));
        doc.setTitulo(rs.getString("titulo"));
        doc.setAutor(rs.getString("autor"));
        doc.setTipo(rs.getString("tipo"));
        doc.setCategoria(rs.getString("categoria"));
        doc.setEditorial(rs.getString("editorial"));
        doc.setIdioma(rs.getString("idioma"));
        doc.setFormato(rs.getString("formato"));
        doc.setEstado(rs.getString("estado"));
        doc.setAñoPublicacion(rs.getInt("año_publicacion"));
        doc.setPaginas(rs.getInt("paginas"));
        doc.setObservaciones(rs.getString("doc_observaciones"));
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
        est.setNombre(rs.getString("estado_nombre"));
        est.setDescripcion(rs.getString("estado_descripcion"));
        est.setHabilitado(rs.getBoolean("estado_habilitado"));
        ej.setEstadoEjemplar(est);

        return ej;
    }

}
