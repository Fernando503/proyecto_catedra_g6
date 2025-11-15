package biblioteca.udb.edu.sv.entidades;

public class Documento {
    private int documentoID;
    private String titulo;
    private String autor;
    private String tipo;
    private String categoria;
    private String editorial;
    private String idioma;
    private String formato;
    private Boolean estado;
    private int añoPublicacion;
    private int paginas;
    private String observaciones;
    private String codigo;

    // Getters y setters
    public int getDocumentoID() { return documentoID; }
    public void setDocumentoID(int id) { this.documentoID = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String t) { this.titulo = t; }

    public String getAutor() { return autor; }
    public void setAutor(String a) { this.autor = a; }

    public String getTipo() { return tipo; }
    public void setTipo(String t) { this.tipo = t; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String c) { this.categoria = c; }

    public String getEditorial() { return editorial; }
    public void setEditorial(String e) { this.editorial = e; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String i) { this.idioma = i; }

    public String getFormato() { return formato; }
    public void setFormato(String f) { this.formato = f; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean e) { this.estado = e; }

    public int getAñoPublicacion() { return añoPublicacion; }
    public void setAñoPublicacion(int a) { this.añoPublicacion = a; }

    public int getPaginas() { return paginas; }
    public void setPaginas(int p) { this.paginas = p; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String o) { this.observaciones = o; }
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String c) { this.codigo = c; }
}
