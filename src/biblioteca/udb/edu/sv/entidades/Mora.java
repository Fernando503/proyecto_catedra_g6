/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.entidades;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 *
 * @author Fernando Flamenco
 */
public class Mora {
    private Integer moraID;
    private Prestamo prestamo;
    private Usuario usuario;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer diasRetraso;
    private BigDecimal monto;
    private Boolean pagado;
    private String observaciones;
    private Boolean habilitado;

    public Mora() {
    }

    public Mora(Integer moraID, Prestamo prestamo, Usuario usuario, LocalDate fechaInicio, LocalDate fechaFin, Integer diasRetraso, BigDecimal monto, Boolean pagado, String observaciones, Boolean habilitado) {
        this.moraID = moraID;
        this.prestamo = prestamo;
        this.usuario = usuario;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.diasRetraso = diasRetraso;
        this.monto = monto;
        this.pagado = pagado;
        this.observaciones = observaciones;
        this.habilitado = habilitado;
    }

    public Integer getMoraID() {
        return moraID;
    }

    public void setMoraID(Integer moraID) {
        this.moraID = moraID;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getDiasRetraso() {
        return diasRetraso;
    }

    public void setDiasRetraso(Integer diasRetraso) {
        this.diasRetraso = diasRetraso;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Boolean getPagado() {
        return pagado;
    }

    public void setPagado(Boolean pagado) {
        this.pagado = pagado;
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
