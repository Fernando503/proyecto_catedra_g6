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
public class Rol {
    private Integer rolID;
    private String nombreRol;
    private String descripcion;
    private Boolean habilitado;

    public Rol() {
    }

    public Rol(Integer rolID, String nombreRol, String descripcion, Boolean habilitado) {
        this.rolID = rolID;
        this.nombreRol = nombreRol;
        this.descripcion = descripcion;
        this.habilitado = habilitado;
    }

    public Integer getRolID() {
        return rolID;
    }

    public void setRolID(Integer rolID) {
        this.rolID = rolID;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }
    
    @Override
    public String toString() {
        return nombreRol;
    }
    
}
