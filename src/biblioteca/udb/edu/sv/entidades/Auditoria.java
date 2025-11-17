/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biblioteca.udb.edu.sv.entidades;

import java.time.LocalDateTime;

/**
 *
 * @author Fernando Flamenco
 */
public class Auditoria {
    private int idAuditoria;
    private Usuario usuario; 
    private LocalDateTime fechaAccion;
    private String tipoAccion;
    private String detalleAccion;

    public Auditoria() {
    }

    public Auditoria(int idAuditoria, Usuario usuario, LocalDateTime fechaAccion, String tipoAccion, String detalleAccion) {
        this.idAuditoria = idAuditoria;
        this.usuario = usuario;
        this.fechaAccion = fechaAccion;
        this.tipoAccion = tipoAccion;
        this.detalleAccion = detalleAccion;
    }

    public int getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(int idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaAccion() {
        return fechaAccion;
    }

    public void setFechaAccion(LocalDateTime fechaAccion) {
        this.fechaAccion = fechaAccion;
    }

    public String getTipoAccion() {
        return tipoAccion;
    }

    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    public String getDetalleAccion() {
        return detalleAccion;
    }

    public void setDetalleAccion(String detalleAccion) {
        this.detalleAccion = detalleAccion;
    }
    
    
}
