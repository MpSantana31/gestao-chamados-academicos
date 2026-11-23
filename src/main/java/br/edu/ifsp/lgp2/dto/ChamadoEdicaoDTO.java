package br.edu.ifsp.lgp2.dto;

import br.edu.ifsp.lgp2.model.Prioridade;
import br.edu.ifsp.lgp2.model.StatusChamado;

/*
 * DTO usado na edição de chamado.
 *
 * A interface web e a API REST compartilham este mesmo DTO, assim como o
 * Service. Ele transporta apenas os campos que o administrador pode alterar:
 * a data de abertura e o requerente continuam sob responsabilidade do Service
 * e da Factory, e por isso não aparecem aqui.
 */
public class ChamadoEdicaoDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private String categoria;
    private StatusChamado status;
    private Prioridade prioridade;
    private String responsavel;
    private String observacaoAtendimento;

    public ChamadoEdicaoDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public StatusChamado getStatus() {
        return status;
    }

    public void setStatus(StatusChamado status) {
        this.status = status;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getObservacaoAtendimento() {
        return observacaoAtendimento;
    }

    public void setObservacaoAtendimento(String observacaoAtendimento) {
        this.observacaoAtendimento = observacaoAtendimento;
    }
}
