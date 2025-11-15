/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.PrestamoDAO;
import biblioteca.udb.edu.sv.entidades.Prestamo;
import biblioteca.udb.edu.sv.entidades.Usuario;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.time.LocalDate;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Fernando Flamenco
 */
public class PrestamoController {
    private static final Logger logger = LogManager.getLogger(PrestamoController.class);
    private final PrestamoDAO prestamoDAO;

    public PrestamoController() {
        this.prestamoDAO = new PrestamoDAO();
    }
    
    public boolean insertar(Prestamo prest, Usuario user) {
        try {
            return prestamoDAO.insertar(prest, user);
        } catch (Exception e) {
            logger.error("Error al insertar préstamo: " + e.getMessage());
            return false;
        }
    }
    
    public boolean registrarDevolucion(int prestID, LocalDate devolFecha) {
        try {
            return prestamoDAO.registrarDevolucion(prestID, devolFecha);
        } catch (Exception e) {
            logger.error("Error al registrar devolución préstamo: " + e.getMessage());
            return false;
        }
    }
    
    public List<Prestamo> listarPorRol(Usuario usuario) {
        try {
            return prestamoDAO.listarPorRol(usuario);
        } catch (Exception e) {
            logger.error("Error al listar por rol en préstamo: " + e.getMessage());
            return null;
        }
    }
    
    public List<Prestamo> listarVencidos() {
        try {
            return prestamoDAO.listarVencidos();
        } catch (Exception e) {
            logger.error("Error al listar vencidos en préstamo: " + e.getMessage());
            return null;
        }
    }
    
    public List<Prestamo> listarHistorialPorUsuario(Integer usuarioId) {
        try {
            return prestamoDAO.listarHistorialPorUsuario(usuarioId);
        } catch (Exception e) {
            logger.error("Error al listar historial por usuario en préstamo: " + e.getMessage());
            return null;
        }
    }
}
