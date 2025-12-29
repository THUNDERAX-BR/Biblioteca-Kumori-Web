package com.bento.BibliotecaKumori.model;

import java.util.List;

public class Livro {

    private int id;
    private String titulo;
    private Autor autor;
    private int ano;
    private String descricao;
    private List<Categoria> categorias;
    private Editora editora;
    private byte[] capa;

    public Livro() {
    }

    public Livro(int id, String titulo, Editora editora) {
        this.id = id;
        this.titulo = titulo;
        this.editora = editora;
    }

    public Livro(int id, String titulo, Autor autor, Editora editora) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.editora = editora;
    }

    public Livro(int id, String titulo, Autor autor, int ano,
                 String descricao, List<Categoria> categorias, Editora editora) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.ano = ano;
        this.descricao = descricao;
        this.categorias = categorias;
        this.editora = editora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Editora getEditora() {
        return editora;
    }

    public void setEditora(Editora editora) {
        this.editora = editora;
    }

    public byte[] getCapa() {
        return capa;
    }

    public void setCapa(byte[] capa) {
        this.capa = capa;
    }
}