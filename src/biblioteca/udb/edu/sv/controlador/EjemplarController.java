/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.EjemplarDAO;
import biblioteca.udb.edu.sv.DAO.EstadoEjemplarDAO;
import biblioteca.udb.edu.sv.entidades.Ejemplar;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class EjemplarController {
    private static final Logger logger = LogManager.getLogger(EjemplarController.class);
    private final EjemplarDAO ejemplarDAO;

    public EjemplarController() {
        this.ejemplarDAO = new EjemplarDAO();
    }
    
    public boolean insertar(Ejemplar ejemp) {
        try {
            return ejemplarDAO.insertar(ejemp);
        } catch (Exception e) {
            logger.error("Error al insertar ejemplar: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Ejemplar estEjem) {
        try {
            return ejemplarDAO.modificar(estEjem);
        } catch (Exception e) {
            logger.error("Error al actualizar ejemplar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idEstEjem) {
        try {
            return ejemplarDAO.eliminar(idEstEjem);
        } catch (Exception e) {
            logger.error("Error al eliminar ejemplar: " + e.getMessage());
            return false;
        }
    }

    public List<Ejemplar> listar() {
        try {
            return ejemplarDAO.listar();
        } catch (Exception e) {
            logger.error("Error al listar ejemplares: " + e.getMessage());
            return null;
        }
    }
    
    public List<Ejemplar> buscar(String filter) {
        try {
            return ejemplarDAO.buscar(filter);
        } catch (Exception e) {
            logger.error("Error al buscar ejemplares: " + e.getMessage());
            return null;
        }
    }
    
    public List<Ejemplar> listarActivosCMB() {
        try {
            return ejemplarDAO.listarActivos();
        } catch (Exception e) {
            logger.error("Error al listar ejemplares: " + e.getMessage());
            return null;
        }
    }
}
