package br.edu.ifsp.lgp2.controller;

import br.edu.ifsp.lgp2.config.UsuarioLogado;
import br.edu.ifsp.lgp2.dto.ChamadoAtualizacaoStatusDTO;
import br.edu.ifsp.lgp2.dto.ChamadoCadastroDTO;
import br.edu.ifsp.lgp2.dto.ChamadoEdicaoDTO;
import br.edu.ifsp.lgp2.dto.FiltroChamadoDTO;
import br.edu.ifsp.lgp2.dto.IndicadoresDTO;
import br.edu.ifsp.lgp2.model.Chamado;
import br.edu.ifsp.lgp2.service.ChamadoService;
import br.edu.ifsp.lgp2.service.ConsultaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/chamados")
public class ChamadoApiController {

    private final ChamadoService chamados;
    private final ConsultaService consultas;
    private final UsuarioLogado usuarioLogado;

    public ChamadoApiController(ChamadoService chamados,
                                ConsultaService consultas,
                                UsuarioLogado usuarioLogado) {
        this.chamados = chamados;
        this.consultas = consultas;
        this.usuarioLogado = usuarioLogado;
    }

    @GetMapping
    public List<Chamado> listar(@ModelAttribute FiltroChamadoDTO filtro, HttpServletRequest request) {
        return consultas.consultar(filtro, usuarioLogado.login(request), usuarioLogado.papel(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chamado> buscar(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.of(chamados.buscarOpcional(id, usuarioLogado.login(request), usuarioLogado.papel(request)));
    }

    @GetMapping("/indicadores")
    public ResponseEntity<IndicadoresDTO> indicadores(HttpServletRequest request) {
        if (!usuarioLogado.isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(consultas.indicadores());
    }

    @PostMapping
    public ResponseEntity<Chamado> criar(@RequestBody ChamadoCadastroDTO dto, HttpServletRequest request) {
        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        Chamado criado = chamados.salvarNovoChamado(dto, usuarioLogado.login(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chamado> atualizar(@PathVariable Long id,
                                             @RequestBody ChamadoEdicaoDTO dto,
                                             HttpServletRequest request) {
        if (!usuarioLogado.isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        dto.setId(id);
        return ResponseEntity.of(Optional.ofNullable(
                chamados.atualizarChamado(dto, usuarioLogado.login(request), usuarioLogado.papel(request))));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Chamado> status(@PathVariable Long id,
                                          @RequestBody ChamadoAtualizacaoStatusDTO dto,
                                          HttpServletRequest request) {
        if (!usuarioLogado.isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        dto.setId(id);
        return ResponseEntity.of(Optional.ofNullable(
                chamados.atualizarStatus(dto, usuarioLogado.login(request), usuarioLogado.papel(request))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, HttpServletRequest request) {
        if (!usuarioLogado.isAdmin(request)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        boolean excluido = chamados.excluir(id, usuarioLogado.login(request), usuarioLogado.papel(request));
        return excluido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
