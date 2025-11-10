/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.controlador;

import biblioteca.udb.edu.sv.DAO.UsuarioDAO;
import biblioteca.udb.edu.sv.entidades.Usuario;

/**
 *
 * @author Fernando Flamenco
 */
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
}
