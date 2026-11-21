package br.edu.ifsp.lgp2.dto;

/*
 * DTO da atividade prática.
 *
 * Ele permite alterar apenas o status e a observação de atendimento.
 * Assim, a tela de atendimento não altera indevidamente título, descrição,
 * categoria, prioridade, responsável ou data de abertura.
 */
public class ChamadoAtualizacaoStatusDTO {

    private Long id;
    private String status;
    private String observacaoAtendimento;

    public ChamadoAtualizacaoStatusDTO() {
    }

    public ChamadoAtualizacaoStatusDTO(Long id, String status, String observacaoAtendimento) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacaoAtendimento() {
        return observacaoAtendimento;
    }

    public void setObservacaoAtendimento(String observacaoAtendimento) {
        this.observacaoAtendimento = observacaoAtendimento;
    }
}
