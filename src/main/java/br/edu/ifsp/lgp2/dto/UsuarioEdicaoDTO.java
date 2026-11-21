package br.edu.ifsp.lgp2.dto;

import br.edu.ifsp.lgp2.model.Papel;

public class UsuarioEdicaoDTO {
    private Long id;
    private String login;
    private String nome;
    private String senha;
    private Papel papel;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Papel getPapel() { return papel; }
    public void setPapel(Papel papel) { this.papel = papel; }
}
