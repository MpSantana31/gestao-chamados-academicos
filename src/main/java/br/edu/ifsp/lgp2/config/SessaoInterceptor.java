package br.edu.ifsp.lgp2.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/** Interceptor MVC: impede acessar rotas privadas sem uma sessão autenticada. */
@Component
public class SessaoInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req,HttpServletResponse res,Object handler) throws Exception {
        if(req.getSession(false)!=null && req.getSession(false).getAttribute("usuario")!=null) return true;
        if(req.getRequestURI().startsWith("/api/")) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Autenticacao necessaria");return false;
        }
        res.sendRedirect(req.getContextPath()+"/login");return false;
    }
}
