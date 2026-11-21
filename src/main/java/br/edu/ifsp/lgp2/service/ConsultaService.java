package br.edu.ifsp.lgp2.service;

import br.edu.ifsp.lgp2.dto.FiltroChamadoDTO;
import br.edu.ifsp.lgp2.dto.IndicadoresDTO;
import br.edu.ifsp.lgp2.model.Chamado;
import br.edu.ifsp.lgp2.repository.ChamadoRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Locale;

/** Camada Service: concentra consultas dinâmicas e regras de indicadores. */
@Service
public class ConsultaService {
    private final ChamadoRepository repository;
    public ConsultaService(ChamadoRepository repository) { this.repository = repository; }

    /** Cada filtro opcional gera um predicado AND; filtros vazios não restringem a consulta. */
    public List<Chamado> consultar(FiltroChamadoDTO filtro) {
        Specification<Chamado> regra = (raiz, query, cb) -> cb.conjunction();
        if (filtro.getBusca() != null && !filtro.getBusca().isBlank()) {
            String termo = "%" + filtro.getBusca().trim().toLowerCase(Locale.ROOT) + "%";
            regra = regra.and((r,q,cb) -> cb.or(
                cb.like(cb.lower(r.get("titulo")), termo),
                cb.like(cb.lower(r.get("descricao")), termo)));
        }
        if (filtro.getStatus() != null && !filtro.getStatus().isBlank()) {
            regra = regra.and((r,q,cb) -> cb.equal(r.get("status"), filtro.getStatus()));
        }
        if (filtro.getCategoria() != null && !filtro.getCategoria().isBlank()) {
            regra = regra.and((r,q,cb) -> cb.equal(r.get("categoria"), filtro.getCategoria()));
        }
        if (filtro.getPrioridade() != null && !filtro.getPrioridade().isBlank()) {
            regra = regra.and((r,q,cb) -> cb.equal(r.get("prioridade"), filtro.getPrioridade()));
        }
        if (filtro.getInicio() != null) {
            regra = regra.and((r,q,cb) -> cb.greaterThanOrEqualTo(r.get("dataAbertura"), filtro.getInicio()));
        }
        if (filtro.getFim() != null) {
            regra = regra.and((r,q,cb) -> cb.lessThanOrEqualTo(r.get("dataAbertura"), filtro.getFim()));
        }
        return repository.findAll(regra, Sort.by(Sort.Direction.DESC,"id"));
    }

    /** Indicadores gerais: as contagens são obtidas da lista persistida, não de valores fixos. */
    public IndicadoresDTO indicadores() { return calcular(repository.findAll()); }

    /** Indicadores do recorte filtrado permitem confrontar listagem e painel. */
    public IndicadoresDTO indicadores(FiltroChamadoDTO filtro) { return calcular(consultar(filtro)); }

    private IndicadoresDTO calcular(List<Chamado> itens) {
        long abertos=contar(itens,"ABERTO");
        long analise=contar(itens,"EM_ANALISE");
        long concluidos=contar(itens,"CONCLUIDO");
        long cancelados=contar(itens,"CANCELADO");
        long altas=itens.stream().filter(c -> "ALTA".equals(c.getPrioridade()) || "URGENTE".equals(c.getPrioridade())).count();
        double percentual=itens.isEmpty()?0:concluidos*100.0/itens.size();
        return new IndicadoresDTO(itens.size(), abertos, analise, concluidos, cancelados, altas, percentual);
    }
    private long contar(List<Chamado> itens,String status) {
        return itens.stream().filter(c -> status.equals(c.getStatus())).count();
    }
}
