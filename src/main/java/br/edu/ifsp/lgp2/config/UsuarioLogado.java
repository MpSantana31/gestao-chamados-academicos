package br.edu.ifsp.lgp2.config;

import br.edu.ifsp.lgp2.model.Papel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

@Component
public class UsuarioLogado {

    public String login(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        return sessao == null ? null : (String) sessao.getAttribute("usuario");
    }

    public Papel papel(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        if (sessao == null) {
            return null;
        }
        Object papel = sessao.getAttribute("papel");
        return papel instanceof Papel ? (Papel) papel : Papel.USUARIO;
    }

    public boolean isAdmin(HttpServletRequest request) {
        return papel(request) == Papel.ADMIN;
    }
}
