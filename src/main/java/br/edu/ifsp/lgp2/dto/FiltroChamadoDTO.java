package br.edu.ifsp.lgp2.dto;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/** DTO de consulta: filtros recebidos da interface e da API, sem expor regras de persistência. */
public class FiltroChamadoDTO {
    private String busca;
    private String status;
    private String categoria;
    private String prioridade;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate inicio;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fim;
    public String getBusca(){return busca;}
    public void setBusca(String busca){this.busca=busca;}
    public String getStatus(){return status;}
    public void setStatus(String status){this.status=status;}
    public String getCategoria(){return categoria;}
    public void setCategoria(String categoria){this.categoria=categoria;}
    public String getPrioridade(){return prioridade;}
    public void setPrioridade(String prioridade){this.prioridade=prioridade;}
    public LocalDate getInicio(){return inicio;}
    public void setInicio(LocalDate inicio){this.inicio=inicio;}
    public LocalDate getFim(){return fim;}
    public void setFim(LocalDate fim){this.fim=fim;}
}
