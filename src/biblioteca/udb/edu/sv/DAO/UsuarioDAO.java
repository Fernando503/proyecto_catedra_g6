package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.*;
import biblioteca.udb.edu.sv.tools.AuditoriaLogger;
import biblioteca.udb.edu.sv.tools.Conexion;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class UsuarioDAO {

    private static final Logger logger = LogManager.getLogger(UsuarioDAO.class);

    public Usuario obtenerUsuarioPorCredenciales(String correo, String contraseña) {
        String sql = "SELECT u.usuario_id, u.nombre, u.correo, u.password, u.habilitado, u.fecha_registro, " +
                     "r.rol_id, r.nombre_rol, r.descripcion, r.habilitado AS rol_habilitado " +
                     "FROM usuarios u " +
                     "INNER JOIN roles r ON u.rol_id = r.rol_id " +
                     "WHERE u.correo = ? AND u.password = SHA2(?, 256)";

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
                    u.setHabilitado(rs.getBoolean("habilitado"));

                    Timestamp ts = rs.getTimestamp("fecha_registro");
                    if (ts != null) {
                        u.setFechaRegistro(ts.toLocalDateTime());
                    }

                    Rol rol = new Rol();
                    rol.setRolID(rs.getInt("rol_id"));
                    rol.setNombreRol(rs.getString("nombre_rol"));
                    rol.setDescripcion(rs.getString("descripcion"));
                    rol.setHabilitado(rs.getBoolean("rol_habilitado"));
                    
                    u.setRol(rol);
                    return u;
                }
            }

        } catch (SQLException e) {
            logger.error("Error al obtener usuario por credenciales: " + e.getMessage());
        }

        return null;
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT u.usuario_id, u.nombre, u.correo, u.password, u.habilitado, u.fecha_registro, " +
                     "r.rol_id, r.nombre_rol, r.descripcion, r.habilitado AS rol_habilitado " +
                     "FROM usuarios u " +
                     "LEFT JOIN roles r ON u.rol_id = r.rol_id";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("usuario_id"));
                u.setNombre(rs.getString("nombre"));
                u.setCorreo(rs.getString("correo"));
                u.setContrasenia(rs.getString("password"));
                u.setHabilitado(rs.getBoolean("habilitado"));

                Timestamp ts = rs.getTimestamp("fecha_registro");
                if (ts != null) {
                    u.setFechaRegistro(ts.toLocalDateTime());
                }

                Rol rol = new Rol();
                rol.setRolID(rs.getInt("rol_id"));
                rol.setNombreRol(rs.getString("nombre_rol"));
                rol.setDescripcion(rs.getString("descripcion"));
                rol.setHabilitado(rs.getBoolean("rol_habilitado"));

                u.setRol(rol);

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

        String sql = "SELECT u.usuario_id, u.nombre, u.correo, u.password, u.habilitado, u.fecha_registro, " +
                     "r.rol_id, r.nombre_rol, r.descripcion, r.habilitado AS rol_habilitado "
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
                        u.setContrasenia(rs.getString("password"));
                        u.setHabilitado(rs.getBoolean("habilitado"));

                        Timestamp ts = rs.getTimestamp("fecha_registro");
                        if (ts != null) {
                            u.setFechaRegistro(ts.toLocalDateTime());
                        }

                        Rol rol = new Rol();
                        rol.setRolID(rs.getInt("rol_id"));
                        rol.setNombreRol(rs.getString("nombre_rol"));
                        rol.setDescripcion(rs.getString("descripcion"));
                        rol.setHabilitado(rs.getBoolean("rol_habilitado"));

                        u.setRol(rol);

                        lista.add(u);
                    }
                }
            

        } catch (SQLException e) {
            logger.error("Error al buscar usuarios: " + e.getMessage());
        }

        return lista;
    }

    // =====================================================
    // INSERTAR USUARIO
    // =====================================================
    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, correo, password, rol_id, habilitado) " +
                     "VALUES (?, ?, SHA2(?, 256), ?, ?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContrasenia());
            ps.setInt(4, usuario.getRol().getRolID());
            ps.setBoolean(5, usuario.getHabilitado());

            ps.executeUpdate();

            // Auditoría
            AuditoriaLogger.registrar("CREAR_USUARIO", "Se creó el usuario: " + usuario.getCorreo());
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
        String sql = "UPDATE usuarios " +
                     "SET nombre = ?, correo = ?, rol_id = ?, habilitado = ? " +
                     "WHERE usuario_id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setInt(3, usuario.getRol().getRolID());
            ps.setBoolean(4, usuario.getHabilitado());
            ps.setInt(5, usuario.getIdUsuario());

            ps.executeUpdate();

            // Auditoría
            AuditoriaLogger.registrar("ACTUALIZAR_USUARIO", "Se actualizó el usuario ID: " + usuario.getIdUsuario());

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
            
            // Auditoría
            AuditoriaLogger.registrar("ELIMINAR_USUARIO", "Se actualizó el usuario ID: " + idUsuario);
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

            // Auditoría
            AuditoriaLogger.registrar("CAMBIAR_CONTRASEÑA_USUARIO", "Se actualizó la contraseña para el usuario con ID: " + idUsuario);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("Error al restablecer contraseña: " + e.getMessage());
            return false;
        }
    }


    public List<String> obtenerRoles() {
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
