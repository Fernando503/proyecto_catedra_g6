/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.EstadisticaDAO;
import biblioteca.udb.edu.sv.entidades.Estadistica;
import biblioteca.udb.edu.sv.tools.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class EstadisticaController {
    
     private static final Logger logger = LogManager.getLogger(EstadisticaController.class);
    private final EstadisticaDAO estadisticaDAO;

    public EstadisticaController() {
        this.estadisticaDAO = new EstadisticaDAO();
    }
    
     public Estadistica obtenerEstadistica(Integer idusuario, Boolean isAdmin) {
        try {
            return estadisticaDAO.obtenerEstadisticas(idusuario, isAdmin);
        } catch (Exception e) {
            logger.error("Error al obtener estadistica: " + e.getMessage());
            return null;
        }
    }
    
}
