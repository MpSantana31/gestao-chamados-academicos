package br.edu.ifsp.lgp2.dto;

/*
 * DTO usado no cadastro de chamado.
 * Ele recebe apenas os campos informados pelo usuário.
 */
public class ChamadoCadastroDTO {

    private String titulo;
    private String descricao;
    private String categoria;

    public ChamadoCadastroDTO() {
    }

    public ChamadoCadastroDTO(String titulo, String descricao, String categoria) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
