package br.edu.ifsp.lgp2.controller;

import br.edu.ifsp.lgp2.config.UsuarioLogado;
import br.edu.ifsp.lgp2.dto.ChamadoAtualizacaoStatusDTO;
import br.edu.ifsp.lgp2.dto.ChamadoCadastroDTO;
import br.edu.ifsp.lgp2.dto.ChamadoEdicaoDTO;
import br.edu.ifsp.lgp2.dto.FiltroChamadoDTO;
import br.edu.ifsp.lgp2.model.Chamado;
import br.edu.ifsp.lgp2.model.Papel;
import br.edu.ifsp.lgp2.service.ChamadoService;
import br.edu.ifsp.lgp2.service.ConsultaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ChamadoController {

    private final ChamadoService chamadoService;
    private final ConsultaService consultaService;
    private final UsuarioLogado usuarioLogado;

    public ChamadoController(ChamadoService chamadoService,
                             ConsultaService consultaService,
                             UsuarioLogado usuarioLogado) {
        this.chamadoService = chamadoService;
        this.consultaService = consultaService;
        this.usuarioLogado = usuarioLogado;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/chamados";
    }

    @GetMapping("/chamados")
    public String listar(@ModelAttribute FiltroChamadoDTO filtro,
                         Model model,
                         HttpServletRequest request) {
        String login = usuarioLogado.login(request);
        Papel papel = usuarioLogado.papel(request);
        model.addAttribute("chamados", consultaService.consultar(filtro, login, papel));
        model.addAttribute("filtro", filtro);
        model.addAttribute("admin", papel == Papel.ADMIN);
        model.addAttribute("usuario", login);
        return "chamados/lista";
    }

    @GetMapping("/chamados/novo")
    public String novo(Model model, HttpServletRequest request) {
        model.addAttribute("chamadoCadastroDTO", new ChamadoCadastroDTO());
        model.addAttribute("admin", usuarioLogado.isAdmin(request));
        model.addAttribute("usuario", usuarioLogado.login(request));
        return "chamados/formulario-cadastro";
    }

    @PostMapping("/chamados/salvar")
    public String salvarNovo(@ModelAttribute ChamadoCadastroDTO dto,
                             RedirectAttributes redirectAttributes,
                             HttpServletRequest request) {

        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "O título é obrigatório.");
            return "redirect:/chamados/novo";
        }

        chamadoService.salvarNovoChamado(dto, usuarioLogado.login(request));
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Chamado cadastrado com sucesso.");
        return "redirect:/chamados";
    }

    @GetMapping("/chamados/editar/{id}")
    public String editar(@PathVariable Long id,
                         Model model,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest request) {

        Chamado chamado = chamadoService.buscarPorId(id, usuarioLogado.login(request), usuarioLogado.papel(request));
        if (chamado == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Chamado não encontrado.");
            return "redirect:/chamados";
        }

        ChamadoEdicaoDTO dto = new ChamadoEdicaoDTO();
        dto.setId(chamado.getId());
        dto.setTitulo(chamado.getTitulo());
        dto.setDescricao(chamado.getDescricao());
        dto.setCategoria(chamado.getCategoria());
        dto.setStatus(chamado.getStatus());
        dto.setPrioridade(chamado.getPrioridade());
        dto.setResponsavel(chamado.getResponsavel());
        dto.setObservacaoAtendimento(chamado.getObservacaoAtendimento());

        model.addAttribute("chamadoEdicaoDTO", dto);
        model.addAttribute("admin", usuarioLogado.isAdmin(request));
        model.addAttribute("usuario", usuarioLogado.login(request));
        return "chamados/formulario-edicao";
    }

    @PostMapping("/chamados/atualizar")
    public String atualizar(@ModelAttribute ChamadoEdicaoDTO dto,
                            RedirectAttributes redirectAttributes,
                            HttpServletRequest request) {

        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            redirectAttributes.addFlashAttribute("mensagemErro", "O título é obrigatório.");
            return "redirect:/chamados/editar/" + dto.getId();
        }

        Chamado atualizado = chamadoService.atualizarChamado(
                dto, usuarioLogado.login(request), usuarioLogado.papel(request));
        if (atualizado == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Chamado não encontrado.");
            return "redirect:/chamados";
        }

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Chamado atualizado com sucesso.");
        return "redirect:/chamados";
    }

    @GetMapping("/chamados/status/{id}")
    public String formularioStatus(@PathVariable Long id,
                                   Model model,
                                   RedirectAttributes redirectAttributes,
                                   HttpServletRequest request) {

        Chamado chamado = chamadoService.buscarPorId(id, usuarioLogado.login(request), usuarioLogado.papel(request));
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
        model.addAttribute("admin", usuarioLogado.isAdmin(request));
        model.addAttribute("usuario", usuarioLogado.login(request));
        return "chamados/formulario-status";
    }

    @PostMapping("/chamados/status/atualizar")
    public String atualizarStatus(@ModelAttribute ChamadoAtualizacaoStatusDTO dto,
                                  RedirectAttributes redirectAttributes,
                                  HttpServletRequest request) {

        Chamado chamado = chamadoService.atualizarStatus(
                dto, usuarioLogado.login(request), usuarioLogado.papel(request));
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
                           RedirectAttributes redirectAttributes,
                           HttpServletRequest request) {

        Chamado chamado = chamadoService.buscarPorId(id, usuarioLogado.login(request), usuarioLogado.papel(request));
        if (chamado == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Chamado não encontrado.");
            return "redirect:/chamados";
        }

        model.addAttribute("chamado", chamado);
        model.addAttribute("admin", usuarioLogado.isAdmin(request));
        model.addAttribute("usuario", usuarioLogado.login(request));
        return "chamados/detalhes";
    }

    @PostMapping("/chamados/excluir/{id}")
    public String excluir(@PathVariable Long id,
                          RedirectAttributes redirectAttributes,
                          HttpServletRequest request) {

        boolean excluido = chamadoService.excluir(id, usuarioLogado.login(request), usuarioLogado.papel(request));
        if (!excluido) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Chamado não encontrado para exclusão.");
            return "redirect:/chamados";
        }

        redirectAttributes.addFlashAttribute("mensagemSucesso", "Chamado excluído com sucesso.");
        return "redirect:/chamados";
    }
}
