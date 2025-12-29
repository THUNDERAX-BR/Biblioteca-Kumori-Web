package com.bento.BibliotecaKumori.service;

import com.bento.BibliotecaKumori.model.Livro;
import com.bento.BibliotecaKumori.repository.LivroDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

    private final LivroDAO livroDAO;

    public LivroService(LivroDAO livroDAO) {
        this.livroDAO = livroDAO;
    }

    public List<Livro> listar(String busca, String anoIni, String anoFim, String categoria) {
        return livroDAO.listarBuscarLivros(busca, anoIni, anoFim, categoria);
    }

    public Livro buscarPorId(int id) {
        return livroDAO.exibirLivro(id);
    }

    public void cadastrar(Livro livro, int idAutor, int idEditora) {
        livroDAO.cadastrar(livro, idAutor, idEditora);
    }

    public void alterar(int id, Livro livro, int idAutor, int idEditora, boolean alterarCapa) {
        livroDAO.alterar(id, livro, idAutor, idEditora, alterarCapa);
    }

    public void excluir(int id) {
        livroDAO.excluir(id);
    }
    
    public List<Livro> listarPorAutor(int idAutor) {
        return livroDAO.listarLivrosPorAutor(idAutor);
    }
}