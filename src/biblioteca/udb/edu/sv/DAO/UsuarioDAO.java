package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.Usuario;
import biblioteca.udb.edu.sv.tools.Conexion;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class UsuarioDAO {

    private static final Logger logger = LogManager.getLogger(UsuarioDAO.class);

    // =====================================================
    // OBTENER USUARIO POR CREDENCIALES (Login)
    // =====================================================
    public Usuario obtenerUsuarioPorCredenciales(String correo, String contraseña) {
        String sql = "SELECT u.usuario_id, u.nombre, u.correo, u.password, "
                   + "u.habilitado, r.nombre_rol "
                   + "FROM usuarios u "
                   + "INNER JOIN roles r ON u.rol_id = r.rol_id "
                   + "WHERE u.correo = ? AND u.password = SHA2(?, 256)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contraseña);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("usuario_id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setCorreo(rs.getString("correo"));
                    usuario.setContraseña(rs.getString("password"));
                    usuario.setRol(rs.getString("nombre_rol"));
                    usuario.setEstadoUsuario(rs.getBoolean("habilitado") ? "Activo" : "Inactivo");
                    return usuario;
                }
            }

        } catch (SQLException e) {
            logger.error("Error al obtener usuario por credenciales: " + e.getMessage());
        }

        return null;
    }

    // =====================================================
    // LISTAR USUARIOS
    // =====================================================
    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT u.usuario_id, u.nombre, u.correo, r.nombre_rol, u.habilitado "
                   + "FROM usuarios u "
                   + "INNER JOIN roles r ON u.rol_id = r.rol_id";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("usuario_id"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setRol(rs.getString("nombre_rol"));
                u.setEstadoUsuario(rs.getBoolean("habilitado") ? "Activo" : "Inactivo");
                lista.add(u);
            }

        } catch (SQLException e) {
            logger.error("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }

    // =====================================================
    // INSERTAR USUARIO
    // =====================================================
    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, correo, password, rol_id, habilitado) "
                   + "VALUES (?, ?, SHA2(?, 256), ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContraseña());
            ps.setInt(4, obtenerRolID(con, usuario.getRol()));
            ps.setBoolean(5, usuario.getEstadoUsuario().equalsIgnoreCase("Activo"));

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            logger.error("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    // =====================================================
    // ACTUALIZAR USUARIO
    // =====================================================
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre=?, correo=?, password=SHA2(?, 256), "
                   + "rol_id=?, habilitado=? WHERE usuario_id=?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContraseña());
            ps.setInt(4, obtenerRolID(con, usuario.getRol()));
            ps.setBoolean(5, usuario.getEstadoUsuario().equalsIgnoreCase("Activo"));
            ps.setInt(6, usuario.getIdUsuario());

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            logger.error("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    // =====================================================
    // ELIMINAR USUARIO
    // =====================================================
    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM usuarios WHERE usuario_id=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            logger.error("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    // =====================================================
    // OBTENER ROL_ID POR NOMBRE
    // =====================================================
    private int obtenerRolID(Connection con, String nombreRol) throws SQLException {
        String sql = "SELECT rol_id FROM roles WHERE nombre_rol=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombreRol);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("rol_id");
        }
        return 3; // Por defecto: Alumno
    }

    // =====================================================
    // RESTABLECER CONTRASEÑA
    // =====================================================
    public boolean restablecerContraseña(int idUsuario, String nuevaContraseña) {
        String sql = "UPDATE usuarios SET password=SHA2(?, 256) WHERE usuario_id=?";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevaContraseña);
            ps.setInt(2, idUsuario);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Error al restablecer contraseña: " + e.getMessage());
            return false;
        }
    }
    
    
    public boolean esUsuarioMoroso(int idUsuario) {
        String sql = "SELECT 1 FROM moras "
                   + "WHERE usuario_id = ? AND pagado = FALSE AND habilitado = TRUE "
                   + "LIMIT 1";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // true si existe al menos una mora pendiente
            }
        } catch (SQLException e) {
            logger.error("Error al verificar moras del usuario: " + e.getMessage());
            return false;
        }
    }

}
