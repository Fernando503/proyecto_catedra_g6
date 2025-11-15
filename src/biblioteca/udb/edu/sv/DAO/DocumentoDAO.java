package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.Documento;
import biblioteca.udb.edu.sv.tools.Conexion;
import biblioteca.udb.edu.sv.tools.LogManager;
import biblioteca.udb.edu.sv.tools.AuditoriaLogger;
import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;

public class DocumentoDAO {

    private static final Logger logger = LogManager.getLogger(DocumentoDAO.class);

    public List<Documento> listarDocumentos() {
        List<Documento> lista = new ArrayList<>();

        String sql = "SELECT d.documento_id, d.titulo, d.autor, "
                + "t.nombre_tipo, c.nombre_categoria, e.nombre_editorial, "
                + "d.idioma, d.formato, d.anio_publicacion, d.numero_paginas, d.observaciones, d.codigo_clasificacion, d.habilitado "
                + "FROM documentos d "
                + "LEFT JOIN tipos_documento t ON d.tipo_documento_id = t.tipo_documento_id "
                + "LEFT JOIN categorias c ON d.categoria_id = c.categoria_id "
                + "LEFT JOIN editoriales e ON d.editorial_id = e.editorial_id "
                + "WHERE d.habilitado = TRUE";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Documento doc = new Documento();
                doc.setDocumentoID(rs.getInt("documento_id"));
                doc.setTitulo(rs.getString("titulo"));
                doc.setAutor(rs.getString("autor"));
                doc.setTipo(rs.getString("nombre_tipo"));             // nombre_tipo
                doc.setCategoria(rs.getString("nombre_categoria"));   // nombre_categoria
                doc.setEditorial(rs.getString("nombre_editorial"));   // nombre_editorial
                doc.setIdioma(rs.getString("idioma"));                // idioma
                doc.setFormato(rs.getString("formato"));              // formato
                doc.setAñoPublicacion(rs.getInt("anio_publicacion")); // año
                doc.setPaginas(rs.getInt("numero_paginas"));          // páginas
                doc.setObservaciones(rs.getString("observaciones"));  // observaciones
                doc.setCodigo(rs.getString("codigo_clasificacion"));  // codigo_clasificacion
                doc.setEstado(rs.getBoolean("habilitado"));

                lista.add(doc);
            }

        } catch (SQLException e) {
            logger.error("Error al listar documentos: " + e.getMessage());
        }
        return lista;
    }

    public List<String> obtenerTiposDocumento() {
        return cargarLista("SELECT nombre_tipo FROM tipos_documento");
    }

    public List<String> obtenerCategorias() {
        return cargarLista("SELECT nombre_categoria FROM categorias");
    }

    public List<String> obtenerEditoriales() {
        return cargarLista("SELECT nombre_editorial FROM editoriales");
    }

    private List<String> cargarLista(String sql) {
        List<String> lista = new ArrayList<>();
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString(1));
            }

        } catch (SQLException e) {
            logger.error("Error al cargar lista: " + e.getMessage());
        }
        return lista;
    }

    public List<Documento> buscarDocumentos(String filtro) {
        List<Documento> lista = new ArrayList<>();

        String sql = "SELECT d.documento_id, d.titulo, d.autor, "
                + "t.nombre_tipo, c.nombre_categoria, e.nombre_editorial, "
                + "d.idioma, d.formato, d.anio_publicacion, d.numero_paginas, "
                + "d.observaciones, d.codigo_clasificacion, d.habilitado "
                + "FROM documentos d "
                + "LEFT JOIN tipos_documento t ON d.tipo_documento_id = t.tipo_documento_id "
                + "LEFT JOIN categorias c ON d.categoria_id = c.categoria_id "
                + "LEFT JOIN editoriales e ON d.editorial_id = e.editorial_id "
                + "WHERE d.titulo LIKE ? "
                + "OR d.autor LIKE ? "
                + "OR t.nombre_tipo LIKE ? "
                + "OR c.nombre_categoria LIKE ? "
                + "OR e.nombre_editorial LIKE ? "
                + "OR d.idioma LIKE ? "
                + "OR d.formato LIKE ? "
                + "OR CAST(d.anio_publicacion AS CHAR) LIKE ? "
                + "OR CAST(d.numero_paginas AS CHAR) LIKE ? "
                + "OR d.codigo_clasificacion LIKE ?";

        try (Connection conn = Conexion.conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {

            String like = "%" + filtro + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);
            ps.setString(5, like);
            ps.setString(6, like);
            ps.setString(7, like);
            ps.setString(8, like);
            ps.setString(9, like);
            ps.setString(10, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Documento d = new Documento();
                    d.setDocumentoID(rs.getInt("documento_id"));
                    d.setTitulo(rs.getString("titulo"));
                    d.setAutor(rs.getString("autor"));
                    d.setIdioma(rs.getString("idioma"));
                    d.setFormato(rs.getString("formato"));
                    d.setAñoPublicacion(rs.getInt("anio_publicacion"));
                    d.setPaginas(rs.getInt("numero_paginas"));
                    d.setObservaciones(rs.getString("observaciones"));
                    d.setCodigo(rs.getString("codigo_clasificacion"));
                    d.setEstado(rs.getBoolean("habilitado"));
                    d.setTipo(rs.getString("nombre_tipo"));
                    d.setCategoria(rs.getString("nombre_categoria"));
                    d.setEditorial(rs.getString("nombre_editorial"));

                    lista.add(d);
                }
            }

        } catch (SQLException e) {
            logger.error("Error al buscar documentos: " + e.getMessage());
        }

        return lista;
    }

    public List<String> obtenerIdiomas() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT idioma FROM documentos WHERE idioma IS NOT NULL AND idioma <> ''";
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("idioma"));
            }

        } catch (SQLException e) {
            logger.error("Error al obtener idiomas: " + e.getMessage());
        }
        return lista;
    }

    public List<String> obtenerFormatos() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT formato FROM documentos WHERE formato IS NOT NULL AND formato <> ''";
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(rs.getString("formato"));
            }

        } catch (SQLException e) {
            logger.error("Error al obtener formatos: " + e.getMessage());
        }
        return lista;
    }

    public List<String> obtenerEstadosDocumento() {
        // El estado aquí se basa en la columna habilitado (boolean)
        List<String> estados = new ArrayList<>();
        estados.add("Habilitado");
        estados.add("Deshabilitado");
        return estados;
    }
    
    public boolean insertarDocumento(Documento d, int idTipo, int idCategoria, int idEditorial) {
        String sql = "INSERT INTO documentos (titulo, autor, tipo_documento_id, categoria_id, editorial_id, idioma, formato, anio_publicacion, numero_paginas, observaciones, codigo_clasificacion, habilitado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, TRUE)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, d.getTitulo());
            ps.setString(2, d.getAutor());
            ps.setInt(3, idTipo);
            ps.setInt(4, idCategoria);
            ps.setInt(5, idEditorial);
            ps.setString(6, d.getIdioma());
            ps.setString(7, d.getFormato());
            ps.setInt(8, d.getAñoPublicacion());
            ps.setInt(9, d.getPaginas());
            ps.setString(10, d.getObservaciones());
            ps.setString(11, d.getCodigo());
            ps.executeUpdate();
            
            AuditoriaLogger.registrar("CREAR_DOCUMENTO", "Se creó el documento: " + d.getTitulo() + " | " + d.getDocumentoID());

            return true;
        } catch (SQLException e) { 
            return false;
        }
    }
    
    public boolean actualizarDocumento(Documento d, int idTipo, int idCategoria, int idEditorial) {
        String sql = "UPDATE documentos SET titulo = ?, autor = ?, tipo_documento_id = ?, categoria_id = ?, editorial_id = ?, idioma = ?, formato = ?, anio_publicacion = ?, numero_paginas = ?, observaciones = ?, codigo_clasificacion = ?, habilitado = ? "
                + "WHERE documento_id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, d.getTitulo());
            ps.setString(2, d.getAutor());
            ps.setInt(3, idTipo);
            ps.setInt(4, idCategoria);
            ps.setInt(5, idEditorial);
            ps.setString(6, d.getIdioma());
            ps.setString(7, d.getFormato());
            ps.setInt(8, d.getAñoPublicacion());
            ps.setInt(9, d.getPaginas());
            ps.setString(10, d.getObservaciones());
            ps.setString(11, d.getCodigo());
            ps.setBoolean(12, d.getEstado());
            
            System.out.println(d.getEstado());
            
            ps.setInt(13, d.getDocumentoID());
            ps.executeUpdate();
            AuditoriaLogger.registrar("ACTUALIZAR_DOCUMENTO", "Se actualizo el documento: " + d.getTitulo() + " | " + d.getDocumentoID());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    
    public boolean eliminarDocumento(String nombre, int id) {
        String sql = "DELETE FROM documentos WHERE documento_id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            AuditoriaLogger.registrar("ELIMINAR_DOCUMENTO",
                    "Se eliminó el documento: " + nombre + " | " + id);

            return true;

        } catch (SQLException e) {
            logger.error("Error al eliminar documento: " + e.getMessage());
            return false;
        }
    }
    
    public int obtenerIdTipo(String nombre) {
        String sql = "SELECT tipo_documento_id FROM tipos_documento WHERE nombre_tipo = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {}
        return -1;
    }
    
    public int obtenerIdCategoria(String nombre) {
        String sql = "SELECT categoria_id FROM categorias WHERE nombre_categoria = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {}
        return -1;
    }

    public int obtenerIdEditorial(String nombre) {
        String sql = "SELECT editorial_id FROM editoriales WHERE nombre_editorial = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {}
        return -1;
    }
    
    public int obtenerIdEstado(String nombre) {
        int id = -1;
        String sql = "SELECT estado_id FROM estados_documento WHERE nombre_estado = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) id = rs.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("Error al obtener ID de estado: " + e.getMessage());
        }
        return id;
    }
    
    public int obtenerSiguienteCorrelativo() {
        String sql = "SELECT codigo_clasificacion FROM documentos ORDER BY documento_id DESC LIMIT 1";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String codigo = rs.getString("codigo_clasificacion");
                String[] partes = codigo.split("-");
                return Integer.parseInt(partes[2]) + 1;
            }

        } catch (SQLException e) {
            logger.error("Error al obtener correlativo: " + e.getMessage());
        }

        return 1;
    }
    
    public int contarDocumentos() {
        String sql = "SELECT COUNT(*) AS total FROM documentos";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getInt("total");

        } catch (SQLException e) {
            return 0;
        }
        return 0;
    }
}
