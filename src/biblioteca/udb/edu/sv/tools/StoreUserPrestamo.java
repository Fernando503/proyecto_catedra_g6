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
public class StoreUserPrestamo {
    private static StoreUserPrestamo instancia;
    private int idUsuario;
    private String nombre;
    private String correo;
    
    public static StoreUserPrestamo getInstancia() {
        if (instancia == null) {
            instancia = new StoreUserPrestamo();
        }
        return instancia;
    }

    public void DatosPrestamosSesion(int idUsuario, String nombre, String correo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
    }

    public int getIdUsuario() { return idUsuario; }

    public String getNombre() { return nombre; }
    
    public String getCorreo() { return correo; }

    public void limpiarInstancia() { instancia = null; }
}
