package com.bento.BibliotecaKumori.controller;

import com.bento.BibliotecaKumori.model.Editora;
import com.bento.BibliotecaKumori.security.Login;
import com.bento.BibliotecaKumori.service.EditoraService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/editora")
public class EditoraController {

    private final EditoraService editoraService;

    public EditoraController(EditoraService editoraService) {
        this.editoraService = editoraService;
    }

    @GetMapping("/exibicao")
    public String exibicaoEditoras(
            @RequestParam(value = "busca", required = false, defaultValue = "") String busca,
            Model model,
            HttpSession session) {

        Login usuario = (Login) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);

        List<Editora> editoras = editoraService.listarGerenciar(busca);
        model.addAttribute("editoras", editoras);
        model.addAttribute("busca", busca);

        return "ExibicaoEditoras";
    }

    @GetMapping("/cadastro")
    public String cadastroEditora(
            @RequestParam(required = false) Integer id,
            Model model,
            HttpSession session) {

        Login usuario = (Login) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);

        if (id != null) {
            Editora editora = editoraService.buscarPorId(id);
            model.addAttribute("editora", editora);
            model.addAttribute("modoEdicao", true);
        } else {
            model.addAttribute("editora", new Editora());
            model.addAttribute("modoEdicao", false);
        }

        return "CadastroEditora";
    }

    @PostMapping("/cadastro")
    public String salvarEditora(
            @RequestParam(required = false) Integer id,
            @RequestParam String nome,
            Model model,
            HttpSession session) {

        Login usuario = (Login) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);

        String nomeNormalizado = nome.trim().toLowerCase();
        List<Editora> existentes = editoraService.listarGerenciar("");

        boolean duplicado = existentes.stream()
                .anyMatch(e -> e.getNome() != null
                        && e.getNome().trim().toLowerCase().equals(nomeNormalizado)
                        && (id == null || e.getId() != id));

        if (duplicado) {
            model.addAttribute("erroDuplicado", true);

            Editora editora = null;
            if (id != null) {
                editora = editoraService.buscarPorId(id);
            }
            if (editora == null) {
                editora = new Editora();
                if (id != null) {
                    editora.setId(id);
                }
            }

            editora.setNome(nome);
            model.addAttribute("editora", editora);
            model.addAttribute("modoEdicao", id != null);

            return "CadastroEditora";
        }

        if (id == null || id < 1) {
            editoraService.cadastrar(nome);
        } else {
            editoraService.alterar(id, nome);
        }

        return "redirect:/editora/exibicao";
    }

    @PostMapping("/excluir/{id}")
    public String excluirEditora(@PathVariable int id) {
        editoraService.excluir(id);
        return "redirect:/editora/exibicao";
    }
}
