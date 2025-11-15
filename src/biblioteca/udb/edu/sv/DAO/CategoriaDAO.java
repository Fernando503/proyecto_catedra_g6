package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.Categoria;
import biblioteca.udb.edu.sv.tools.Conexion;
import biblioteca.udb.edu.sv.tools.LogManager;
import biblioteca.udb.edu.sv.tools.AuditoriaLogger;

import java.sql.*;
import java.util.*;
import org.apache.log4j.Logger;

public class CategoriaDAO {

    private static final Logger logger = LogManager.getLogger(CategoriaDAO.class);

    // ---------------------------------------------------------
    // LISTAR TODAS LAS CATEGORÍAS
    // ---------------------------------------------------------
    public List<Categoria> listarCategorias() {
        List<Categoria> lista = new ArrayList<>();

        String sql = "SELECT categoria_id, nombre_categoria, descripcion, habilitado "
                   + "FROM categorias";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria c = new Categoria();

                c.setCategoriaId(rs.getInt("categoria_id"));
                c.setNombreCategoria(rs.getString("nombre_categoria"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setHabilitado(rs.getBoolean("habilitado"));

                lista.add(c);
            }

        } catch (SQLException e) {
            logger.error("Error al listar categorías: " + e.getMessage());
        }
        return lista;
    }

    // ---------------------------------------------------------
    // BUSCAR POR FILTRO (LIKE)
    // ---------------------------------------------------------
    public List<Categoria> buscarCategorias(String filtro) {
        List<Categoria> lista = new ArrayList<>();

        String sql = "SELECT categoria_id, nombre_categoria, descripcion, habilitado "
                   + "FROM categorias "
                   + "WHERE nombre_categoria LIKE ? OR descripcion LIKE ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String like = "%" + filtro + "%";
            ps.setString(1, like);
            ps.setString(2, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Categoria c = new Categoria();

                    c.setCategoriaId(rs.getInt("categoria_id"));
                    c.setNombreCategoria(rs.getString("nombre_categoria"));
                    c.setDescripcion(rs.getString("descripcion"));
                    c.setHabilitado(rs.getBoolean("habilitado"));

                    lista.add(c);
                }
            }

        } catch (SQLException e) {
            logger.error("Error al buscar categorías: " + e.getMessage());
        }

        return lista;
    }

    // ---------------------------------------------------------
    // AGREGAR CATEGORÍA
    // ---------------------------------------------------------
    public boolean insertarCategoria(Categoria c) {
        String sql = "INSERT INTO categorias (nombre_categoria, descripcion, habilitado) "
                   + "VALUES (?, ?, ?)";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombreCategoria());
            ps.setString(2, c.getDescripcion());
            ps.setBoolean(3, c.isHabilitado());

            ps.executeUpdate();

            AuditoriaLogger.registrar("CREAR_CATEGORIA",
                    "Se creó la categoría: " + c.getNombreCategoria());

            return true;

        } catch (SQLException e) {
            logger.error("Error al insertar categoría: " + e.getMessage());
            return false;
        }
    }

    // ---------------------------------------------------------
    // ACTUALIZAR CATEGORÍA
    // ---------------------------------------------------------
    public boolean actualizarCategoria(Categoria c) {
        String sql = "UPDATE categorias SET nombre_categoria = ?, descripcion = ?, habilitado = ? "
                   + "WHERE categoria_id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNombreCategoria());
            ps.setString(2, c.getDescripcion());
            ps.setBoolean(3, c.isHabilitado());
            ps.setInt(4, c.getCategoriaId());

            ps.executeUpdate();

            AuditoriaLogger.registrar("ACTUALIZAR_CATEGORIA",
                    "Se actualizó la categoría: " + c.getCategoriaId());

            return true;

        } catch (SQLException e) {
            logger.error("Error al actualizar categoría: " + e.getMessage());
            return false;
        }
    }

    // ---------------------------------------------------------
    // ELIMINAR CATEGORÍA
    // ---------------------------------------------------------
    public boolean eliminarCategoria(int id) {
        String sql = "DELETE FROM categorias WHERE categoria_id = ?";

        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            AuditoriaLogger.registrar("ELIMINAR_CATEGORIA",
                    "Se eliminó la categoría ID: " + id);

            return true;

        } catch (SQLException e) {
            logger.error("Error al eliminar categoría: " + e.getMessage());
            return false;
        }
    }
    
    public List<String> obtenerEstados() {
        List<String> estados = new ArrayList<>();
        estados.add("Habilitado");
        estados.add("Deshabilitado");
        return estados;
    }
}
