package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.CategoriaDAO;
import biblioteca.udb.edu.sv.entidades.Categoria;
import biblioteca.udb.edu.sv.tools.LogManager;

import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public class CategoriaController {

    private static final Logger logger = LogManager.getLogger(CategoriaController.class);
    private final CategoriaDAO categoriaDAO;

    public CategoriaController() {
        this.categoriaDAO = new CategoriaDAO();
    }

    public List<Categoria> listarCategorias() {
        try {
            return categoriaDAO.listarCategorias();
        } catch (Exception e) {
            logger.error("Error al obtener categorías: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Categoria> buscarCategorias(String filtro) {
        try {
            if (filtro == null || filtro.trim().isEmpty()) {
                return listarCategorias();
            }
            return categoriaDAO.buscarCategorias(filtro.trim());
        } catch (Exception e) {
            logger.error("Error al buscar categorías: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean agregarCategoria(Categoria c) {
        try {
            return categoriaDAO.insertarCategoria(c);
        } catch (Exception e) {
            logger.error("Error al agregar categoría: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarCategoria(Categoria c) {
        try {
            return categoriaDAO.actualizarCategoria(c);
        } catch (Exception e) {
            logger.error("Error al actualizar categoría: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarCategoria(int id) {
        try {
            return categoriaDAO.eliminarCategoria(id);
        } catch (Exception e) {
            logger.error("Error al eliminar categoría: " + e.getMessage());
            return false;
        }
    }
    
    public List<String> obtenerEstados() {
        try {
            return categoriaDAO.obtenerEstados();
        } catch (Exception e) {
            logger.error("Error al obtener estados de ubicación: " + e.getMessage());
            return null;
        }
    }
}