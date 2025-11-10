/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.DAO;

import biblioteca.udb.edu.sv.entidades.Usuario;
import biblioteca.udb.edu.sv.tools.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Fernando Flamenco
 */
public class UsuarioDAO {
    
    /**
     * Busca un usuario por correo y contraseña.
     * Retorna un objeto Usuario si las credenciales son válidas,
     * o null si no coinciden.
     */
    public Usuario obtenerUsuarioPorCredenciales(String correo, String contraseña) {
        String sql = "SELECT u.UsuarioID, u.Nombre, u.Correo, u.Contraseña, "
                   + "u.EstadoUsuario, r.NombreRol "
                   + "FROM Usuarios u "
                   + "INNER JOIN Roles r ON u.RolID = r.RolID "
                   + "WHERE u.Correo = ? AND u.Contraseña = ? "
                   + "AND u.EstadoUsuario = 'Activo'";
        
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
}
