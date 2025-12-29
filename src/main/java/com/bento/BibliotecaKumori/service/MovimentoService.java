package com.bento.BibliotecaKumori.service;

import com.bento.BibliotecaKumori.model.Movimento;
import com.bento.BibliotecaKumori.repository.MovimentoDAO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovimentoService {

    private final MovimentoDAO movimentoDAO;

    public MovimentoService(MovimentoDAO movimentoDAO) {
        this.movimentoDAO = movimentoDAO;
    }

    public List<String> listarFiltroBuscarAutores() {
        return movimentoDAO.listarFiltroBuscarAutores();
    }

    public List<Movimento> listarGerenciar(String busca) {
        return movimentoDAO.listarGerenciar(busca);
    }

    public Movimento buscarPorId(int id) {
        return movimentoDAO.buscarPorId(id);
    }

    public void cadastrar(String nome) {
        movimentoDAO.cadastrar(nome);
    }

    public void alterar(int id, String nome) {
        movimentoDAO.alterar(id, nome);
    }

    public void excluir(int id) {
        movimentoDAO.excluir(id);
    }

    public List<Movimento> listarParaComboCadastroAutor(Integer idAutor) {
        return movimentoDAO.listarParaComboCadastroAutor(idAutor);
    }
}