package com.bento.BibliotecaKumori.model;

public class Autor {

    private int id;
    private String nome;
    private String dataNascimento;
    private String dataFalecimento;
    private Movimento movimento;
    private String biografia;
    private byte[] foto;

    public Autor() {
    }

    public Autor(int id, String nome, Movimento movimento) {
        this.id = id;
        this.nome = nome;
        this.movimento = movimento;
    }

    public Autor(int id, String nome, String dataNascimento, String dataFalecimento,
                 Movimento movimento, String biografia, byte[] foto) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.dataFalecimento = dataFalecimento;
        this.movimento = movimento;
        this.biografia = biografia;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getDataFalecimento() {
        return dataFalecimento;
    }

    public void setDataFalecimento(String dataFalecimento) {
        this.dataFalecimento = dataFalecimento;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
}