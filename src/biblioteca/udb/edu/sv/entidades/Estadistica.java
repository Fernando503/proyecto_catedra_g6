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
public class Estadistica {
    private int totalDocumentos;
    private int totalEjemplares;
    private int prestamosActivos;
    private int morasPendientes;

    // Getters y Setters
    public int getTotalDocumentos() { return totalDocumentos; }
    public void setTotalDocumentos(int totalDocumentos) { this.totalDocumentos = totalDocumentos; }

    public int getTotalEjemplares() { return totalEjemplares; }
    public void setTotalEjemplares(int totalEjemplares) { this.totalEjemplares = totalEjemplares; }

    public int getPrestamosActivos() { return prestamosActivos; }
    public void setPrestamosActivos(int prestamosActivos) { this.prestamosActivos = prestamosActivos; }

    public int getMorasPendientes() { return morasPendientes; }
    public void setMorasPendientes(int morasPendientes) { this.morasPendientes = morasPendientes; }

}
