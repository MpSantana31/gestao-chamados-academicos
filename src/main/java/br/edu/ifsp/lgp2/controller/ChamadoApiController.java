package br.edu.ifsp.lgp2.controller;

import br.edu.ifsp.lgp2.dto.*;
import br.edu.ifsp.lgp2.model.Chamado;
import br.edu.ifsp.lgp2.service.ChamadoService;
import br.edu.ifsp.lgp2.service.ConsultaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/** API REST: responde JSON em vez de telas HTML; a mesma camada Service é reutilizada. */
@RestController
@RequestMapping("/api/chamados")
public class ChamadoApiController {
    private final ChamadoService chamados;
    private final ConsultaService consultas;
    public ChamadoApiController(ChamadoService chamados,ConsultaService consultas){this.chamados=chamados;this.consultas=consultas;}
    @GetMapping public List<Chamado> listar(@ModelAttribute FiltroChamadoDTO filtro){return consultas.consultar(filtro);}
    @GetMapping("/{id}") public ResponseEntity<Chamado> buscar(@PathVariable Long id){return ResponseEntity.of(chamados.buscarOpcional(id));}
    @GetMapping("/indicadores") public IndicadoresDTO indicadores(){return consultas.indicadores();}
    @PostMapping public ResponseEntity<Chamado> criar(@RequestBody ChamadoCadastroDTO dto){
        if(dto.getTitulo()==null || dto.getTitulo().isBlank())return ResponseEntity.badRequest().build();
        return ResponseEntity.status(HttpStatus.CREATED).body(chamados.salvarNovoChamado(dto));
    }
    @PutMapping("/{id}/status") public ResponseEntity<Chamado> status(@PathVariable Long id,@RequestBody ChamadoAtualizacaoStatusDTO dto){
        dto.setId(id);return ResponseEntity.of(java.util.Optional.ofNullable(chamados.atualizarStatus(dto)));
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> excluir(@PathVariable Long id){
        if(!chamados.existe(id))return ResponseEntity.notFound().build();
        chamados.excluir(id);return ResponseEntity.noContent().build();
    }
}
