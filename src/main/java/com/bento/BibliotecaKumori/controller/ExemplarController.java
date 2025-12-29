package com.bento.BibliotecaKumori.controller;

import com.bento.BibliotecaKumori.model.Livro;
import com.bento.BibliotecaKumori.model.Exemplar;
import com.bento.BibliotecaKumori.security.Login;
import com.bento.BibliotecaKumori.service.ExemplarService;
import com.bento.BibliotecaKumori.service.LivroService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/exemplar")
public class ExemplarController {

    private final ExemplarService exemplarService;
    private final LivroService livroService;

    public ExemplarController(ExemplarService exemplarService, LivroService livroService) {
        this.exemplarService = exemplarService;
        this.livroService = livroService;
    }

    @GetMapping("/cadastro")
    public String cadastroExemplar(
            @RequestParam(required = false) Integer idLivro,
            @RequestParam(required = false) Integer idExemplar,
            Model model,
            HttpSession session) {

        Login usuario = (Login) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);

        if (idLivro == null && idExemplar != null) {
            idLivro = exemplarService.buscarIdLivro(idExemplar);
        }

        if (idLivro == null) {
            return "redirect:/buscarlivros";
        }

        Livro livro = livroService.buscarPorId(idLivro);
        if (livro == null) {
            return "redirect:/buscarlivros";
        }
        model.addAttribute("livro", livro);

        Exemplar exemplar;
        boolean modoEdicao = false;

        if (idExemplar != null && idExemplar > 0) {
            exemplar = exemplarService.buscarPorId(idExemplar);
            if (exemplar == null) {
                return "redirect:/infolivro?id=" + idLivro;
            }
            if (exemplar.getId() == 0) {
                exemplar.setId(idExemplar);
            }
            modoEdicao = true;
        } else {
            exemplar = new Exemplar();
        }

        exemplar.setLivro(livro);
        model.addAttribute("exemplar", exemplar);
        model.addAttribute("modoEdicao", modoEdicao);

        return "CadastroExemplar";
    }

    @PostMapping("/cadastro")
    public String salvarExemplar(
            @RequestParam(name = "id", defaultValue = "0") int id,
            @RequestParam int idLivro,
            @RequestParam String localizacao) {

        if (id > 0) {
            exemplarService.alterar(id, idLivro, localizacao);
        } else {
            exemplarService.cadastrar(idLivro, localizacao);
        }

        return "redirect:/infolivro?id=" + idLivro;
    }

    @PostMapping("/excluir/{id}")
    public String excluirExemplar(@PathVariable int id) {
        Exemplar exemplar = exemplarService.buscarPorId(id);
        if (exemplar == null) {
            return "redirect:/buscarlivros";
        }

        int idLivro = exemplarService.buscarIdLivro(id);
        exemplarService.excluir(id);

        return "redirect:/infolivro?id=" + idLivro;
    }
}
