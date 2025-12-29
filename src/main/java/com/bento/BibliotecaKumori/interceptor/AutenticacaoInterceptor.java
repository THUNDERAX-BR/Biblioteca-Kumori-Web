package com.bento.BibliotecaKumori.interceptor;

import com.bento.BibliotecaKumori.security.Login;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class AutenticacaoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String uri = request.getRequestURI();
        HttpSession session = request.getSession(false);

        if (uri.equals("/login")
                || uri.equals("/login/")
                || (uri.equals("/login") && request.getMethod().equals("POST"))
                || uri.equals("/login/sem-login")
                || uri.equals("/login/logout")
                || uri.startsWith("/css")
                || uri.startsWith("/js")
                || uri.startsWith("/icons")) {
            return true;
        }

        Login usuario = (session != null)
                ? (Login) session.getAttribute("usuarioLogado")
                : null;

        if (usuario == null) {
            if (session != null) {
                session.invalidate();
            }
            response.sendRedirect("/login");
            return false;
        }

        if (uri.startsWith("/login/") && !usuario.permissaoGerenciarLogins()) {
            response.sendRedirect("/buscarlivros");
            return false;
        }
        
        if (uri.startsWith("/livro/") && !usuario.permissaoGerenciar()) {
            response.sendRedirect("/buscarlivros");
            return false;
        }
        
        if (uri.startsWith("/exemplar/") && !usuario.permissaoGerenciar()) {
            response.sendRedirect("/buscarlivros");
            return false;
        }
        
        if (uri.startsWith("/autor/") && !usuario.permissaoGerenciar()) {
            response.sendRedirect("/buscarlivros");
            return false;
        }
        
        if (uri.startsWith("/gerenciar") && !usuario.permissaoGerenciar()) {
            response.sendRedirect("/buscarlivros");
            return false;
        }
        
        if (uri.startsWith("/movimento/") && !usuario.permissaoGerenciar()) {
            response.sendRedirect("/buscarlivros");
            return false;
        }
        
        if (uri.startsWith("/categoria/") && !usuario.permissaoGerenciar()) {
            response.sendRedirect("/buscarlivros");
            return false;
        }
        
        if (uri.startsWith("/editora/") && !usuario.permissaoGerenciar()) {
            response.sendRedirect("/buscarlivros");
            return false;
        }

        return true;
    }
}