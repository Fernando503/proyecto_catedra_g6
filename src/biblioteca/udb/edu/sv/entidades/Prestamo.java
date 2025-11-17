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
public class Prestamo {
    private Integer prestamoId;
    private Usuario usuario;
    private Ejemplar ejemplar;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionPrevista;
    private LocalDate fechaDevolucionReal;
    private EstadoPrestamo estadoPrestamo;
    private String observaciones;
    private Boolean habilitado = true;

    public Prestamo() {
    }

    public Prestamo(Integer prestamoId, Usuario usuario, Ejemplar ejemplar, LocalDate fechaPrestamo, LocalDate fechaDevolucionPrevista, LocalDate fechaDevolucionReal, EstadoPrestamo estadoPrestamo, String observaciones) {
        this.prestamoId = prestamoId;
        this.usuario = usuario;
        this.ejemplar = ejemplar;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.fechaDevolucionReal = fechaDevolucionReal;
        this.estadoPrestamo = estadoPrestamo;
        this.observaciones = observaciones;
    }

    public Integer getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(Integer prestamoId) {
        this.prestamoId = prestamoId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Ejemplar getEjemplar() {
        return ejemplar;
    }

    public void setEjemplar(Ejemplar ejemplar) {
        this.ejemplar = ejemplar;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucionPrevista() {
        return fechaDevolucionPrevista;
    }

    public void setFechaDevolucionPrevista(LocalDate fechaDevolucionPrevista) {
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public EstadoPrestamo getEstadoPrestamo() {
        return estadoPrestamo;
    }

    public void setEstadoPrestamo(EstadoPrestamo estadoPrestamo) {
        this.estadoPrestamo = estadoPrestamo;
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
