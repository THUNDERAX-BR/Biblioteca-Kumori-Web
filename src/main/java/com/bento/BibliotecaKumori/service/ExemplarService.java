package com.bento.BibliotecaKumori.service;

import com.bento.BibliotecaKumori.model.Exemplar;
import com.bento.BibliotecaKumori.repository.ExemplarDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExemplarService {

    private final ExemplarDAO exemplarDAO;

    public ExemplarService(ExemplarDAO exemplarDAO) {
        this.exemplarDAO = exemplarDAO;
    }

    public List<Exemplar> listarExemplaresLivro(int idLivro) {
        return exemplarDAO.listarExemplaresLivro(idLivro);
    }

    public List<Exemplar> listarGerenciar(String busca) {
        return exemplarDAO.listarGerenciar(busca);
    }

    public Exemplar buscarPorId(int id) {
        return exemplarDAO.buscarPorId(id);
    }

    public int buscarIdLivro(int idExemplar) {
        return exemplarDAO.buscarIdLivro(idExemplar);
    }

    public void cadastrar(int idLivro, String localizacao) {
        exemplarDAO.cadastrar(idLivro, localizacao);
    }

    public void alterar(int id, int idLivro, String localizacao) {
        exemplarDAO.alterar(id, idLivro, localizacao);
    }

    public void excluir(int id) {
        exemplarDAO.excluir(id);
    }
}