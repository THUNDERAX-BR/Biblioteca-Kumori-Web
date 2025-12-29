package com.bento.BibliotecaKumori.security;

public class Login {

    private int id;
    private String login;
    private String senha;

    public Login() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getAcesso() {
        return "";
    }

    public boolean permissaoGerenciar() {
        return false;
    }

    public boolean permissaoGerenciarLogins() {
        return false;
    }
}