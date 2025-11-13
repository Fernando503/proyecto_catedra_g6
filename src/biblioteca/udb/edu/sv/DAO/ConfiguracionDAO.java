/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.Configuracion;
import biblioteca.udb.edu.sv.tools.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class ConfiguracionDAO {
    private static final Logger logger = LogManager.getLogger(ConfiguracionDAO.class);
    
    public boolean insertarConfiguracion(Configuracion config) {
        String sql = "INSERT INTO configuraciones_sistema (nombre_parametro, valor_parametro, descripcion, habilitado) " +
                     "VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, config.getParametro());
            ps.setString(2, config.getValor());
            ps.setString(3, config.getDescripcion());
            ps.setBoolean(4, config.getHabilitado());

            ps.executeUpdate();

            AuditoriaLogger.registrar("CREAR_CONFIG", "Se creó el parámetro: " + config.getParametro());
            return true;

        } catch (SQLException e) {
            logger.error("Error al insertar configuración: " + e.getMessage());
            return false;
        }
    }
    
    public boolean actualizarConfiguracion(Configuracion config) {
        String sql = "UPDATE configuraciones_sistema SET nombre_parametro = ?, valor_parametro = ?, descripcion = ?, habilitado = ? " +
                     "WHERE config_id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, config.getParametro());
            ps.setString(2, config.getValor());
            ps.setString(3, config.getDescripcion());
            ps.setBoolean(4, config.getHabilitado());
            ps.setInt(5, config.getIdConfig());

            ps.executeUpdate();

            AuditoriaLogger.registrar("ACTUALIZAR_CONFIG", "Se actualizó el parámetro ID: " + config.getIdConfig());
            return true;

        } catch (SQLException e) {
            logger.error("Error al actualizar configuración: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarConfiguracion(int idConfig) {
        String sql = "UPDATE configuraciones_sistema SET habilitado = false WHERE config_id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idConfig);
            ps.executeUpdate();

            AuditoriaLogger.registrar("ELIMINAR_CONFIG", "Se deshabilitó el parámetro ID: " + idConfig);
            return true;

        } catch (SQLException e) {
            logger.error("Error al eliminar configuración: " + e.getMessage());
            return false;
        }
    }

    public List<Configuracion> listarConfiguraciones() {
        List<Configuracion> lista = new ArrayList<>();
        String sql = "SELECT config_id, nombre_parametro, valor_parametro, descripcion, habilitado FROM configuraciones_sistema";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Configuracion c = new Configuracion();
                c.setIdConfig(rs.getInt("config_id"));
                c.setParametro(rs.getString("nombre_parametro"));
                c.setValor(rs.getString("valor_parametro"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setHabilitado(rs.getBoolean("habilitado"));
                lista.add(c);
            }

        } catch (SQLException e) {
           logger.error("Error al listar configuraciones: " + e.getMessage());
        }

        return lista;
    }


    
}
