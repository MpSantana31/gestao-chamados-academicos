package br.edu.ifsp.lgp2.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/** Login demonstrativo: apresenta o conceito de sessão HTTP. Não é autenticação de produção. */
@Controller
public class LoginController {
    @Value("${app.demo.usuario}") private String usuario;
    @Value("${app.demo.senha}") private String senha;
    @GetMapping("/login") public String formulario(){return "login";}
    @PostMapping("/login") public String entrar(@RequestParam String usuario,@RequestParam String senha,
                                                HttpServletRequest request,Model model){
        if(this.usuario.equals(usuario) && this.senha.equals(senha)) {
            // Invalida sessão anterior para reduzir risco de fixação de sessão.
            HttpSession anterior=request.getSession(false);if(anterior!=null) anterior.invalidate();
            request.getSession(true).setAttribute("usuario",usuario);
            return "redirect:/chamados";
        }
        model.addAttribute("erro","Usuário ou senha inválidos.");return "login";
    }
    @PostMapping("/logout") public String sair(HttpSession sessao){sessao.invalidate();return "redirect:/login";}
}
