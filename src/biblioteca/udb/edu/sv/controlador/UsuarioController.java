package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.UsuarioDAO;
import biblioteca.udb.edu.sv.entidades.Rol;
import biblioteca.udb.edu.sv.entidades.Usuario;
import biblioteca.udb.edu.sv.tools.LogManager;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public class UsuarioController {

    private static final Logger logger = LogManager.getLogger(UsuarioController.class);
    private final UsuarioDAO usuarioDAO;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    // LOGIN
    public Usuario iniciarSesion(String correo, String contraseña) {
        try {
            return usuarioDAO.obtenerUsuarioPorCredenciales(correo, contraseña);
        } catch (Exception e) {
            logger.error("Error en iniciarSesion: " + e.getMessage());
            return null;
        }
    }

    // LISTAR
    public List<Usuario> listarUsuarios() {
        try {
            return usuarioDAO.listarUsuarios();
        } catch (Exception e) {
            logger.error("Error al listar usuarios: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // BUSCAR
    public List<Usuario> buscarUsuarios(String filtro) {
        try {
            if (filtro == null || filtro.trim().isEmpty()) {
                return listarUsuarios();
            }
            return usuarioDAO.buscarUsuarios(filtro.trim());
        } catch (Exception e) {
            logger.error("Error al buscar usuarios: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // INSERTAR
    public boolean agregarUsuario(Usuario u) {
        try {
            return usuarioDAO.insertarUsuario(u);
        } catch (Exception e) {
            logger.error("Error al agregar usuario: " + e.getMessage());
            return false;
        }
    }

    // ACTUALIZAR
    public boolean editarUsuario(Usuario u) {
        try {
            return usuarioDAO.actualizarUsuario(u);
        } catch (Exception e) {
            logger.error("Error al editar usuario: " + e.getMessage());
            return false;
        }
    }

    // ELIMINAR
    public boolean eliminarUsuario(int idUsuario) {
        try {
            return usuarioDAO.eliminarUsuario(idUsuario);
        } catch (Exception e) {
            logger.error("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    // RESTABLECER CONTRASEÑA (siempre 1234 como dijiste)
    public boolean restablecerContraseña(int idUsuario) {
        try {
            return usuarioDAO.restablecerContraseña(idUsuario, "1234");
        } catch (Exception e) {
            logger.error("Error al restablecer contraseña: " + e.getMessage());
            return false;
        }
    }

    // OBTENER ROLES PARA EL COMBO
    public List<Rol> obtenerRoles() {
        try {
            return usuarioDAO.obtenerRoles();
        } catch (Exception e) {
            logger.error("Error al obtener roles: " + e.getMessage());
            return Collections.emptyList();
        }
    }
    
    public Rol obtenerRolByNombre(String nombreRol){
        try {
            return usuarioDAO.obtenerRolByNombre(nombreRol);
        } catch (Exception e) {
            logger.error("Error al obtener rol: " + e.getMessage());
            return null;
        }
    }
}
