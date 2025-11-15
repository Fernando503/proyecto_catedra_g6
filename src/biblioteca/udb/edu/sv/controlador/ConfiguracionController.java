/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.controlador;


import biblioteca.udb.edu.sv.DAO.ConfiguracionDAO;
import biblioteca.udb.edu.sv.entidades.Configuracion;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.util.List;
import org.apache.log4j.Logger;
/**
 *
 * @author Fernando Flamenco
 */
public class ConfiguracionController {
    
    private static final Logger logger = LogManager.getLogger(ConfiguracionController.class);
    private final ConfiguracionDAO configuracionDAO;

    public ConfiguracionController() {
        this.configuracionDAO = new ConfiguracionDAO();
    }

    public boolean insertar(Configuracion config) {
        try {
            return configuracionDAO.insertarConfiguracion(config);
        } catch (Exception e) {
            logger.error("Error al insertar configuración: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Configuracion config) {
        try {
            return configuracionDAO.actualizarConfiguracion(config);
        } catch (Exception e) {
            logger.error("Error al actualizar configuración: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idConfig) {
        try {
            return configuracionDAO.eliminarConfiguracion(idConfig);
        } catch (Exception e) {
            logger.error("Error al eliminar configuración: " + e.getMessage());
            return false;
        }
    }

    public List<Configuracion> listar() {
        try {
            return configuracionDAO.listarConfiguraciones();
        } catch (Exception e) {
            logger.error("Error al listar configuraciones: " + e.getMessage());
            return null;
        }
    }

}
