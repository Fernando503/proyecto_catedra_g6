package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.Usuario;
import biblioteca.udb.edu.sv.tools.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario validarUsuario(String correo, String contraseña) {
        String sql = "SELECT u.UsuarioID, u.Nombre, r.NombreRol " +
                     "FROM Usuarios u JOIN Roles r ON u.RolID = r.RolID " +
                     "WHERE u.Correo = ? AND u.Contraseña = ? AND u.EstadoUsuario = 'Activo'";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, correo);
            ps.setString(2, contraseña);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setUsuarioID(rs.getInt("UsuarioID"));
                usuario.setNombre(rs.getString("Nombre"));
                usuario.setRol(rs.getString("NombreRol"));
                return usuario;
            }

        } catch (SQLException e) {
            System.err.println("❌ Error al validar el usuario: " + e.getMessage());
        }

        return null;
    }
}