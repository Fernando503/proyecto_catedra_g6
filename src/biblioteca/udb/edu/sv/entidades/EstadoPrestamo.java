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
public class EstadoPrestamo {
    private Integer estadoPrestamoID;
    private String nombre;
    private String descripcion;
    private Boolean habilitado;

    public EstadoPrestamo() {
    }

    public EstadoPrestamo(Integer estadoPrestamoID, String nombre, String descripcion, Boolean habilitado) {
        this.estadoPrestamoID = estadoPrestamoID;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.habilitado = habilitado;
    }

    public Integer getEstadoPrestamoID() {
        return estadoPrestamoID;
    }

    public void setEstadoPrestamoID(Integer estadoPrestamoID) {
        this.estadoPrestamoID = estadoPrestamoID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
    
    
    
}
