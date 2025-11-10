package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.UsuarioDAO;
import biblioteca.udb.edu.sv.entidades.Usuario;
import java.util.List;

public class UsuarioController {
    
    private final UsuarioDAO usuarioDAO;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    /**
     * Intenta iniciar sesión con las credenciales proporcionadas.
     * @param correo Correo del usuario (campo "Usuario" del login).
     * @param contraseña Contraseña del usuario.
     * @return Usuario válido si las credenciales son correctas, null en caso contrario.
     */
    public Usuario iniciarSesion(String correo, String contraseña) {
        return usuarioDAO.obtenerUsuarioPorCredenciales(correo, contraseña);
    }

    // Métodos para el CRUD del formulario de gestión

    public List<Usuario> listarUsuarios() {
        return usuarioDAO.listarUsuarios();
    }

    public boolean agregarUsuario(Usuario usuario) {
        return usuarioDAO.insertarUsuario(usuario);
    }

    public boolean editarUsuario(Usuario usuario) {
        return usuarioDAO.actualizarUsuario(usuario);
    }

    public boolean eliminarUsuario(int idUsuario) {
        return usuarioDAO.eliminarUsuario(idUsuario);
    }
    
    public boolean restablecerContraseña(int idUsuario) {
        return usuarioDAO.restablecerContraseña(idUsuario, "1234");
    }
    
    public boolean esUsuarioMoroso(int idUsuario) {
        return usuarioDAO.esUsuarioMoroso(idUsuario);
    }
}