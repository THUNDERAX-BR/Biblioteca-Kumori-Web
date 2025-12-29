package com.bento.BibliotecaKumori.security;

public class LoginAdministrador extends Login {

    @Override
    public String getAcesso() {
        return "Administrador";
    }

    @Override
    public boolean permissaoGerenciar() {
        return true;
    }

    @Override
    public boolean permissaoGerenciarLogins() {
        return true;
    }
}