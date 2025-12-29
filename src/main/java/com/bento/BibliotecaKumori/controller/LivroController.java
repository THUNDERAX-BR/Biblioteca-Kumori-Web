package com.bento.BibliotecaKumori.controller;

import com.bento.BibliotecaKumori.security.Login;
import com.bento.BibliotecaKumori.service.AutorService;
import com.bento.BibliotecaKumori.service.CategoriaService;
import com.bento.BibliotecaKumori.service.ExemplarService;
import com.bento.BibliotecaKumori.service.EditoraService;
import com.bento.BibliotecaKumori.service.LivroService;
import com.bento.BibliotecaKumori.model.Livro;
import com.bento.BibliotecaKumori.model.Categoria;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLConnection;

@Controller
public class LivroController {

    private final LivroService livroService;
    private final CategoriaService categoriaService;
    private final AutorService autorService;
    private final ExemplarService exemplarService;
    private final EditoraService editoraService;

    public LivroController(
            LivroService livroService,
            CategoriaService categoriaService,
            AutorService autorService,
            ExemplarService exemplarService,
            EditoraService editoraService) {

        this.livroService = livroService;
        this.categoriaService = categoriaService;
        this.autorService = autorService;
        this.exemplarService = exemplarService;
        this.editoraService = editoraService;
    }

    @GetMapping("/buscarlivros")
    public String buscarLivros(
            @RequestParam(value = "busca", required = false, defaultValue = "") String busca,
            @RequestParam(value = "anoIni", required = false, defaultValue = "") String anoIni,
            @RequestParam(value = "anoFim", required = false, defaultValue = "") String anoFim,
            @RequestParam(value = "categoria", required = false, defaultValue = "") String categoria,
            Model model,
            HttpSession session) {

        Login usuarioLogado = (Login) session.getAttribute("usuarioLogado");

        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("categoriasFiltro", categoriaService.listarNomesParaFiltro());
        model.addAttribute("livros", livroService.listar(busca, anoIni, anoFim, categoria));

        model.addAttribute("busca", busca);
        model.addAttribute("anoIni", anoIni);
        model.addAttribute("anoFim", anoFim);
        model.addAttribute("categoriaSelecionada", categoria);

        return "BuscarLivros";
    }

    @GetMapping("/infolivro")
    public String infoLivro(
            @RequestParam int id,
            Model model,
            HttpSession session) {

        Login usuarioLogado = (Login) session.getAttribute("usuarioLogado");

        Livro livro = livroService.buscarPorId(id);

        model.addAttribute("usuarioLogado", usuarioLogado);
        model.addAttribute("livro", livro);
        model.addAttribute("categorias", categoriaService.listarCategoriasDoLivro(id));
        model.addAttribute("exemplares", exemplarService.listarExemplaresLivro(id));

        return "InfoLivro";
    }
    
    @GetMapping("/capa/{id}")
    public ResponseEntity<byte[]> capaLivro(@PathVariable int id) {

        Livro livro = livroService.buscarPorId(id);

        if (livro == null || livro.getCapa() == null || livro.getCapa().length == 0) {
            return ResponseEntity.notFound().build();
        }

        String contentType = "image/*";

        try {
            contentType = URLConnection
                    .guessContentTypeFromStream(new ByteArrayInputStream(livro.getCapa()));
        } catch (IOException ignored) {}

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,
                        contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .body(livro.getCapa());
    }

    @GetMapping("/livro/cadastro")
    public String cadastroLivro(
            @RequestParam(required = false) Integer id,
            Model model,
            HttpSession session) {

        Login usuario = (Login) session.getAttribute("usuarioLogado");

        model.addAttribute("usuarioLogado", usuario);
        model.addAttribute("categorias", categoriaService.listarParaComboCadastroLivro());
        model.addAttribute("editoras", editoraService.listarParaComboCadastroLivro(id));
        model.addAttribute("autores", autorService.listarParaComboCadastroLivro(id));

        if (id != null) {
            model.addAttribute("livro", livroService.buscarPorId(id));
            model.addAttribute("categoriasLivro", categoriaService.listarCategoriasDoLivro(id));
            model.addAttribute("modoEdicao", true);
        } else {
            model.addAttribute("livro", new Livro());
            model.addAttribute("categoriasLivro", List.of());
            model.addAttribute("modoEdicao", false);
        }

        return "CadastroLivro";
    }

    @PostMapping("/livro/cadastro")
    public String salvarLivro(
            @RequestParam(required = false) Integer id,
            @RequestParam String titulo,
            @RequestParam int ano,
            @RequestParam String descricao,
            @RequestParam int idAutor,
            @RequestParam int idEditora,
            @RequestParam String categoriasIds,
            @RequestParam(required = false) MultipartFile capa,
            @RequestParam boolean alterarCapa,
            HttpSession session) throws IOException {

        Login usuario = (Login) session.getAttribute("usuarioLogado");

        Livro livro = new Livro();
        livro.setTitulo(titulo);
        livro.setAno(ano);
        livro.setDescricao(descricao);
        
        List<Categoria> categorias = new ArrayList<>();
        if (categoriasIds != null && !categoriasIds.isBlank()) {
            for (String idCat : categoriasIds.split(",")) {
                Categoria c = new Categoria();
                c.setId(Integer.parseInt(idCat));
                categorias.add(c);
            }
        }
        livro.setCategorias(categorias);

        if (alterarCapa && capa != null && !capa.isEmpty()) {
            livro.setCapa(capa.getBytes());
        }

        if (id == null || id < 1) {
            livroService.cadastrar(livro, idAutor, idEditora);
        } else {
            livroService.alterar(id, livro, idAutor, idEditora, alterarCapa);
        }

        return "redirect:/buscarlivros";
    }

    @PostMapping("/livro/excluir/{id}")
    public String excluirLivro(
            @PathVariable int id,
            HttpSession session) {

        Login usuarioLogado = (Login) session.getAttribute("usuarioLogado");

        livroService.excluir(id);
        return "redirect:/buscarlivros";
    }
}