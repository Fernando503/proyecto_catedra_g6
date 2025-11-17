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
public class Editorial {
    private Integer editorialID;
    private String nombreEditorial;
    private Pais paisEditorial;
    private Ciudad ciudadEditorial;
    private String sitioWeb;
    private String contactoEmail;
    private String contactoPhone;
    private String observaciones;
    private Boolean habilitado;

    public Editorial() {
    }

    public Editorial(Integer editorialID, String nombreEditorial, Pais paisEditorial, Ciudad ciudadEditorial, String sitioWeb, String contactoEmail, String contactoPhone, String observaciones, Boolean habilitado) {
        this.editorialID = editorialID;
        this.nombreEditorial = nombreEditorial;
        this.paisEditorial = paisEditorial;
        this.ciudadEditorial = ciudadEditorial;
        this.sitioWeb = sitioWeb;
        this.contactoEmail = contactoEmail;
        this.contactoPhone = contactoPhone;
        this.observaciones = observaciones;
        this.habilitado = habilitado;
    }

    public Integer getEditorialID() {
        return editorialID;
    }

    public void setEditorialID(Integer editorialID) {
        this.editorialID = editorialID;
    }

    public String getNombreEditorial() {
        return nombreEditorial;
    }

    public void setNombreEditorial(String nombreEditorial) {
        this.nombreEditorial = nombreEditorial;
    }

    public Pais getPaisEditorial() {
        return paisEditorial;
    }

    public void setPaisEditorial(Pais paisEditorial) {
        this.paisEditorial = paisEditorial;
    }

    public Ciudad getCiudadEditorial() {
        return ciudadEditorial;
    }

    public void setCiudadEditorial(Ciudad ciudadEditorial) {
        this.ciudadEditorial = ciudadEditorial;
    }

    public String getSitioWeb() {
        return sitioWeb;
    }

    public void setSitioWeb(String sitioWeb) {
        this.sitioWeb = sitioWeb;
    }

    public String getContactoEmail() {
        return contactoEmail;
    }

    public void setContactoEmail(String contactoEmail) {
        this.contactoEmail = contactoEmail;
    }

    public String getContactoPhone() {
        return contactoPhone;
    }

    public void setContactoPhone(String contactoPhone) {
        this.contactoPhone = contactoPhone;
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
