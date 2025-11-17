/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.EstadoEjemplarDAO;
import biblioteca.udb.edu.sv.entidades.EstadoEjemplar;
import biblioteca.udb.edu.sv.tools.LogManager;
import org.apache.log4j.Logger;
import java.util.*;

/**
 *
 * @author Fernando Flamenco
 */
public class EstadoEjemplarController {
    private static final Logger logger = LogManager.getLogger(EstadoEjemplarController.class);
    private final EstadoEjemplarDAO estadoEjemplarDAO;

    public EstadoEjemplarController() {
        this.estadoEjemplarDAO = new EstadoEjemplarDAO();
    }
    
    public boolean insertar(EstadoEjemplar estEjem) {
        try {
            return estadoEjemplarDAO.insertar(estEjem);
        } catch (Exception e) {
            logger.error("Error al insertar estado ejemplar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(EstadoEjemplar estEjem) {
        try {
            return estadoEjemplarDAO.actualizar(estEjem);
        } catch (Exception e) {
            logger.error("Error al actualizar estado ejemplar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idEstEjem) {
        try {
            return estadoEjemplarDAO.eliminar(idEstEjem);
        } catch (Exception e) {
            logger.error("Error al eliminar estado ejemplar: " + e.getMessage());
            return false;
        }
    }

    public List<EstadoEjemplar> listar() {
        try {
            return estadoEjemplarDAO.listar();
        } catch (Exception e) {
            logger.error("Error al listar estado ejemplares: " + e.getMessage());
            return null;
        }
    }
    
    public List<EstadoEjemplar> buscar(String filter) {
        try {
            return estadoEjemplarDAO.buscar(filter);
        } catch (Exception e) {
            logger.error("Error al buscar estado ejemplares: " + e.getMessage());
            return null;
        }
    }
    
    public List<EstadoEjemplar> listarActivosCMB() {
        try {
            return estadoEjemplarDAO.listarActivos();
        } catch (Exception e) {
            logger.error("Error al listar estado ejemplares: " + e.getMessage());
            return null;
        }
    }
}
