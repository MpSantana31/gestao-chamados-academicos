package br.edu.ifsp.lgp2.dto;

import br.edu.ifsp.lgp2.model.Papel;

public class UsuarioResponseDTO {
    private Long id;
    private String login;
    private String nome;
    private Papel papel;

    public UsuarioResponseDTO(Long id, String login, String nome, Papel papel) {
        this.id = id;
        this.login = login;
        this.nome = nome;
        this.papel = papel;
    }

    public Long getId() { return id; }
    public String getLogin() { return login; }
    public String getNome() { return nome; }
    public Papel getPapel() { return papel; }
}
