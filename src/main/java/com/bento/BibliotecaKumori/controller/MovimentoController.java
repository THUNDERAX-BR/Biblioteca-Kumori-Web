package com.bento.BibliotecaKumori.controller;

import com.bento.BibliotecaKumori.model.Movimento;
import com.bento.BibliotecaKumori.security.Login;
import com.bento.BibliotecaKumori.service.MovimentoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/movimento")
public class MovimentoController {

    private final MovimentoService movimentoService;

    public MovimentoController(MovimentoService movimentoService) {
        this.movimentoService = movimentoService;
    }

    @GetMapping("/exibicao")
    public String exibicaoMovimentos(
            @RequestParam(value = "busca", required = false, defaultValue = "") String busca,
            Model model,
            HttpSession session) {

        Login usuario = (Login) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);

        List<Movimento> movimentos = movimentoService.listarGerenciar(busca);
        model.addAttribute("movimentos", movimentos);
        model.addAttribute("busca", busca);

        return "ExibicaoMovimentos";
    }

    @GetMapping("/cadastro")
    public String cadastroMovimento(
            @RequestParam(required = false) Integer id,
            Model model,
            HttpSession session) {

        Login usuario = (Login) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);

        if (id != null) {
            Movimento movimento = movimentoService.buscarPorId(id);
            model.addAttribute("movimento", movimento);
            model.addAttribute("modoEdicao", true);
        } else {
            model.addAttribute("movimento", new Movimento());
            model.addAttribute("modoEdicao", false);
        }

        return "CadastroMovimento";
    }

    @PostMapping("/cadastro")
    public String salvarMovimento(
            @RequestParam(required = false) Integer id,
            @RequestParam String nome,
            Model model,
            HttpSession session) {

        Login usuario = (Login) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);

        String nomeNormalizado = nome.trim().toLowerCase();
        List<Movimento> existentes = movimentoService.listarGerenciar("");

        boolean duplicado = existentes.stream()
                .anyMatch(m -> m.getNome() != null
                        && m.getNome().trim().toLowerCase().equals(nomeNormalizado)
                        && (id == null || m.getId() != id));

        if (duplicado) {
            model.addAttribute("erroDuplicado", true);

            Movimento movimento = null;
            if (id != null) {
                movimento = movimentoService.buscarPorId(id);
            }
            if (movimento == null) {
                movimento = new Movimento();
                if (id != null) {
                    movimento.setId(id);
                }
            }

            movimento.setNome(nome);
            model.addAttribute("movimento", movimento);
            model.addAttribute("modoEdicao", id != null);

            return "CadastroMovimento";
        }

        if (id == null || id < 1) {
            movimentoService.cadastrar(nome);
        } else {
            movimentoService.alterar(id, nome);
        }

        return "redirect:/movimento/exibicao";
    }

    @PostMapping("/excluir/{id}")
    public String excluirMovimento(@PathVariable int id) {
        movimentoService.excluir(id);
        return "redirect:/movimento/exibicao";
    }
}
