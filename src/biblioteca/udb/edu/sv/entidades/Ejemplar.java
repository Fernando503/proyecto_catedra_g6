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
    private Integer ubicacion;
    private EstadoEjemplar estadoEjemplar;
    private LocalDate fechaAdquisicion;
    private String observaciones;
    private Boolean habilitado; 

    public Ejemplar() {
    }

    public Integer getEjemplarID() {
        return ejemplarID;
    }

    public void setEjemplarID(Integer ejemplarID) {
        this.ejemplarID = ejemplarID;
    }
    
    
}
