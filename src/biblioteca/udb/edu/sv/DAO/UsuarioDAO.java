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
    // LOGIN
    // =====================================================
    public Usuario obtenerUsuarioPorCredenciales(String correo, String contraseña) {
        String sql = "SELECT u.usuario_id, u.nombre, u.correo, u.password, u.rol_id, "
                   + "u.habilitado, r.nombre_rol "
                   + "FROM usuarios u "
                   + "INNER JOIN roles r ON u.rol_id = r.rol_id "
                   + "WHERE u.correo = ? AND u.password = SHA2(?, 256)";

         try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, correo);
                ps.setString(2, contraseña);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Usuario u = new Usuario();
                        u.setIdUsuario(rs.getInt("usuario_id"));
                        u.setNombre(rs.getString("nombre"));
                        u.setCorreo(rs.getString("correo"));
                        u.setContrasenia(rs.getString("password"));
                        u.setRol(rs.getString("nombre_rol"));
                        u.setEstadoUsuario(rs.getBoolean("habilitado") ? "Activo" : "Inactivo");
                        return u;
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

        String sql = "SELECT u.usuario_id, u.nombre, u.correo, "
                   + "r.nombre_rol, u.rol_id, u.habilitado, u.fecha_registro "
                   + "FROM usuarios u "
                   + "LEFT JOIN roles r ON u.rol_id = r.rol_id";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("usuario_id"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setRol(rs.getString("nombre_rol"));
                u.setRolID(rs.getInt("rol_id"));
                u.setEstadoUsuario(rs.getBoolean("habilitado") ? "Activo" : "Inactivo"); //los unicos 2 estados posibles por el momento
                lista.add(u);
            }

        } catch (SQLException e) {
            logger.error("Error al listar usuarios: " + e.getMessage());
        }

        return lista;
    }

    // =====================================================
    // BUSCAR USUARIOS (por nombre/correo/rol)
    // =====================================================
    public List<Usuario> buscarUsuarios(String filtro) {
        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT u.usuario_id, u.nombre, u.correo, "
                   + "r.nombre_rol, u.rol_id, u.habilitado, u.fecha_registro "
                   + "FROM usuarios u "
                   + "LEFT JOIN roles r ON u.rol_id = r.rol_id "
                   + "WHERE u.nombre LIKE ? "
                   + "OR u.correo LIKE ? "
                   + "OR r.nombre_rol LIKE ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

                String like = "%" + filtro + "%";
                ps.setString(1, like);
                ps.setString(2, like);
                ps.setString(3, like);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Usuario u = new Usuario();
                        u.setIdUsuario(rs.getInt("usuario_id"));
                        u.setNombre(rs.getString("nombre"));
                        u.setCorreo(rs.getString("correo"));
                        u.setRol(rs.getString("nombre_rol"));
                        u.setEstadoUsuario(rs.getBoolean("habilitado") ? "Activo" : "Inactivo");
                        lista.add(u);
                    }
                }
            

        } catch (SQLException e) {
            logger.error("Error al buscar usuarios: " + e.getMessage());
        }

        return lista;
    }

    // =====================================================
    // OBTENER USUARIO POR ID (para editar)
    // =====================================================
    public Usuario obtenerUsuarioPorId(int idUsuario) {
        String sql = "SELECT u.usuario_id, u.nombre, u.correo, u.password, "
                   + "r.nombre_rol, u.rol_id, u.habilitado "
                   + "FROM usuarios u "
                   + "LEFT JOIN roles r ON u.rol_id = r.rol_id "
                   + "WHERE u.usuario_id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setInt(1, idUsuario);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        Usuario u = new Usuario();
                        u.setIdUsuario(rs.getInt("usuario_id"));
                        u.setNombre(rs.getString("nombre"));
                        u.setCorreo(rs.getString("correo"));
                        u.setContrasenia(rs.getString("password"));
                        u.setRol(rs.getString("nombre_rol"));
                        u.setEstadoUsuario(rs.getBoolean("habilitado") ? "Activo" : "Inactivo");
                        return u;
                    }
                }
            

        } catch (SQLException e) {
            logger.error("Error al obtener usuario por ID: " + e.getMessage());
        }

        return null;
    }

    // =====================================================
    // INSERTAR USUARIO
    // =====================================================
    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, correo, password, rol_id, habilitado) "
                   + "VALUES (?, ?, SHA2(?, 256), ?, ?)";

         try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContrasenia());
            ps.setInt(4, Integer.parseInt(usuario.getRol()));
            ps.setBoolean(5, usuario.getEstadoUsuario().equalsIgnoreCase("Activo"));

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            logger.error("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    // =====================================================
    // ACTUALIZAR USUARIO (sin cambiar password)
    // =====================================================
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios "
                   + "SET nombre = ?, correo = ?, rol_id = ?, habilitado = ? "
                   + "WHERE usuario_id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setInt(3, Integer.parseInt(usuario.getRol()));
            ps.setBoolean(4, usuario.getEstadoUsuario().equalsIgnoreCase("Activo"));
            ps.setInt(5, usuario.getIdUsuario());

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
    public boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE usuario_id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            logger.error("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    // =====================================================
    // RESTABLECER CONTRASEÑA
    // =====================================================
    public boolean restablecerContraseña(int idUsuario, String nuevaContraseña) {
        String sql = "UPDATE usuarios SET password = SHA2(?, 256) WHERE usuario_id = ?";

         try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
             
            ps.setString(1, nuevaContraseña);
            ps.setInt(2, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Error al restablecer contraseña: " + e.getMessage());
            return false;
        }
    }

    // ROLES
    public int obtenerRolID(String nombreRol) throws SQLException {
        String sql = "SELECT rol_id FROM roles WHERE nombre_rol = ?";
        try (Connection con = Conexion.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreRol);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("rol_id");
                }
            }
        } catch (SQLException e) {
            logger.error("Error al obtener rol_id: " + e.getMessage());
        }
        return -1; // o 3 si deseas valor por defecto "Alumno"
    }

    public List<String> obtenerRoles() 
    {
        List<String> roles = new ArrayList<>();
        String sql = "SELECT nombre_rol FROM roles WHERE habilitado = TRUE";

         try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                roles.add(rs.getString("nombre_rol"));
            }

        } catch (SQLException e) {
            logger.error("Error al obtener roles: " + e.getMessage());
        }

        return roles;
    }
}
