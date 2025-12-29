package com.bento.BibliotecaKumori.service;

import com.bento.BibliotecaKumori.repository.LoginDAO;
import com.bento.BibliotecaKumori.security.Login;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {

    private final LoginDAO loginDAO;

    public LoginService(LoginDAO loginDAO) {
        this.loginDAO = loginDAO;
    }

    public Login validarLogin(String login, String senha) {
        return loginDAO.validarLogin(login, senha);
    }

    public List<Login> listar(String busca) {
        return loginDAO.listarGerenciar(busca);
    }

    public void cadastrar(String login, String senha, String acesso) {
        loginDAO.cadastrar(login, senha, acesso);
    }

    public void excluir(int id) {
        loginDAO.excluir(id);
    }
}