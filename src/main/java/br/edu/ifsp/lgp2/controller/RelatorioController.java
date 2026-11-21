package br.edu.ifsp.lgp2.controller;

import br.edu.ifsp.lgp2.config.UsuarioLogado;
import br.edu.ifsp.lgp2.dto.FiltroChamadoDTO;
import br.edu.ifsp.lgp2.service.ConsultaService;
import br.edu.ifsp.lgp2.service.ExportacaoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class RelatorioController {

    private final ConsultaService consulta;
    private final ExportacaoService exportacao;
    private final UsuarioLogado usuarioLogado;

    public RelatorioController(ConsultaService consulta,
                               ExportacaoService exportacao,
                               UsuarioLogado usuarioLogado) {
        this.consulta = consulta;
        this.exportacao = exportacao;
        this.usuarioLogado = usuarioLogado;
    }

    @GetMapping("/indicadores")
    public String indicadores(Model model, HttpServletRequest request) {
        model.addAttribute("indicadores", consulta.indicadores());
        model.addAttribute("admin", usuarioLogado.isAdmin(request));
        model.addAttribute("usuario", usuarioLogado.login(request));
        return "indicadores";
    }

    @GetMapping("/relatorios")
    public String relatorios(@ModelAttribute FiltroChamadoDTO filtro,
                             Model model,
                             HttpServletRequest request) {
        String login = usuarioLogado.login(request);
        var papel = usuarioLogado.papel(request);
        model.addAttribute("filtro", filtro);
        model.addAttribute("chamados", consulta.consultar(filtro, login, papel));
        model.addAttribute("indicadores", consulta.indicadores(filtro, login, papel));
        model.addAttribute("admin", usuarioLogado.isAdmin(request));
        model.addAttribute("usuario", login);
        return "relatorios";
    }

    @GetMapping("/relatorios/exportar/{formato}")
    public ResponseEntity<byte[]> exportar(@PathVariable String formato,
                                           @ModelAttribute FiltroChamadoDTO filtro,
                                           HttpServletRequest request) throws IOException {
        var registros = consulta.consultar(
                filtro, usuarioLogado.login(request), usuarioLogado.papel(request));
        byte[] conteudo;
        String tipo;
        String extensao;
        switch (formato.toLowerCase()) {
            case "csv" -> {
                conteudo = exportacao.csv(registros);
                tipo = "text/csv;charset=UTF-8";
                extensao = "csv";
            }
            case "excel" -> {
                conteudo = exportacao.excel(registros);
                tipo = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                extensao = "xlsx";
            }
            case "pdf" -> {
                conteudo = exportacao.pdf(registros);
                tipo = "application/pdf";
                extensao = "pdf";
            }
            default -> {
                return ResponseEntity.badRequest().body(new byte[0]);
            }
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=chamados." + extensao)
                .contentType(MediaType.parseMediaType(tipo))
                .body(conteudo);
    }
}
