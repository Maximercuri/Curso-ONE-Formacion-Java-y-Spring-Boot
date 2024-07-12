package com.aluracursos.screenmatchFrases.model;

import jakarta.persistence.*;

@Entity
@Table(name = "frases")
public class Frase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String frase;
    private String autor;
    private String poster;

    public Frase(String titulo, String frase, String autor, String serie, String poster) {
        this.titulo = titulo;
        this.frase = frase;
        this.autor = autor;
        this.poster = poster;
    }

    public Frase(){
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFrase() {
        return frase;
    }

    public void setFrase(String frase) {
        this.frase = frase;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getposter() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
