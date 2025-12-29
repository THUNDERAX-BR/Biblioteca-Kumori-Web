package com.bento.BibliotecaKumori.controller;

import com.bento.BibliotecaKumori.security.Login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session) {

        Login usuario = (Login) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            return "redirect:/login";
        }

        return "redirect:/buscarlivros";
    }
    
    @GetMapping("/gerenciar")
    public String gerenciar(Model model, HttpSession session) {
        Login usuario = (Login) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);
        return "Gerenciar";
    }

}