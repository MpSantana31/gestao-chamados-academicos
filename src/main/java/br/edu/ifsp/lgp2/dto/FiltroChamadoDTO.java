package br.edu.ifsp.lgp2.dto;

import br.edu.ifsp.lgp2.model.Prioridade;
import br.edu.ifsp.lgp2.model.StatusChamado;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/** DTO de consulta: filtros recebidos da interface e da API, sem expor regras de persistência. */
public class FiltroChamadoDTO {
    private String busca;
    private StatusChamado status;
    private String categoria;
    private Prioridade prioridade;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate inicio;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fim;
    public String getBusca(){return busca;}
    public void setBusca(String busca){this.busca=busca;}
    public StatusChamado getStatus(){return status;}
    public void setStatus(StatusChamado status){this.status=status;}
    public String getCategoria(){return categoria;}
    public void setCategoria(String categoria){this.categoria=categoria;}
    public Prioridade getPrioridade(){return prioridade;}
    public void setPrioridade(Prioridade prioridade){this.prioridade=prioridade;}
    public LocalDate getInicio(){return inicio;}
    public void setInicio(LocalDate inicio){this.inicio=inicio;}
    public LocalDate getFim(){return fim;}
    public void setFim(LocalDate fim){this.fim=fim;}
}
