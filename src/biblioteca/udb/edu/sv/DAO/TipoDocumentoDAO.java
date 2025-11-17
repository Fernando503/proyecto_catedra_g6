/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.TipoDocumento;
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
public class TipoDocumentoDAO {
    private static final Logger logger = LogManager.getLogger(TipoDocumentoDAO.class);
    
    public boolean insertar(TipoDocumento tipo) {
        String sql = "INSERT INTO tipos_documento (nombre_tipo, descripcion, habilitado) VALUES (?, ?, ?)";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipo.getNombre());
            ps.setString(2, tipo.getDescripcion());
            ps.setBoolean(3, tipo.getHabilitado());

            ps.executeUpdate();

            AuditoriaLogger.registrar("CREAR_TIPO_DOC", "Se creó el tipo de documento: " + tipo.getNombre());
            return true;

        } catch (SQLException e) {
            logger.error("Error al insertar tipo de documento: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(TipoDocumento tipo) {
        String sql = "UPDATE tipos_documento SET nombre_tipo = ?, descripcion = ?, habilitado = ? WHERE tipo_documento_id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipo.getNombre());
            ps.setString(2, tipo.getDescripcion());
            ps.setBoolean(3, tipo.getHabilitado());
            ps.setInt(4, tipo.getTipoDocumentoID());

            ps.executeUpdate();

            AuditoriaLogger.registrar("MODIFICAR_TIPO_DOC", "Se modificó el tipo de documento ID: " + tipo.getTipoDocumentoID());
            return true;

        } catch (SQLException e) {
            logger.error("Error al actualizar tipo de documento: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(Integer id) {
        String sql = "UPDATE tipos_documento SET habilitado = FALSE WHERE tipo_documento_id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            AuditoriaLogger.registrar("ELIMINAR_TIPO_DOC", "Se deshabilitó el tipo de documento ID: " + id);
            return true;

        } catch (SQLException e) {
            logger.error("Error al eliminar tipo de documento: " + e.getMessage());
            return false;
        }
    }

    public List<TipoDocumento> listar() {
        List<TipoDocumento> lista = new ArrayList<>();
        String sql = "SELECT tipo_documento_id, nombre_tipo, descripcion, habilitado FROM tipos_documento";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoDocumento tipo = new TipoDocumento();
                tipo.setTipoDocumentoID(rs.getInt("tipo_documento_id"));
                tipo.setNombre(rs.getString("nombre_tipo"));
                tipo.setDescripcion(rs.getString("descripcion"));
                tipo.setHabilitado(rs.getBoolean("habilitado"));
                lista.add(tipo);
            }

        } catch (SQLException e) {
            logger.error("Error al listar tipos de documento: " + e.getMessage());
        }
        return lista;
    }
    
    public List<TipoDocumento> buscarTipoDocumento(String filter) {
        List<TipoDocumento> lista = new ArrayList<>();
        String sql = "SELECT tipo_documento_id, nombre_tipo, descripcion, habilitado " +
                     "FROM tipos_documento " +
                     "WHERE CAST(tipo_documento_id AS CHAR) LIKE ? " +
                     "   OR nombre_tipo LIKE ? " +
                     "   OR descripcion LIKE ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String filtro = "%" + filter + "%";
            ps.setString(1, filtro);
            ps.setString(2, filtro);
            ps.setString(3, filtro);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TipoDocumento tipo = new TipoDocumento();
                    tipo.setTipoDocumentoID(rs.getInt("tipo_documento_id"));
                    tipo.setNombre(rs.getString("nombre_tipo"));
                    tipo.setDescripcion(rs.getString("descripcion"));
                    tipo.setHabilitado(rs.getBoolean("habilitado"));
                    lista.add(tipo);
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar tipo de documento con filtro '" + filter + "': " + e.getMessage());
        }
        return lista;
    }
    
    public List<TipoDocumento> listarActivos() {
        List<TipoDocumento> lista = new ArrayList<>();
        String sql = "SELECT tipo_documento_id, nombre_tipo, descripcion, habilitado FROM tipos_documento WHERE habilitado = TRUE";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoDocumento tipo = new TipoDocumento();
                tipo.setTipoDocumentoID(rs.getInt("tipo_documento_id"));
                tipo.setNombre(rs.getString("nombre_tipo"));
                tipo.setDescripcion(rs.getString("descripcion"));
                tipo.setHabilitado(rs.getBoolean("habilitado"));
                lista.add(tipo);
            }

        } catch (SQLException e) {
            logger.error("Error al listar tipos de documento: " + e.getMessage());
        }
        return lista;
    }


    
}
