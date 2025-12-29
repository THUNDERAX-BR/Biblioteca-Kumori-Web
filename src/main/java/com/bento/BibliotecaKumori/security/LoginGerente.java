package com.bento.BibliotecaKumori.security;

public class LoginGerente extends Login {

    @Override
    public String getAcesso() {
        return "Gerente";
    }

    @Override
    public boolean permissaoGerenciar() {
        return true;
    }
}