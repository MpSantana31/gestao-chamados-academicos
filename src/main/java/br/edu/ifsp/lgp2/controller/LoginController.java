package br.edu.ifsp.lgp2.controller;

import br.edu.ifsp.lgp2.model.Usuario;
import br.edu.ifsp.lgp2.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/** Login demonstrativo: consulta os usuários persistidos e guarda papel na sessão. */
@Controller
public class LoginController {

    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String formulario() {
        return "login";
    }

    @PostMapping("/login")
    public String entrar(@RequestParam String usuario,
                         @RequestParam String senha,
                         HttpServletRequest request,
                         Model model) {

        Usuario encontrado = usuarioService.buscarPorLogin(usuario);
        if (encontrado == null || !encontrado.getSenha().equals(senha)) {
            model.addAttribute("erro", "Usuário ou senha inválidos.");
            return "login";
        }

        HttpSession anterior = request.getSession(false);
        if (anterior != null) {
            anterior.invalidate();
        }
        HttpSession sessao = request.getSession(true);
        sessao.setAttribute("usuario", encontrado.getLogin());
        sessao.setAttribute("papel", encontrado.getPapel());
        return "redirect:/chamados";
    }

    @PostMapping("/logout")
    public String sair(HttpSession sessao) {
        sessao.invalidate();
        return "redirect:/login";
    }
}
