package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.UsuarioDAO;
import biblioteca.udb.edu.sv.entidades.Usuario;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.util.List;
import org.apache.log4j.Logger;

public class UsuarioController {
    
    private static final Logger logger = LogManager.getLogger(UsuarioController.class);
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
        try {
            return usuarioDAO.obtenerUsuarioPorCredenciales(correo, contraseña);
        } catch (Exception e) {
            logger.error("Error en iniciarSesion: " + e.getMessage());
            return null;
        }
    }


    public List<Usuario> listarUsuarios() {
        try {
            return usuarioDAO.listarUsuarios();
        } catch (Exception e) {
            logger.error("Error al listar usuarios: " + e.getMessage());
            return null;
        }
    }

    public boolean agregarUsuario(Usuario usuario) {
        try {
            return usuarioDAO.insertarUsuario(usuario);
        } catch (Exception e) {
            logger.error("Error al agregar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean editarUsuario(Usuario usuario) {
        try {
            return usuarioDAO.actualizarUsuario(usuario);
        } catch (Exception e) {
            logger.error("Error al editar usuario: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarUsuario(int idUsuario) {
        try {
            return usuarioDAO.eliminarUsuario(idUsuario);
        } catch (Exception e) {
            logger.error("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Restablece la contraseña de un usuario a un valor por defecto.
     * Ejemplo: "1234" o lo que quieras usar.
     */
    public boolean restablecerContraseña(int idUsuario) {
        try {
            // aquí puedes cambiar "1234" por lo que quieras como default
            return usuarioDAO.restablecerContraseña(idUsuario, "1234");
        } catch (Exception e) {
            logger.error("Error al restablecer contraseña: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si un usuario tiene moras activas (NO pagadas).
     * Requiere que tengas implementado el método esUsuarioMoroso en UsuarioDAO.
     */
    public boolean esUsuarioMoroso(int idUsuario) {
        try {
            return usuarioDAO.esUsuarioMoroso(idUsuario);
        } catch (Exception e) {
            logger.error("Error al verificar si usuario es moroso: " + e.getMessage());
            return false;
        }
    }
}
