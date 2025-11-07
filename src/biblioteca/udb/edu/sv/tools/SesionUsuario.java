/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.tools;

/**
 *
 * @author Fernando Flamenco
 */
public class SesionUsuario {
    private static SesionUsuario instancia;
    private int idUsuario;
    private String nombre;
    private String rol;

    private SesionUsuario() {}

    public static SesionUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SesionUsuario();
        }
        return instancia;
    }

    public void iniciarSesion(int idUsuario, String nombre, String rol) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.rol = rol;
    }

    public String getRol() { return rol; }

    public int getIdUsuario() { return idUsuario; }

    public String getNombre() { return nombre; }

    public void cerrarSesion() { instancia = null; }
}
