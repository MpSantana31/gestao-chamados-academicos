package br.edu.ifsp.lgp2.dto;

import br.edu.ifsp.lgp2.model.StatusChamado;

/*
 * DTO da atividade prática.
 *
 * Ele permite alterar apenas o status e a observação de atendimento.
 * Assim, a tela de atendimento não altera indevidamente título, descrição,
 * categoria, prioridade, responsável ou data de abertura.
 */
public class ChamadoAtualizacaoStatusDTO {

    private Long id;
    private StatusChamado status;
    private String observacaoAtendimento;

    public ChamadoAtualizacaoStatusDTO() {
    }

    public ChamadoAtualizacaoStatusDTO(Long id, StatusChamado status, String observacaoAtendimento) {
        this.id = id;
        this.status = status;
        this.observacaoAtendimento = observacaoAtendimento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public StatusChamado getStatus() {
        return status;
    }

    public void setStatus(StatusChamado status) {
        this.status = status;
    }

    public String getObservacaoAtendimento() {
        return observacaoAtendimento;
    }

    public void setObservacaoAtendimento(String observacaoAtendimento) {
        this.observacaoAtendimento = observacaoAtendimento;
    }
}
