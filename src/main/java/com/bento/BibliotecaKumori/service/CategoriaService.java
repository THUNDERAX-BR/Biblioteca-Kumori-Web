package com.bento.BibliotecaKumori.service;

import com.bento.BibliotecaKumori.repository.CategoriaDAO;
import com.bento.BibliotecaKumori.model.Categoria;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaDAO categoriaDAO;

    public CategoriaService(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    public List<String> listarNomesParaFiltro() {
        return categoriaDAO.listarNomesParaFiltro();
    }

    public List<Categoria> listarParaComboCadastroLivro() {
        return categoriaDAO.listarParaComboCadastroLivro();
    }

    public List<Categoria> listarCategoriasDoLivro(int idLivro) {
        return categoriaDAO.listarCategoriasDoLivro(idLivro);
    }

    public Categoria buscarPorId(int id) {
        return categoriaDAO.buscarPorId(id);
    }

    public List<Categoria> listarGerenciar(String busca) {
        return categoriaDAO.listarGerenciar(busca);
    }

    public void cadastrar(String nome) {
        categoriaDAO.cadastrar(nome);
    }

    public void alterar(int id, String nome) {
        categoriaDAO.alterar(id, nome);
    }

    public void excluir(int id) {
        categoriaDAO.excluir(id);
    }
}