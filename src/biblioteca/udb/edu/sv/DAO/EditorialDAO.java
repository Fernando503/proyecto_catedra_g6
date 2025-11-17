/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.Ciudad;
import biblioteca.udb.edu.sv.entidades.Editorial;
import biblioteca.udb.edu.sv.entidades.Pais;
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
public class EditorialDAO {
    private static final Logger logger = LogManager.getLogger(EditorialDAO.class);
    
    public boolean insertar(Editorial editorial) {
        String sql = "INSERT INTO editoriales (nombre_editorial, pais_id, ciudad_id, sitio_web, contacto_email, contacto_telefono, observaciones) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, editorial.getNombreEditorial());
            ps.setInt(2, editorial.getPaisEditorial().getPaisID());
            ps.setInt(3, editorial.getCiudadEditorial().getCiudadID());
            ps.setString(4, editorial.getSitioWeb());
            ps.setString(5, editorial.getContactoEmail());
            ps.setString(6, editorial.getContactoPhone());
            ps.setString(7, editorial.getObservaciones());

            ps.executeUpdate();

            AuditoriaLogger.registrar("CREAR_EDITORIAL", "Se creó la editorial: " + editorial.getNombreEditorial());
            return true;

        } catch (SQLException e) {
            logger.error("Error al insertar editorial: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Editorial editorial) {
        String sql = "UPDATE editoriales SET nombre_editorial = ?, pais_id = ?, ciudad_id = ?, sitio_web = ?, contacto_email = ?, contacto_telefono = ?,  observaciones = ? " +
                     "WHERE editorial_id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, editorial.getNombreEditorial());
            ps.setInt(2, editorial.getPaisEditorial().getPaisID());
            ps.setInt(3, editorial.getCiudadEditorial().getCiudadID());
            ps.setString(4, editorial.getSitioWeb());
            ps.setString(5, editorial.getContactoEmail());
             ps.setString(6, editorial.getContactoPhone());
            ps.setString(7, editorial.getObservaciones());
            ps.setInt(8, editorial.getEditorialID());

            ps.executeUpdate();

            AuditoriaLogger.registrar("MODIFICAR_EDITORIAL", "Se modificó la editorial ID: " + editorial.getEditorialID());
            return true;

        } catch (SQLException e) {
            logger.error("Error al actualizar editorial: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(Integer id) {
        String sql = "UPDATE editoriales SET habilitado = FALSE WHERE editorial_id = ?";
        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            AuditoriaLogger.registrar("ELIMINAR_EDITORIAL", "Se deshabilitó la editorial ID: " + id);
            return true;

        } catch (SQLException e) {
            logger.error("Error al eliminar editorial: " + e.getMessage());
            return false;
        }
    }

    public List<Editorial> listar() {
        List<Editorial> lista = new ArrayList<>();
        String sql = "SELECT e.editorial_id, e.nombre_editorial, e.sitio_web, e.contacto_email, e.contacto_telefono, e.observaciones, e.habilitado, " +
                     "p.pais_id, p.nombre AS pais_nombre, p.habilitado AS pais_habilitado, " +
                     "c.ciudad_id, c.nombre AS ciudad_nombre, c.habilitado AS ciudad_habilitado " +
                     "FROM editoriales e " +
                     "INNER JOIN pais p ON e.pais_id = p.pais_id " +
                     "INNER JOIN ciudad c ON e.ciudad_id = c.ciudad_id";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pais pais = new Pais();
                pais.setPaisID(rs.getInt("pais_id"));
                pais.setNombre(rs.getString("pais_nombre"));
                pais.setHabilitado(rs.getBoolean("pais_habilitado"));

                Ciudad ciudad = new Ciudad();
                ciudad.setCiudadID(rs.getInt("ciudad_id"));
                ciudad.setNombre(rs.getString("ciudad_nombre"));
                ciudad.setPais(pais);
                ciudad.setHabilitado(rs.getBoolean("ciudad_habilitado"));

                Editorial editorial = new Editorial();
                editorial.setEditorialID(rs.getInt("editorial_id"));
                editorial.setNombreEditorial(rs.getString("nombre_editorial"));
                editorial.setPaisEditorial(pais);
                editorial.setCiudadEditorial(ciudad);
                editorial.setSitioWeb(rs.getString("sitio_web"));
                editorial.setContactoEmail(rs.getString("contacto_email"));
                editorial.setContactoPhone(rs.getString("contacto_telefono"));
                editorial.setObservaciones(rs.getString("observaciones"));
                editorial.setHabilitado(rs.getBoolean("habilitado"));

                lista.add(editorial);
            }

        } catch (SQLException e) {
            logger.error("Error al listar editoriales: " + e.getMessage());
        }
        return lista;
    }

    public List<Editorial> buscarEditorial(String filter) {
        List<Editorial> lista = new ArrayList<>();
        String sql = "SELECT e.editorial_id, e.nombre_editorial, e.sitio_web,  e.contacto_email, e.contacto_telefono, e.observaciones, e.habilitado, " +
                     "p.pais_id, p.nombre AS pais_nombre, p.habilitado AS pais_habilitado, " +
                     "c.ciudad_id, c.nombre AS ciudad_nombre, c.habilitado AS ciudad_habilitado " +
                     "FROM editoriales e " +
                     "INNER JOIN pais p ON e.pais_id = p.pais_id " +
                     "INNER JOIN ciudad c ON e.ciudad_id = c.ciudad_id " +
                     "WHERE CAST(e.editorial_id AS CHAR) LIKE ? " +
                     "   OR e.nombre_editorial LIKE ? " +
                     "   OR e.sitio_web LIKE ? " +
                     "   OR e.contacto_email LIKE ? " +
                     "   OR e.contacto_telefono LIKE ? " +
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

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pais pais = new Pais();
                    pais.setPaisID(rs.getInt("pais_id"));
                    pais.setNombre(rs.getString("pais_nombre"));
                    pais.setHabilitado(rs.getBoolean("pais_habilitado"));

                    Ciudad ciudad = new Ciudad();
                    ciudad.setCiudadID(rs.getInt("ciudad_id"));
                    ciudad.setNombre(rs.getString("ciudad_nombre"));
                    ciudad.setPais(pais);
                    ciudad.setHabilitado(rs.getBoolean("ciudad_habilitado"));

                    Editorial editorial = new Editorial();
                    editorial.setEditorialID(rs.getInt("editorial_id"));
                    editorial.setNombreEditorial(rs.getString("nombre_editorial"));
                    editorial.setPaisEditorial(pais);
                    editorial.setCiudadEditorial(ciudad);
                    editorial.setSitioWeb(rs.getString("sitio_web"));
                    editorial.setContactoEmail(rs.getString("contacto_email"));
                    editorial.setContactoPhone(rs.getString("contacto_telefono"));
                    editorial.setObservaciones(rs.getString("observaciones"));
                    editorial.setHabilitado(rs.getBoolean("habilitado"));

                    lista.add(editorial);
                }
            }

        } catch (SQLException e) {
            logger.error("Error al buscar editorial con filtro '" + filter + "': " + e.getMessage());
        }
        return lista;
    }
    
    public List<Editorial> listarActivos() {
        List<Editorial> lista = new ArrayList<>();
        String sql = "SELECT e.editorial_id, e.nombre_editorial, e.sitio_web, e.contacto_email, e.contacto_telefono, e.observaciones, e.habilitado, " +
                     "p.pais_id, p.nombre AS pais_nombre, p.habilitado AS pais_habilitado, " +
                     "c.ciudad_id, c.nombre AS ciudad_nombre, c.habilitado AS ciudad_habilitado " +
                     "FROM editoriales e " +
                     "INNER JOIN pais p ON e.pais_id = p.pais_id " +
                     "INNER JOIN ciudad c ON e.ciudad_id = c.ciudad_id " + 
                     "WHERE e.habilitado = TRUE";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pais pais = new Pais();
                pais.setPaisID(rs.getInt("pais_id"));
                pais.setNombre(rs.getString("pais_nombre"));
                pais.setHabilitado(rs.getBoolean("pais_habilitado"));

                Ciudad ciudad = new Ciudad();
                ciudad.setCiudadID(rs.getInt("ciudad_id"));
                ciudad.setNombre(rs.getString("ciudad_nombre"));
                ciudad.setPais(pais);
                ciudad.setHabilitado(rs.getBoolean("ciudad_habilitado"));

                Editorial editorial = new Editorial();
                editorial.setEditorialID(rs.getInt("editorial_id"));
                editorial.setNombreEditorial(rs.getString("nombre_editorial"));
                editorial.setPaisEditorial(pais);
                editorial.setCiudadEditorial(ciudad);
                editorial.setSitioWeb(rs.getString("sitio_web"));
                editorial.setContactoEmail(rs.getString("contacto_email"));
                editorial.setContactoPhone(rs.getString("contacto_telefono"));
                editorial.setObservaciones(rs.getString("observaciones"));
                editorial.setHabilitado(rs.getBoolean("habilitado"));

                lista.add(editorial);
            }

        } catch (SQLException e) {
            logger.error("Error al listar editoriales: " + e.getMessage());
        }
        return lista;
    }

    
}
