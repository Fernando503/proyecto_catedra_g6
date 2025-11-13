/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.AuditoriaDAO;
import biblioteca.udb.edu.sv.entidades.Auditoria;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class AuditoriaController {
    private static final Logger logger = LogManager.getLogger(AuditoriaController.class);
    private final AuditoriaDAO auditoriaDAO;

    public AuditoriaController() {
        this.auditoriaDAO = new AuditoriaDAO();
    }

    public List<Auditoria> listar() {
        try {
            return auditoriaDAO.listarAuditoria();
        } catch (Exception e) {
            logger.error("Error al listar auditoría: " + e.getMessage());
            return null;
        }
    }

}
