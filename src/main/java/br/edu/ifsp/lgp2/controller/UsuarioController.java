package br.edu.ifsp.lgp2.controller;

import br.edu.ifsp.lgp2.config.UsuarioLogado;
import br.edu.ifsp.lgp2.dto.UsuarioCadastroDTO;
import br.edu.ifsp.lgp2.dto.UsuarioEdicaoDTO;
import br.edu.ifsp.lgp2.dto.UsuarioResponseDTO;
import br.edu.ifsp.lgp2.model.Usuario;
import br.edu.ifsp.lgp2.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioLogado usuarioLogado;

    public UsuarioController(UsuarioService usuarioService, UsuarioLogado usuarioLogado) {
        this.usuarioService = usuarioService;
        this.usuarioLogado = usuarioLogado;
    }

    @GetMapping
    public String listar(Model model, HttpServletRequest request) {
        List<UsuarioResponseDTO> usuarios = usuarioService.listarTodos().stream()
                .map(u -> new UsuarioResponseDTO(u.getId(), u.getLogin(), u.getNome(), u.getPapel()))
                .toList();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("admin", usuarioLogado.isAdmin(request));
        model.addAttribute("usuario", usuarioLogado.login(request));
        return "usuarios/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model, HttpServletRequest request) {
        model.addAttribute("usuarioCadastroDTO", new UsuarioCadastroDTO());
        model.addAttribute("admin", usuarioLogado.isAdmin(request));
        model.addAttribute("usuario", usuarioLogado.login(request));
        return "usuarios/formulario-cadastro";
    }

    @PostMapping("/salvar")
    public String salvarNovo(@ModelAttribute UsuarioCadastroDTO dto,
                             RedirectAttributes redirectAttributes) {
        if (usuarioService.salvarNovo(dto) == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Login já cadastrado ou dados incompletos.");
            return "redirect:/usuarios/novo";
        }
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário cadastrado com sucesso.");
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id,
                         Model model,
                         RedirectAttributes redirectAttributes,
                         HttpServletRequest request) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Usuário não encontrado.");
            return "redirect:/usuarios";
        }
        UsuarioEdicaoDTO dto = new UsuarioEdicaoDTO();
        dto.setId(usuario.getId());
        dto.setLogin(usuario.getLogin());
        dto.setNome(usuario.getNome());
        dto.setSenha("");
        dto.setPapel(usuario.getPapel());
        model.addAttribute("usuarioEdicaoDTO", dto);
        model.addAttribute("admin", usuarioLogado.isAdmin(request));
        model.addAttribute("usuario", usuarioLogado.login(request));
        return "usuarios/formulario-edicao";
    }

    @PostMapping("/atualizar")
    public String atualizar(@ModelAttribute UsuarioEdicaoDTO dto,
                            RedirectAttributes redirectAttributes) {
        if (usuarioService.atualizar(dto) == null) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não foi possível atualizar. Login já em uso ou dados incompletos.");
            return "redirect:/usuarios/editar/" + dto.getId();
        }
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário atualizado com sucesso.");
        return "redirect:/usuarios";
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id,
                          RedirectAttributes redirectAttributes,
                          HttpServletRequest request) {
        if (!usuarioService.excluir(id, usuarioLogado.login(request))) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não foi possível excluir este usuário.");
            return "redirect:/usuarios";
        }
        redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário excluído com sucesso.");
        return "redirect:/usuarios";
    }
}
