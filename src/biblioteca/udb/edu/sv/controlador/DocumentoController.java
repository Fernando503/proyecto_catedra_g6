package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.DocumentoDAO;
import biblioteca.udb.edu.sv.entidades.Documento;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.util.List;
import org.apache.log4j.Logger;

public class DocumentoController {
    
    private static final Logger logger = LogManager.getLogger(DocumentoController.class);
    private final DocumentoDAO documentoDAO;

    public DocumentoController() {
        this.documentoDAO = new DocumentoDAO();
    }



    public List<Documento> obtenerDocumentos() {
        try {
            return documentoDAO.listarDocumentos();
        } catch (Exception e) {
            logger.error("Error al obtener documentos: " + e.getMessage());
            return null;
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

    public List<String> obtenerCategorias() {
        try {
            return documentoDAO.obtenerCategorias();
        } catch (Exception e) {
            logger.error("Error al obtener categorías: " + e.getMessage());
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

    /**
     * Busca documentos por una columna específica y valor.
     * La lógica interna de columnas válidas está en el DAO.
     */
    public List<Documento> buscarDocumentos(String columna, String valor) {
        try {
            return documentoDAO.buscarDocumentos(columna, valor);
        } catch (Exception e) {
            logger.error("Error al buscar documentos: " + e.getMessage());
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

    public List<String> obtenerColumnasDocumentos() {
        try {
            return documentoDAO.obtenerColumnasDocumentos();
        } catch (Exception e) {
            logger.error("Error al obtener columnas de documento: " + e.getMessage());
            return null;
        }
    }

}
