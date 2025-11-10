package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.Usuario;
import biblioteca.udb.edu.sv.tools.Conexion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// @author Fernando Flamenco

public class UsuarioDAO {

    // ============================================================
    //  OBTENER USUARIO POR CREDENCIALES (Login)
    // ============================================================
    public Usuario obtenerUsuarioPorCredenciales(String correo, String contraseña) {
        String sql = "SELECT u.UsuarioID, u.Nombre, u.Correo, u.Contraseña, "
                   + "u.EstadoUsuario, r.NombreRol "
                   + "FROM Usuarios u "
                   + "INNER JOIN Roles r ON u.RolID = r.RolID "
                   + "WHERE u.Correo = ? AND u.Contraseña = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contraseña);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("UsuarioID"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setCorreo(rs.getString("Correo"));
                usuario.setContraseña(rs.getString("Contraseña"));
                usuario.setEstadoUsuario(rs.getString("EstadoUsuario"));
                usuario.setRol(rs.getString("NombreRol"));
                return usuario;
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por credenciales: " + e.getMessage());
        }
        return null;
    }

    // ============================================================
    //  LISTAR USUARIOS PARA TABLA DE GESTIÓN
    // ============================================================
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.UsuarioID, u.Nombre, u.Correo, r.NombreRol, u.EstadoUsuario "
                   + "FROM Usuarios u "
                   + "INNER JOIN Roles r ON u.RolID = r.RolID";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("UsuarioID"));
                u.setNombre(rs.getString("Nombre"));
                u.setCorreo(rs.getString("Correo"));
                u.setRol(rs.getString("NombreRol"));
                u.setEstadoUsuario(rs.getString("EstadoUsuario"));
                lista.add(u);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    // ============================================================
    //  INSERTAR USUARIO
    // ============================================================
    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (Nombre, Correo, Contraseña, RolID, EstadoUsuario) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int rolID = obtenerRolID(con, usuario.getRol());

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContraseña());
            ps.setInt(4, rolID);
            ps.setString(5, usuario.getEstadoUsuario());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    //  ACTUALIZAR USUARIO
    // ============================================================
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE Usuarios "
                   + "SET Nombre=?, Correo=?, Contraseña=?, RolID=?, EstadoUsuario=? "
                   + "WHERE UsuarioID=?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int rolID = obtenerRolID(con, usuario.getRol());

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContraseña());
            ps.setInt(4, rolID);
            ps.setString(5, usuario.getEstadoUsuario());
            ps.setInt(6, usuario.getIdUsuario());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    //  ELIMINAR USUARIO
    // ============================================================
    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM Usuarios WHERE UsuarioID=?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    // ============================================================
    //  OBTENER ROL ID (USANDO CONEXIÓN EXISTENTE)
    // ============================================================
    private int obtenerRolID(Connection con, String nombreRol) throws SQLException {
        String sql = "SELECT RolID FROM Roles WHERE NombreRol=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreRol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("RolID");
            }
        }
        return 1; // valor por defecto si no encuentra
    }

    // ============================================================
    //  RESTABLECER CONTRASEÑA
    // ============================================================
    public boolean restablecerContraseña(int idUsuario, String nuevaContraseña) {
        String sql = "UPDATE Usuarios SET Contraseña = ? WHERE UsuarioID = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevaContraseña);
            ps.setInt(2, idUsuario);
            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al restablecer contraseña: " + e.getMessage());
            return false;
        }
    }
    
    // ============================================================
    //  VERIFICAR SI UN USUARIO ES MOROSO
    // ============================================================
    public boolean esUsuarioMoroso(int idUsuario) {
        String sql = "SELECT EstadoUsuario FROM Usuarios WHERE UsuarioID = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String estado = rs.getString("EstadoUsuario");
                return "Moroso".equalsIgnoreCase(estado);
            }

        } catch (SQLException e) {
            System.err.println("Error al verificar estado moroso: " + e.getMessage());
        }

        return false; // por defecto no moroso
    }

}
