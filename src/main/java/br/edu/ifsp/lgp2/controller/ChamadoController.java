package br.edu.ifsp.lgp2.controller;

import br.edu.ifsp.lgp2.dto.ChamadoAtualizacaoStatusDTO;
import br.edu.ifsp.lgp2.dto.ChamadoCadastroDTO;
import br.edu.ifsp.lgp2.dto.FiltroChamadoDTO;
import br.edu.ifsp.lgp2.service.ConsultaService;
import br.edu.ifsp.lgp2.model.Chamado;
import br.edu.ifsp.lgp2.service.ChamadoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/*
 * Controller cuida das rotas, telas, formulários e mensagens.
 */
@Controller
public class ChamadoController {

    private final ChamadoService chamadoService;
    private final ConsultaService consultaService;

    public ChamadoController(ChamadoService chamadoService, ConsultaService consultaService) {
        this.chamadoService = chamadoService;
        this.consultaService = consultaService;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/chamados";
    }

    @GetMapping("/chamados")
    public String listar(@ModelAttribute FiltroChamadoDTO filtro, Model model) {
        model.addAttribute("chamados", consultaService.consultar(filtro));
        model.addAttribute("filtro", filtro);
        return "chamados/lista";
    }

    @GetMapping("/chamados/novo")
    public String novo(Model model) {
        model.addAttribute("chamadoCadastroDTO", new ChamadoCadastroDTO());
        return "chamados/formulario-cadastro";
    }

    @PostMapping("/chamados/salvar")
    public String salvarNovo(@ModelAttribute ChamadoCadastroDTO dto,
                             RedirectAttributes redirectAttributes) {

        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "O título é obrigatório.");
            return "redirect:/chamados/novo";
        }

        chamadoService.salvarNovoChamado(dto);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Chamado cadastrado com sucesso.");
        return "redirect:/chamados";
    }

    @GetMapping("/chamados/editar/{id}")
    public String editar(@PathVariable Long id,
                         Model model,
                         RedirectAttributes redirectAttributes) {

        Chamado chamado = chamadoService.buscarPorId(id);

        if (chamado == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Chamado não encontrado.");
            return "redirect:/chamados";
        }

        model.addAttribute("chamado", chamado);
        return "chamados/formulario-edicao";
    }

    @PostMapping("/chamados/atualizar")
    public String atualizar(@ModelAttribute Chamado chamado,
                            RedirectAttributes redirectAttributes) {

        if (chamado.getTitulo() == null || chamado.getTitulo().isBlank()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "O título é obrigatório.");
            return "redirect:/chamados/editar/" + chamado.getId();
        }

        chamadoService.atualizarChamado(chamado);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Chamado atualizado com sucesso.");
        return "redirect:/chamados";
    }

    /*
     * Rota da atividade:
     * abre o formulário específico para atualização de status.
     */
    @GetMapping("/chamados/status/{id}")
    public String formularioStatus(@PathVariable Long id,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {

        Chamado chamado = chamadoService.buscarPorId(id);

        if (chamado == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Chamado não encontrado.");
            return "redirect:/chamados";
        }

        ChamadoAtualizacaoStatusDTO dto = new ChamadoAtualizacaoStatusDTO();
        dto.setId(chamado.getId());
        dto.setStatus(chamado.getStatus());
        dto.setObservacaoAtendimento(chamado.getObservacaoAtendimento());

        model.addAttribute("statusDTO", dto);
        model.addAttribute("chamado", chamado);

        return "chamados/formulario-status";
    }

    /*
     * Rota da atividade:
     * recebe o DTO e atualiza apenas status e observação.
     */
    @PostMapping("/chamados/status/atualizar")
    public String atualizarStatus(@ModelAttribute ChamadoAtualizacaoStatusDTO dto,
                                  RedirectAttributes redirectAttributes) {

        Chamado chamado = chamadoService.atualizarStatus(dto);

        if (chamado == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Chamado não encontrado.");
            return "redirect:/chamados";
        }

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Status atualizado com sucesso.");
        return "redirect:/chamados/detalhes/" + chamado.getId();
    }

    @GetMapping("/chamados/detalhes/{id}")
    public String detalhes(@PathVariable Long id,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        Chamado chamado = chamadoService.buscarPorId(id);

        if (chamado == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Chamado não encontrado.");
            return "redirect:/chamados";
        }

        model.addAttribute("chamado", chamado);
        return "chamados/detalhes";
    }

    @PostMapping("/chamados/excluir/{id}")
    public String excluir(@PathVariable Long id,
                          RedirectAttributes redirectAttributes) {

        if (!chamadoService.existe(id)) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Chamado não encontrado para exclusão.");
            return "redirect:/chamados";
        }

        chamadoService.excluir(id);

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Chamado excluído com sucesso.");
        return "redirect:/chamados";
    }
}
