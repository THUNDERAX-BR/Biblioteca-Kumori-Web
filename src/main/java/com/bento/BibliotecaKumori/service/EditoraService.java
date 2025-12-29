package com.bento.BibliotecaKumori.service;

import com.bento.BibliotecaKumori.model.Editora;
import com.bento.BibliotecaKumori.repository.EditoraDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EditoraService {

    private final EditoraDAO editoraDAO;

    public EditoraService(EditoraDAO editoraDAO) {
        this.editoraDAO = editoraDAO;
    }

    public List<Editora> listarGerenciar(String busca) {
        return editoraDAO.listarGerenciar(busca);
    }

    public Editora buscarPorId(int id) {
        return editoraDAO.buscarPorId(id);
    }

    public void cadastrar(String nome) {
        editoraDAO.cadastrar(nome);
    }

    public void alterar(int id, String nome) {
        editoraDAO.alterar(id, nome);
    }

    public void excluir(int id) {
        editoraDAO.excluir(id);
    }

    public List<Editora> listarParaComboCadastroLivro(Integer idLivro) {
        return editoraDAO.listarParaComboCadastroLivro(idLivro);
    }
}