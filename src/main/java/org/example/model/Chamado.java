package org.example.model;

public class Chamado {
    private int id;
    private String titulo;
    private String descricao;
    private String status;
    private Usuario requerente;
    private Categoria categoria;

    public Chamado(int id, String titulo, String descricao, String status, Usuario requerente, Categoria categoria) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.status = status;
        this.requerente = requerente;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Usuario getRequerente() {
        return requerente;
    }

    public void setRequerente(Usuario requerente) {
        this.requerente = requerente;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
