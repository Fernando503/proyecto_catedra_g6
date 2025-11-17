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
public class Configuracion {
    private Integer idConfig;
    private String parametro;
    private String valor;
    private String descripcion;
    private Boolean habilitado;

    public Configuracion() {
    }

    public Configuracion(Integer idConfig, String parametro, String valor, String descripcion, Boolean habilitado) {
        this.idConfig = idConfig;
        this.parametro = parametro;
        this.valor = valor;
        this.descripcion = descripcion;
        this.habilitado = habilitado;
    }

    public Integer getIdConfig() {
        return idConfig;
    }

    public void setIdConfig(Integer idConfig) {
        this.idConfig = idConfig;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
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
