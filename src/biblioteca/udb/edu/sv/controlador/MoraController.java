/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.MoraDAO;
import biblioteca.udb.edu.sv.tools.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class MoraController {
    private static final Logger logger = LogManager.getLogger(MoraController.class);
    private final MoraDAO moraDAO;

    public MoraController() {
        this.moraDAO = new MoraDAO();
    }
}
