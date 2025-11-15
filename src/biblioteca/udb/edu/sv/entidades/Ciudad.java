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
public class Ciudad {
    private Integer ciudadID;
    private String nombre;
    private Pais pais;
    private Boolean habilitado;

    public Ciudad() {
    }

    public Ciudad(Integer ciudadID, String nombre, Pais pais, Boolean habilitado) {
        this.ciudadID = ciudadID;
        this.nombre = nombre;
        this.pais = pais;
        this.habilitado = habilitado;
    }

    public Integer getCiudadID() {
        return ciudadID;
    }

    public void setCiudadID(Integer ciudadID) {
        this.ciudadID = ciudadID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }
    
    @Override
    public String toString() {
        return nombre;
    }
    
}
