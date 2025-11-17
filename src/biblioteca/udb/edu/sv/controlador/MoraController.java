/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.MoraDAO;
import biblioteca.udb.edu.sv.entidades.Mora;
import biblioteca.udb.edu.sv.entidades.Usuario;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.math.BigDecimal;
import java.util.List;
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
    
    public List<Mora> obtenerMoraPendiente (Usuario user){
        try {
            return moraDAO.listarPendientes(user);
        } catch (Exception e) {
            logger.error("Error al listar moras pendientes: " + e.getMessage());
            return null;
        }
    }
    
    public Boolean pagoMora (int moraID, BigDecimal monto, Usuario user){
        try {
            return moraDAO.registrarPago(moraID, monto, user);
        } catch (Exception e) {
            logger.error("Error al pagar mora: " + e.getMessage());
            return false;
        }
    }
    
     public void procesoGestionMora (){
        try {
            moraDAO.verificarMorasPendientes();
        } catch (Exception e) {
            logger.error("Error en proceso automatico de mora: " + e.getMessage());
        }
    }
}
