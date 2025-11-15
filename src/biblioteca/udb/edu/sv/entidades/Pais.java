/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.entidades;

/**
 *
 * @author Fernando Flamenco
 */
public class Pais {
    private Integer paisID;
    private String nombre;
    private Boolean Habilitado;

    public Pais() {
    }

    public Pais(Integer paisID, String nombre, Boolean Habilitado) {
        this.paisID = paisID;
        this.nombre = nombre;
        this.Habilitado = Habilitado;
    }

    public Integer getPaisID() {
        return paisID;
    }

    public void setPaisID(Integer paisID) {
        this.paisID = paisID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getHabilitado() {
        return Habilitado;
    }

    public void setHabilitado(Boolean Habilitado) {
        this.Habilitado = Habilitado;
    }
    
    @Override
    public String toString() {
        return nombre;
    }

}
