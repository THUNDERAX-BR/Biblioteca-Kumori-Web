package com.bento.BibliotecaKumori.controller;

import com.bento.BibliotecaKumori.model.Categoria;
import com.bento.BibliotecaKumori.security.Login;
import com.bento.BibliotecaKumori.service.CategoriaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("/exibicao")
    public String exibicaoCategorias(
            @RequestParam(value = "busca", required = false, defaultValue = "") String busca,
            Model model,
            HttpSession session) {

        Login usuario = (Login) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);

        List<Categoria> categorias = categoriaService.listarGerenciar(busca);
        model.addAttribute("categorias", categorias);
        model.addAttribute("busca", busca);

        return "ExibicaoCategorias";
    }

    @GetMapping("/cadastro")
    public String cadastroCategoria(
            @RequestParam(required = false) Integer id,
            Model model,
            HttpSession session) {

        Login usuario = (Login) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);

        if (id != null) {
            Categoria categoria = categoriaService.buscarPorId(id);
            model.addAttribute("categoria", categoria);
            model.addAttribute("modoEdicao", true);
        } else {
            model.addAttribute("categoria", new Categoria());
            model.addAttribute("modoEdicao", false);
        }

        return "CadastroCategoria";
    }

    @PostMapping("/cadastro")
    public String salvarCategoria(
            @RequestParam(required = false) Integer id,
            @RequestParam String nome,
            Model model,
            HttpSession session) {

        Login usuario = (Login) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);

        String nomeNormalizado = nome.trim().toLowerCase();

        List<Categoria> existentes = categoriaService.listarGerenciar("");

        boolean duplicado = existentes.stream()
                .anyMatch(c -> c.getNome().trim().toLowerCase().equals(nomeNormalizado)
                        && (id == null || c.getId() != id));

        if (duplicado) {
            model.addAttribute("erroDuplicado", true);

            Categoria categoria = (id != null) ? categoriaService.buscarPorId(id) : new Categoria();
            categoria.setNome(nome);
            model.addAttribute("categoria", categoria);
            model.addAttribute("modoEdicao", id != null);

            return "CadastroCategoria";
        }

        if (id == null || id < 1) {
            categoriaService.cadastrar(nome);
        } else {
            categoriaService.alterar(id, nome);
        }

        return "redirect:/categoria/exibicao";
    }



    @PostMapping("/excluir/{id}")
    public String excluirCategoria(@PathVariable int id) {
        categoriaService.excluir(id);
        return "redirect:/categoria/exibicao";
    }
}
