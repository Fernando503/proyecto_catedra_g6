package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.UbicacionDAO;
import biblioteca.udb.edu.sv.entidades.Ubicacion;
import biblioteca.udb.edu.sv.tools.LogManager;

import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public class UbicacionController {

    private static final Logger logger = LogManager.getLogger(UbicacionController.class);
    private final UbicacionDAO ubicacionDAO;

    public UbicacionController() {
        this.ubicacionDAO = new UbicacionDAO();
    }

    public List<Ubicacion> listarUbicaciones() {
        try {
            return ubicacionDAO.listarUbicaciones();
        } catch (Exception e) {
            logger.error("Error al obtener ubicaciones: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public List<Ubicacion> listarUbicacionesActivas() {
        try {
            return ubicacionDAO.listarUbicacionesActivas();
        } catch (Exception e) {
            logger.error("Error al obtener ubicaciones (activas): " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Ubicacion> buscarUbicaciones(String filtro) {
        try {
            if (filtro == null || filtro.trim().isEmpty()) {
                return listarUbicaciones();
            }
            return ubicacionDAO.buscarUbicaciones(filtro.trim());
        } catch (Exception e) {
            logger.error("Error al buscar ubicaciones: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean agregarUbicacion(Ubicacion u) {
        try {
            return ubicacionDAO.insertarUbicacion(u);
        } catch (Exception e) {
            logger.error("Error al agregar ubicación: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarUbicacion(Ubicacion u) {
        try {
            return ubicacionDAO.actualizarUbicacion(u);
        } catch (Exception e) {
            logger.error("Error al actualizar ubicación: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarUbicacion(Integer id) {
        try {
            return ubicacionDAO.eliminarUbicacion(id);
        } catch (Exception e) {
            logger.error("Error al eliminar ubicación: " + e.getMessage());
            return false;
        }
    }
    
    public List<String> obtenerEstados() {
        try {
            return ubicacionDAO.obtenerEstados();
        } catch (Exception e) {
            logger.error("Error al obtener estados de ubicación: " + e.getMessage());
            return null;
        }
    }
    
    public String generarCodigo(Ubicacion u) {

        // Letra de sala: "Sala A" → "A"
        String salaLetra = u.getSala().replaceAll("[^A-Za-z]", "");
        salaLetra = salaLetra.substring(salaLetra.length() - 1).toUpperCase();

        // Número de estantería: "Est-03" → "03"
        String estNum = u.getEstanteria().replaceAll("[^0-9]", "");
        if (estNum.isEmpty()) estNum = "00";

        return "RACK-" + salaLetra + estNum;
    }
}
