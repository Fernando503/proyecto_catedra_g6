/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.EstadoPrestamoDAO;
import biblioteca.udb.edu.sv.entidades.EstadoPrestamo;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class EstadoPrestamoController {
 
    private static final Logger logger = LogManager.getLogger(EstadoPrestamoController.class);
    private final EstadoPrestamoDAO estadoPrestamoDAO;

    public EstadoPrestamoController() {
        this.estadoPrestamoDAO = new EstadoPrestamoDAO();
    }
    
    public boolean insertar(EstadoPrestamo estPrest) {
        try {
            return estadoPrestamoDAO.insertar(estPrest);
        } catch (Exception e) {
            logger.error("Error al insertar estado préstamo: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(EstadoPrestamo estPrest) {
        try {
            return estadoPrestamoDAO.actualizar(estPrest);
        } catch (Exception e) {
            logger.error("Error al actualizar estado préstamo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idEstPrest) {
        try {
            return estadoPrestamoDAO.eliminar(idEstPrest);
        } catch (Exception e) {
            logger.error("Error al eliminar estado préstamo: " + e.getMessage());
            return false;
        }
    }

    public List<EstadoPrestamo> listar() {
        try {
            return estadoPrestamoDAO.listar();
        } catch (Exception e) {
            logger.error("Error al listar estado préstamo: " + e.getMessage());
            return null;
        }
    }
    
    public List<EstadoPrestamo> buscar(String filter) {
        try {
            return estadoPrestamoDAO.buscar(filter);
        } catch (Exception e) {
            logger.error("Error al buscar estado préstamo: " + e.getMessage());
            return null;
        }
    }
    
    public List<EstadoPrestamo> listarActivosCMB() {
        try {
            return estadoPrestamoDAO.listarActivos();
        } catch (Exception e) {
            logger.error("Error al listar estado préstamo: " + e.getMessage());
            return null;
        }
    }
}
