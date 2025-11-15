package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.DocumentoDAO;
import biblioteca.udb.edu.sv.entidades.Documento;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public class DocumentoController {
    
    private static final Logger logger = LogManager.getLogger(DocumentoController.class);
    private final DocumentoDAO documentoDAO;

    public DocumentoController() {
        this.documentoDAO = new DocumentoDAO();
    }

    public List<Documento> listarDocumentos() {
        try {
            return documentoDAO.listarDocumentos();
        } catch (Exception e) {
            logger.error("Error al obtener documentos: " + e.getMessage());
            return null;
        }
    }
    
    
    /**
     * Busca documentos por una columna específica y valor.
     * La lógica interna de columnas válidas está en el DAO.
     */
    
    public List<Documento> buscarDocumentos(String filtro) {
        try {
            if (filtro == null || filtro.trim().isEmpty()) {
                return listarDocumentos();
            }
            return documentoDAO.buscarDocumentos(filtro.trim());
        } catch (Exception e) {
            logger.error("Error al buscar usuarios: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<String> obtenerTiposDocumento() {
        try {
            return documentoDAO.obtenerTiposDocumento();
        } catch (Exception e) {
            logger.error("Error al obtener tipos de documento: " + e.getMessage());
            return null;
        }
    }

    public List<String> obtenerEditoriales() {
        try {
            return documentoDAO.obtenerEditoriales();
        } catch (Exception e) {
            logger.error("Error al obtener editoriales: " + e.getMessage());
            return null;
        }
    }
    
    public List<String> obtenerIdiomas() {
        try {
            return documentoDAO.obtenerIdiomas();
        } catch (Exception e) {
            logger.error("Error al obtener idiomas: " + e.getMessage());
            return null;
        }
    }

    public List<String> obtenerFormatos() {
        try {
            return documentoDAO.obtenerFormatos();
        } catch (Exception e) {
            logger.error("Error al obtener formatos: " + e.getMessage());
            return null;
        }
    }

    public List<String> obtenerEstadosDocumento() {
        try {
            return documentoDAO.obtenerEstadosDocumento();
        } catch (Exception e) {
            logger.error("Error al obtener estados: " + e.getMessage());
            return null;
        }
    }
    
    public int obtenerIdTipo(String nombre) {
        return documentoDAO.obtenerIdTipo(nombre);
    }

    public int obtenerIdCategoria(String nombre) {
        return documentoDAO.obtenerIdCategoria(nombre);
    }

    public int obtenerIdEditorial(String nombre) {
        return documentoDAO.obtenerIdEditorial(nombre);
    }

    public int obtenerIdEstado(String nombre) {
        return documentoDAO.obtenerIdEstado(nombre);
    }

    public boolean actualizarDocumento(Documento d) {
        try {
            int idTipo = obtenerIdTipo(d.getTipo());
            int idCategoria = obtenerIdCategoria(d.getCategoria());
            int idEditorial = obtenerIdEditorial(d.getEditorial());
            
            d.setCodigo(generarCodigo(d));
            
            return documentoDAO.actualizarDocumento(d, idTipo, idCategoria, idEditorial);
        } catch (Exception e) {
            logger.error("Error al actualizar documento: " + e.getMessage());
            return false;
        }
    }
    
    public boolean eliminarDocumento(String nombre, int id) {
        try {
            return documentoDAO.eliminarDocumento(nombre, id);
        } catch (Exception e) {
            logger.error("Error al eliminar documento: " + e.getMessage());
            return false;
        }
    }

    public boolean agregarDocumento(Documento d) {
        try {
            int idTipo = obtenerIdTipo(d.getTipo());
            int idCategoria = obtenerIdCategoria(d.getCategoria());
            int idEditorial = obtenerIdEditorial(d.getEditorial());
            return documentoDAO.insertarDocumento(d, idTipo, idCategoria, idEditorial);
        } catch (Exception e) {
            logger.error("Error al agregar documento: " + e.getMessage());
            return false;
        }
    }
    
        // Genera código único
    public String generarCodigo(Documento d) {

        String categoria = d.getCategoria();
        String titulo = d.getTitulo();

        // abreviación categoría
        String cat = categoria.substring(0, Math.min(3, categoria.length())).toUpperCase();

        // abreviación título
        String tit = titulo.replaceAll("[^A-Za-z]", "").toUpperCase();
        tit = tit.substring(0, Math.min(4, tit.length()));

        // correlativo (COUNT+1)
        int correlativo = documentoDAO.contarDocumentos() + 1;
        String corr = String.format("%03d", correlativo);

        return cat + "-" + tit + "-" + corr;
    }
}
