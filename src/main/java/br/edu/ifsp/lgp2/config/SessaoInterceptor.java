package br.edu.ifsp.lgp2.config;

import br.edu.ifsp.lgp2.model.Papel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessaoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        HttpSession sessao = req.getSession(false);
        if (sessao == null || sessao.getAttribute("usuario") == null) {
            if (req.getRequestURI().startsWith("/api/")) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Autenticação necessária");
            } else {
                res.sendRedirect(req.getContextPath() + "/login");
            }
            return false;
        }

        Object papel = sessao.getAttribute("papel");
        boolean admin = papel instanceof Papel && papel == Papel.ADMIN;
        String uri = req.getRequestURI();
        String metodo = req.getMethod();

        boolean rotaAdmin = uri.startsWith("/chamados/editar")
                || uri.startsWith("/chamados/atualizar")
                || uri.startsWith("/chamados/status")
                || uri.startsWith("/chamados/excluir")
                || uri.startsWith("/indicadores")
                || uri.startsWith("/relatorios")
                || uri.startsWith("/usuarios");

        boolean apiRestrita = uri.startsWith("/api/chamados")
                && ("PUT".equals(metodo) || "DELETE".equals(metodo)
                || uri.startsWith("/api/chamados/indicadores"));

        if (!admin && (rotaAdmin || apiRestrita)) {
            if (uri.startsWith("/api/")) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso restrito a administradores");
            } else {
                res.sendRedirect(req.getContextPath() + "/acesso-negado.html");
            }
            return false;
        }

        return true;
    }
}
