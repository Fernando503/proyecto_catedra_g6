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
public class TipoDocumento {
    private Integer tipoDocumentoID;
    private String nombre;
    private String descripcion;
    private Boolean habilitado;

    public TipoDocumento() {
    }

    public TipoDocumento(Integer tipoDocumentoID, String nombre, String descripcion, Boolean habilitado) {
        this.tipoDocumentoID = tipoDocumentoID;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.habilitado = habilitado;
    }

    public Integer getTipoDocumentoID() {
        return tipoDocumentoID;
    }

    public void setTipoDocumentoID(Integer tipoDocumentoID) {
        this.tipoDocumentoID = tipoDocumentoID;
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
