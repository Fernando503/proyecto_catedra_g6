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
public class Ubicacion {
    private Integer ubicacionID;
    private String sala;
    private String estanteria;
    private String nivel;
    private String codigoRack;
    private String descripcion;
    private Boolean habilitado;

    public Ubicacion() {
    }

    public Ubicacion(Integer ubicacionID, String sala, String estanteria, String nivel, String codigoRack, String descripcion, Boolean habilitado) {
        this.ubicacionID = ubicacionID;
        this.sala = sala;
        this.estanteria = estanteria;
        this.nivel = nivel;
        this.codigoRack = codigoRack;
        this.descripcion = descripcion;
        this.habilitado = habilitado;
    }

    public Integer getUbicacionID() {
        return ubicacionID;
    }

    public void setUbicacionID(Integer ubicacionID) {
        this.ubicacionID = ubicacionID;
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public String getEstanteria() {
        return estanteria;
    }

    public void setEstanteria(String estanteria) {
        this.estanteria = estanteria;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getCodigoRack() {
        return codigoRack;
    }

    public void setCodigoRack(String codigoRack) {
        this.codigoRack = codigoRack;
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
