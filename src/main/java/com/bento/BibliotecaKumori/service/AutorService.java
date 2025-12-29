package com.bento.BibliotecaKumori.service;

import com.bento.BibliotecaKumori.model.Autor;
import com.bento.BibliotecaKumori.repository.AutorDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutorService {

    private final AutorDAO autorDAO;

    public AutorService(AutorDAO autorDAO) {
        this.autorDAO = autorDAO;
    }

    public List<Autor> listar(String busca, String movimento) {
        return autorDAO.listarBuscarAutores(busca, movimento);
    }

    public Autor buscarPorId(int id) {
        return autorDAO.exibirAutor(id);
    }

    public void cadastrar(Autor autor, int idMovimento) {
        autorDAO.cadastrar(autor, idMovimento);
    }

    public void alterar(int id, Autor autor, int idMovimento, boolean alterarFoto) {
        autorDAO.alterar(id, autor, idMovimento, alterarFoto);
    }

    public void excluir(int id) {
        autorDAO.excluir(id);
    }
    
    public List<Autor> listarParaComboCadastroLivro(Integer idLivro) {
        return autorDAO.listarParaComboCadastroLivro(idLivro);
    }
}
