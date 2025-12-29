package com.bento.BibliotecaKumori.controller;

import com.bento.BibliotecaKumori.security.Login;
import com.bento.BibliotecaKumori.security.LoginFactory;
import com.bento.BibliotecaKumori.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping
    public String telaLogin() {
        return "TelaLogin";
    }

    @PostMapping
    public String autenticar(
            @RequestParam String login,
            @RequestParam String senha,
            HttpSession session,
            Model model) {

        try {
            Login usuario = loginService.validarLogin(login, senha);
            session.setAttribute("usuarioLogado", usuario);
            return "redirect:/buscarlivros";

        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "TelaLogin";
        }
    }

    @PostMapping("/sem-login")
    public String semLogin(HttpSession session) {

        Login usuario = new Login();
        usuario.setLogin("Sem Login");

        session.setAttribute("usuarioLogado", usuario);

        return "redirect:/buscarlivros";
    }

    @GetMapping("/exibicao")
    public String exibicao(
            @RequestParam(value = "busca", required = false, defaultValue = "") String busca,
            Model model,
            HttpSession session) {

        Login usuarioLogado = (Login) session.getAttribute("usuarioLogado");

        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("logins", loginService.listar(busca));
        model.addAttribute("busca", busca);

        return "ExibicaoLogins";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model, HttpSession session) {

        Login usuarioLogado = (Login) session.getAttribute("usuarioLogado");

        LoginFactory factory = new LoginFactory();

        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("tiposAcesso", factory.getTiposLogin());

        return "CadastroLogin";
    }

    @PostMapping("/cadastro")
    public String cadastrar(
            @RequestParam String login,
            @RequestParam String senha,
            @RequestParam String acesso,
            Model model,
            HttpSession session) {

        Login usuarioLogado = (Login) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuarioLogado);

        LoginFactory factory = new LoginFactory();
        String acessoBanco = factory.passarParaBancoDeDados(acesso);

        try {
            loginService.cadastrar(login, senha, acessoBanco);
            return "redirect:/login/exibicao";
        } catch (IllegalStateException e) {
            model.addAttribute("erroDuplicado", true);
            model.addAttribute("tiposAcesso", factory.getTiposLogin());
            return "CadastroLogin";
        }
    }

    @PostMapping("/excluir/{id}")
    public String excluir(@PathVariable int id, HttpSession session) {
        Login usuarioLogado = (Login) session.getAttribute("usuarioLogado");
        if(id == usuarioLogado.getId()){
            return "redirect:/login/exibicao";
        }
        
        loginService.excluir(id);
        return "redirect:/login/exibicao";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }
}