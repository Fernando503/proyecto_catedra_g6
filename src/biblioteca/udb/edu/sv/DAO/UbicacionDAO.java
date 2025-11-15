package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.Ubicacion;
import biblioteca.udb.edu.sv.tools.Conexion;
import biblioteca.udb.edu.sv.tools.LogManager;
import biblioteca.udb.edu.sv.tools.AuditoriaLogger;

import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;

public class UbicacionDAO {

    private static final Logger logger = LogManager.getLogger(UbicacionDAO.class);

    // ---------------------------------------------------------
    // LISTAR TODAS LAS UBICACIONES
    // ---------------------------------------------------------
    public List<Ubicacion> listarUbicaciones() {
        List<Ubicacion> lista = new ArrayList<>();

        String sql = "SELECT ubicacion_id, sala, estanteria, nivel, codigo_rack, descripcion, habilitado "
                   + "FROM ubicaciones";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Ubicacion u = new Ubicacion(
                    rs.getInt("ubicacion_id"),
                    rs.getString("sala"),
                    rs.getString("estanteria"),
                    rs.getString("nivel"),
                    rs.getString("codigo_rack"),
                    rs.getString("descripcion"),
                    rs.getBoolean("habilitado")
                );
                lista.add(u);
            }

        } catch (SQLException e) {
            logger.error("Error al listar ubicaciones: " + e.getMessage());
        }
        return lista;
    }

    // ---------------------------------------------------------
    // BUSCAR POR FILTRO
    // ---------------------------------------------------------
    public List<Ubicacion> buscarUbicaciones(String filtro) {
        List<Ubicacion> lista = new ArrayList<>();

        String sql = "SELECT ubicacion_id, sala, estanteria, nivel, codigo_rack, descripcion, habilitado "
                   + "FROM ubicaciones "
                   + "WHERE sala LIKE ? OR estanteria LIKE ? OR nivel LIKE ? "
                   + "OR codigo_rack LIKE ? OR descripcion LIKE ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String like = "%" + filtro + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            ps.setString(5, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Ubicacion u = new Ubicacion(
                        rs.getInt("ubicacion_id"),
                        rs.getString("sala"),
                        rs.getString("estanteria"),
                        rs.getString("nivel"),
                        rs.getString("codigo_rack"),
                        rs.getString("descripcion"),
                        rs.getBoolean("habilitado")
                    );
                    lista.add(u);
                }
            }

        } catch (SQLException e) {
            logger.error("Error al buscar ubicaciones: " + e.getMessage());
        }

        return lista;
    }

    // ---------------------------------------------------------
    // INSERTAR UBICACIÓN
    // ---------------------------------------------------------
    public boolean insertarUbicacion(Ubicacion u) {
        String sql = "INSERT INTO ubicaciones (sala, estanteria, nivel, codigo_rack, descripcion, habilitado) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getSala());
            ps.setString(2, u.getEstanteria());
            ps.setString(3, u.getNivel());
            ps.setString(4, u.getCodigoRack());
            ps.setString(5, u.getDescripcion());
            ps.setBoolean(6, u.getHabilitado());

            ps.executeUpdate();

            AuditoriaLogger.registrar("CREAR_UBICACION",
                    "Se creó la ubicación en sala: " + u.getSala());

            return true;

        } catch (SQLException e) {
            logger.error("Error al insertar ubicación: " + e.getMessage());
            return false;
        }
    }

    // ---------------------------------------------------------
    // ACTUALIZAR UBICACIÓN
    // ---------------------------------------------------------
    public boolean actualizarUbicacion(Ubicacion u) {
        String sql = "UPDATE ubicaciones SET sala = ?, estanteria = ?, nivel = ?, codigo_rack = ?, "
                   + "descripcion = ?, habilitado = ? "
                   + "WHERE ubicacion_id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getSala());
            ps.setString(2, u.getEstanteria());
            ps.setString(3, u.getNivel());
            ps.setString(4, u.getCodigoRack());
            ps.setString(5, u.getDescripcion());
            ps.setBoolean(6, u.getHabilitado());
            ps.setInt(7, u.getUbicacionID());

            ps.executeUpdate();

            AuditoriaLogger.registrar("ACTUALIZAR_UBICACION",
                    "Se actualizó ubicación ID: " + u.getUbicacionID());

            return true;

        } catch (SQLException e) {
            logger.error("Error al actualizar ubicación: " + e.getMessage());
            return false;
        }
    }

    // ---------------------------------------------------------
    // ELIMINAR UBICACIÓN
    // ---------------------------------------------------------
    public boolean eliminarUbicacion(Integer id) {
        String sql = "DELETE FROM ubicaciones WHERE ubicacion_id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            AuditoriaLogger.registrar("ELIMINAR_UBICACION",
                    "Se eliminó ubicación ID: " + id);

            return true;

        } catch (SQLException e) {
            logger.error("Error al eliminar ubicación: " + e.getMessage());
            return false;
        }
    }
    
    public List<String> obtenerEstados() {
        List<String> estados = new ArrayList<>();
        estados.add("Habilitado");
        estados.add("Deshabilitado");
        return estados;
    }
}
