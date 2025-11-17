/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.entidades;

import java.time.LocalDate;

/**
 *
 * @author Fernando Flamenco
 */
public class Ejemplar {
    private Integer ejemplarID;
    private Documento documento;
    private String codigoBarra;
    private Ubicacion ubicacion;
    private EstadoEjemplar estadoEjemplar;
    private LocalDate fechaAdquisicion;
    private String observaciones;
    private Boolean habilitado; 

    public Ejemplar() {
    }

    public Ejemplar(Integer ejemplarID, Documento documento, String codigoBarra, Ubicacion ubicacion, EstadoEjemplar estadoEjemplar, LocalDate fechaAdquisicion, String observaciones, Boolean habilitado) {
        this.ejemplarID = ejemplarID;
        this.documento = documento;
        this.codigoBarra = codigoBarra;
        this.ubicacion = ubicacion;
        this.estadoEjemplar = estadoEjemplar;
        this.fechaAdquisicion = fechaAdquisicion;
        this.observaciones = observaciones;
        this.habilitado = habilitado;
    }

    public Integer getEjemplarID() {
        return ejemplarID;
    }

    public void setEjemplarID(Integer ejemplarID) {
        this.ejemplarID = ejemplarID;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public EstadoEjemplar getEstadoEjemplar() {
        return estadoEjemplar;
    }

    public void setEstadoEjemplar(EstadoEjemplar estadoEjemplar) {
        this.estadoEjemplar = estadoEjemplar;
    }

    public LocalDate getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(LocalDate fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }


    
    
}
