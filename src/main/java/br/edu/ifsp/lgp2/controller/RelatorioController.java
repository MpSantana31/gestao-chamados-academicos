package br.edu.ifsp.lgp2.controller;

import br.edu.ifsp.lgp2.dto.FiltroChamadoDTO;
import br.edu.ifsp.lgp2.service.ConsultaService;
import br.edu.ifsp.lgp2.service.ExportacaoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

/** Controller MVC: apresenta indicadores e devolve arquivos como resposta HTTP. */
@Controller
public class RelatorioController {
    private final ConsultaService consulta;
    private final ExportacaoService exportacao;
    public RelatorioController(ConsultaService consulta,ExportacaoService exportacao) {
        this.consulta=consulta;this.exportacao=exportacao;
    }
    @GetMapping("/indicadores")
    public String indicadores(Model model) {
        model.addAttribute("indicadores",consulta.indicadores());
        return "indicadores";
    }
    @GetMapping("/relatorios")
    public String relatorios(@ModelAttribute FiltroChamadoDTO filtro,Model model) {
        model.addAttribute("filtro",filtro);
        model.addAttribute("chamados",consulta.consultar(filtro));
        model.addAttribute("indicadores",consulta.indicadores(filtro));
        return "relatorios";
    }
    @GetMapping("/relatorios/exportar/{formato}")
    public ResponseEntity<byte[]> exportar(@PathVariable String formato,@ModelAttribute FiltroChamadoDTO filtro) throws IOException {
        var registros=consulta.consultar(filtro);
        byte[] conteudo;
        String tipo;
        String extensao;
        switch(formato.toLowerCase()) {
            case "csv" -> { conteudo=exportacao.csv(registros);tipo="text/csv;charset=UTF-8";extensao="csv"; }
            case "excel" -> { conteudo=exportacao.excel(registros);tipo="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";extensao="xlsx"; }
            case "pdf" -> { conteudo=exportacao.pdf(registros);tipo="application/pdf";extensao="pdf"; }
            default -> { return ResponseEntity.badRequest().body(new byte[0]); }
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=chamados."+extensao)
                .contentType(MediaType.parseMediaType(tipo)).body(conteudo);
    }
}
