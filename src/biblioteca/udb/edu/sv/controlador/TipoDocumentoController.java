/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.TipoDocumentoDAO;
import biblioteca.udb.edu.sv.entidades.TipoDocumento;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class TipoDocumentoController {
    private static final Logger logger = LogManager.getLogger(TipoDocumentoController.class);
    private final TipoDocumentoDAO tipoDocumentoDAO;

    public TipoDocumentoController() {
        this.tipoDocumentoDAO = new TipoDocumentoDAO();
    }

    public boolean insertar(TipoDocumento tipDoc) {
        try {
            return tipoDocumentoDAO.insertar(tipDoc);
        } catch (Exception e) {
            logger.error("Error al insertar tipo de documento: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(TipoDocumento tipDoc) {
        try {
            return tipoDocumentoDAO.actualizar(tipDoc);
        } catch (Exception e) {
            logger.error("Error al actualizar tipo de documento: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idTipoDoc) {
        try {
            return tipoDocumentoDAO.eliminar(idTipoDoc);
        } catch (Exception e) {
            logger.error("Error al eliminar tipo de documento: " + e.getMessage());
            return false;
        }
    }

    public List<TipoDocumento> listar() {
        try {
            return tipoDocumentoDAO.listar();
        } catch (Exception e) {
            logger.error("Error al listar tipo de documento: " + e.getMessage());
            return null;
        }
    }
    
    public List<TipoDocumento> buscar(String filter) {
        try {
            return tipoDocumentoDAO.buscarTipoDocumento(filter);
        } catch (Exception e) {
            logger.error("Error al buscar tipo de documento: " + e.getMessage());
            return null;
        }
    }
    
    public List<TipoDocumento> listarActivosCMB() {
        try {
            return tipoDocumentoDAO.listarActivos();
        } catch (Exception e) {
            logger.error("Error al listar tipo de documento: " + e.getMessage());
            return null;
        }
    }
}
