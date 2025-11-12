package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.Documento;
import biblioteca.udb.edu.sv.tools.Conexion;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;

public class DocumentoDAO {

    private static final Logger logger = LogManager.getLogger(DocumentoDAO.class);

    public List<Documento> listarDocumentos() {
        List<Documento> lista = new ArrayList<>();

        String sql = "SELECT d.documento_id, d.titulo, d.autor, "
                   + "t.nombre_tipo, c.nombre_categoria, e.nombre_editorial, "
                   + "d.idioma, d.formato, d.anio_publicacion, d.numero_paginas, d.observaciones "
                   + "FROM documentos d "
                   + "LEFT JOIN tipos_documento t ON d.tipo_documento_id = t.tipo_documento_id "
                   + "LEFT JOIN categorias c ON d.categoria_id = c.categoria_id "
                   + "LEFT JOIN editoriales e ON d.editorial_id = e.editorial_id "
                   + "WHERE d.habilitado = TRUE";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(rs.getString(1));

        } catch (SQLException e) {
            logger.error("Error al cargar lista: " + e.getMessage());
        }
        return lista;
    }
    
    public List<Documento> buscarDocumentos(String columna, String valor) {
        List<Documento> lista = new ArrayList<>();

        // Normaliza el nombre para evitar problemas de mayúsculas/minúsculas
        columna = columna.toLowerCase();

        String campoSQL;
        switch (columna) {
            case "titulo":
                campoSQL = "d.titulo";
                break;
            case "autor":
                campoSQL = "d.autor";
                break;
            case "año de publicacion":
                campoSQL = "d.anio_publicacion";
                break;
            case "idioma":
                campoSQL = "d.idioma";
                break;
            case "formato":
                campoSQL = "d.formato";
                break;
            case "categoria":
                campoSQL = "c.nombre_categoria";
                break;
            case "editorial":
                campoSQL = "e.nombre_editorial";
                break;
            case "tipo":
                campoSQL = "t.nombre_tipo";
                break;
            default:
                System.err.println("Columna no válida para búsqueda: " + columna);
                return lista;
        }

        // Cambia el LIKE por un = si es búsqueda por año
        String sql = "SELECT d.documento_id, d.titulo, d.autor, "
                + "t.nombre_tipo, c.nombre_categoria, e.nombre_editorial, "
                + "d.idioma, d.formato, d.anio_publicacion, d.numero_paginas, d.observaciones "
                + "FROM documentos d "
                + "LEFT JOIN tipos_documento t ON d.tipo_documento_id = t.tipo_documento_id "
                + "LEFT JOIN categorias c ON d.categoria_id = c.categoria_id "
                + "LEFT JOIN editoriales e ON d.editorial_id = e.editorial_id "
                + "WHERE " + campoSQL + (columna.equals("año de publicacion") ? " = ?" : " LIKE ?")
                + " LIMIT 5000";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Si se busca por año, usar comparación numérica
            if (columna.equals("año de publicacion")) {
                try {
                    int anio = Integer.parseInt(valor);
                    ps.setInt(1, anio);
                } catch (NumberFormatException e) {
                    System.err.println("Valor no válido para año: " + valor);
                    return lista;
                }
            } else {
                ps.setString(1, "%" + valor + "%");
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Documento doc = new Documento();
                doc.setDocumentoID(rs.getInt("documento_id"));
                doc.setTitulo(rs.getString("titulo"));
                doc.setAutor(rs.getString("autor"));
                doc.setTipo(rs.getString("nombre_tipo"));
                doc.setCategoria(rs.getString("nombre_categoria"));
                doc.setEditorial(rs.getString("nombre_editorial"));
                doc.setIdioma(rs.getString("idioma"));
                doc.setFormato(rs.getString("formato"));
                doc.setAñoPublicacion(rs.getInt("anio_publicacion"));
                doc.setPaginas(rs.getInt("numero_paginas"));
                doc.setObservaciones(rs.getString("observaciones"));
                lista.add(doc);
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar documentos: " + e.getMessage());
        }

        return lista;
    }
 
    public List<String> obtenerIdiomas() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT idioma FROM documentos WHERE idioma IS NOT NULL AND idioma <> ''";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(rs.getString("idioma"));

        } catch (SQLException e) {
            logger.error("Error al obtener idiomas: " + e.getMessage());
        }
        return lista;
    }

    public List<String> obtenerFormatos() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT formato FROM documentos WHERE formato IS NOT NULL AND formato <> ''";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) lista.add(rs.getString("formato"));

        } catch (SQLException e) {
            logger.error("Error al obtener formatos: " + e.getMessage());
        }
        return lista;
    }

    public List<String> obtenerEstadosDocumento() {
        // El estado aquí se basa en la columna habilitado (boolean)
        List<String> estados = new ArrayList<>();
        estados.add("Activo");
        estados.add("Inactivo");
        return estados;
    }

    public List<String> obtenerColumnasDocumentos() {
        // columnas para mostrar en la tabla, en este ORDEN
        return Arrays.asList(
            "Titulo",
            "Autor",
            "Año de Publicacion",
            "Idioma",
            "Formato",
            "Categoria",
            "Editorial",
            "Tipo"
        );
    }
}