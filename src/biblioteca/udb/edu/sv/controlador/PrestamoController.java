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
    
    public boolean insertar(Prestamo prest, Usuario user, int idEjemplar) {
        try {
            return prestamoDAO.prestarEjemplarConPrestamo(prest, user, idEjemplar);
        } catch (Exception e) {
            logger.error("Error al insertar préstamo: " + e.getMessage());
            return false;
        }
    }
    
    public boolean registrarDevolucion(int prestID, LocalDate devolFecha, int estPrestamo) {
        try {
            return prestamoDAO.registrarDevolucion(prestID, devolFecha, estPrestamo);
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
    
    public String validarPrestamo(String correo){
        try {
            return prestamoDAO.validarUsuarioPrestamo(correo);
        } catch (Exception e) {
            return "Error al verificar correo";
        }
    }
    
    public String validarDevolucion(String correo){
        try {
            return prestamoDAO.verificarPrestamosActivosPorCorreo(correo);
        } catch (Exception e) {
            return "Error al verificar correo";
        }
    }
    
    public String validarMora(String correo){
        try {
            return prestamoDAO.verificarMoraPorCorreo(correo);
        } catch (Exception e) {
            return "Error al verificar correo";
        }
    }
    
    public String valPrestByCorreo (String correo) {
        try {
            return prestamoDAO.verificarPrestamosByCorreo(correo);
        } catch (Exception e) {
             return "Error al verificar correo";
        }
    }
    
    public boolean genMoraPrestamo (int idPrestamo){
        try {
            return prestamoDAO.verificarMoraPorPrestamo(idPrestamo);
        } catch (Exception e) {
            return true;
        }
    }
    
  
}
