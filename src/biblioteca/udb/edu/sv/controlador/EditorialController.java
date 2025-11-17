/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.CiudadDAO;
import biblioteca.udb.edu.sv.DAO.EditorialDAO;
import biblioteca.udb.edu.sv.DAO.PaisDAO;
import biblioteca.udb.edu.sv.entidades.Ciudad;
import biblioteca.udb.edu.sv.entidades.Editorial;
import biblioteca.udb.edu.sv.entidades.Pais;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class EditorialController {
    private static final Logger logger = LogManager.getLogger(EditorialController.class);
    private final EditorialDAO editorialDAO;
    private final PaisDAO paisDAO;
    private final CiudadDAO ciudadDAO;

    public EditorialController() {
        this.editorialDAO = new EditorialDAO();
        this.paisDAO = new PaisDAO();
        this.ciudadDAO = new CiudadDAO();
    }
    
    public boolean insertar(Editorial editorial) {
        try {
            return editorialDAO.insertar(editorial);
        } catch (Exception e) {
            logger.error("Error al insertar editorial: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Editorial editorial) {
        try {
            return editorialDAO.actualizar(editorial);
        } catch (Exception e) {
            logger.error("Error al actualizar editorial: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idEditorial) {
        try {
            return editorialDAO.eliminar(idEditorial);
        } catch (Exception e) {
            logger.error("Error al eliminar editorial: " + e.getMessage());
            return false;
        }
    }

    public List<Editorial> listar() {
        try {
            return editorialDAO.listar();
        } catch (Exception e) {
            logger.error("Error al listar editorial: " + e.getMessage());
            return null;
        }
    }
    
    public List<Editorial> buscar(String filter) {
        try {
            return editorialDAO.buscarEditorial(filter);
        } catch (Exception e) {
            logger.error("Error al buscar editorial: " + e.getMessage());
            return null;
        }
    }
    
    public List<Editorial> listarActivosCMB() {
        try {
            return editorialDAO.listarActivos();
        } catch (Exception e) {
            logger.error("Error al listar editorial: " + e.getMessage());
            return null;
        }
    }
    
    public List<Pais> obtenerPaises() {
        try {
            return paisDAO.listarPaises();
        } catch (Exception e) {
            logger.error("Error al obtener paises: " + e.getMessage());
            return null;
        }
    }
    
    public List<Ciudad> obtenerCiudades(int idPais) {
        try {
            return ciudadDAO.listarCiudadesPorPais(idPais);
        } catch (Exception e) {
            logger.error("Error al buscar ciudad por pais: " + e.getMessage());
            return null;
        }
    }
}
