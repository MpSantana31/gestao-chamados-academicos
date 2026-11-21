package br.edu.ifsp.lgp2.dto;

import br.edu.ifsp.lgp2.model.Papel;

public class UsuarioCadastroDTO {
    private String login;
    private String nome;
    private String senha;
    private Papel papel;

    public UsuarioCadastroDTO() {
    }

    public UsuarioCadastroDTO(String login, String nome, String senha, Papel papel) {
        this.login = login;
        this.nome = nome;
        this.senha = senha;
        this.papel = papel;
    }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Papel getPapel() { return papel; }
    public void setPapel(Papel papel) { this.papel = papel; }
}
