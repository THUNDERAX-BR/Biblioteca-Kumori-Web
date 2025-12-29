package com.bento.BibliotecaKumori.controller;

import com.bento.BibliotecaKumori.model.Autor;
import com.bento.BibliotecaKumori.model.Movimento;
import com.bento.BibliotecaKumori.model.Livro;
import com.bento.BibliotecaKumori.security.Login;
import com.bento.BibliotecaKumori.service.AutorService;
import com.bento.BibliotecaKumori.service.MovimentoService;
import com.bento.BibliotecaKumori.service.LivroService;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

@Controller
public class AutorController {

    private final AutorService autorService;
    private final MovimentoService movimentoService;
    private final LivroService livroService;

    public AutorController(AutorService autorService, MovimentoService movimentoService, LivroService livroService) {
        this.autorService = autorService;
        this.movimentoService = movimentoService;
        this.livroService = livroService;
    }

    @GetMapping("/buscarautores")
    public String buscarAutores(
            @RequestParam(value = "busca", required = false, defaultValue = "") String busca,
            @RequestParam(value = "movimento", required = false, defaultValue = "") String movimento,
            Model model,
            HttpSession session) {

        Login usuarioLogado = (Login) session.getAttribute("usuarioLogado");

        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("movimentosFiltro", movimentoService.listarFiltroBuscarAutores());
        model.addAttribute("autores", autorService.listar(busca, movimento));

        model.addAttribute("busca", busca);
        model.addAttribute("movimentoSelecionado", movimento);

        return "BuscarAutores";
    }

    @GetMapping("/infoautor")
    public String infoAutor(
            @RequestParam int id,
            Model model,
            HttpSession session) {

        Login usuarioLogado = (Login) session.getAttribute("usuarioLogado");

        Autor autor = autorService.buscarPorId(id);
        List<Livro> livrosAutor = livroService.listarPorAutor(id);

        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("autor", autor);
        model.addAttribute("livrosAutor", livrosAutor);

        return "InfoAutor";
    }


    @GetMapping("/foto/{id}")
    public ResponseEntity<byte[]> fotoAutor(@PathVariable int id) {
        Autor autor = autorService.buscarPorId(id);

        if (autor == null || autor.getFoto() == null || autor.getFoto().length == 0) {
            return ResponseEntity.notFound().build();
        }

        String contentType = "image/*";
        try {
            contentType = URLConnection.guessContentTypeFromStream(new ByteArrayInputStream(autor.getFoto()));
        } catch (IOException ignored) {}

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,
                        contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(autor.getFoto());
    }

    @GetMapping("/autor/cadastro")
    public String cadastroAutor(
            @RequestParam(required = false) Integer id,
            Model model,
            HttpSession session) {

        Login usuario = (Login) session.getAttribute("usuarioLogado");
        model.addAttribute("usuarioLogado", usuario);

        Autor autor;
        boolean modoEdicao = false;

        if (id != null) {
            autor = autorService.buscarPorId(id);
            modoEdicao = true;

            if (autor.getDataNascimento() != null && !autor.getDataNascimento().isBlank()) {
                LocalDate nasc = LocalDate.parse(autor.getDataNascimento(),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                autor.setDataNascimento(nasc.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }

            if (autor.getDataFalecimento() != null && !autor.getDataFalecimento().isBlank()) {
                LocalDate fal = LocalDate.parse(autor.getDataFalecimento(),
                        DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                autor.setDataFalecimento(fal.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }

            model.addAttribute("movimentos", movimentoService.listarParaComboCadastroAutor(id));
        } else {
            autor = new Autor();
            model.addAttribute("movimentos", movimentoService.listarParaComboCadastroAutor(null));
        }

        model.addAttribute("autor", autor);
        model.addAttribute("modoEdicao", modoEdicao);

        return "CadastroAutor";
    }


    @PostMapping("/autor/cadastro")
    public String salvarAutor(
            @RequestParam(required = false) Integer id,
            @RequestParam String nome,
            @RequestParam String dataNascimento,
            @RequestParam(required = false) String dataFalecimento,
            @RequestParam int idMovimento,
            @RequestParam String biografia,
            @RequestParam(required = false) MultipartFile foto,
            @RequestParam boolean alterarFoto,
            HttpSession session) throws IOException {

        Login usuario = (Login) session.getAttribute("usuarioLogado");

        Autor autor = new Autor();
        autor.setNome(nome);
        autor.setDataNascimento(dataNascimento);
        autor.setDataFalecimento(dataFalecimento);
        autor.setBiografia(biografia);

        Movimento movimento = new Movimento();
        movimento.setId(idMovimento);
        autor.setMovimento(movimento);

        if (alterarFoto && foto != null && !foto.isEmpty()) {
            autor.setFoto(foto.getBytes());
        }

        if (id == null || id < 1) {
            autorService.cadastrar(autor, idMovimento);
        } else {
            autorService.alterar(id, autor, idMovimento, alterarFoto);
        }

        return "redirect:/buscarautores";
    }

    @PostMapping("/autor/excluir/{id}")
    public String excluirAutor(@PathVariable int id, HttpSession session) {
        Login usuarioLogado = (Login) session.getAttribute("usuarioLogado");
        autorService.excluir(id);
        return "redirect:/buscarautores";
    }
}
