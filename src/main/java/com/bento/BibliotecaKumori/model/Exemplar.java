package com.bento.BibliotecaKumori.model;

public class Exemplar {

    private int id;
    private Livro livro;
    private String localizacao;

    public Exemplar() {
    }

    public Exemplar(int id, String localizacao) {
        this.id = id;
        this.localizacao = localizacao;
    }

    public Exemplar(int id, Livro livro, String localizacao) {
        this.id = id;
        this.livro = livro;
        this.localizacao = localizacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
}