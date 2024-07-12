package com.aluracursos.screenmatch.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "capitulos")
public class Capitulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer temporada;
    private String titulo;
    private Integer numeroDeCapitulo;
    private Double evaluacion;
    private LocalDate fechaDeLanzamiento;
    @ManyToOne
    private Serie serie;

    public Capitulo(){}

    public Capitulo(Integer temporada, DatosCapitulo d) {
        this.temporada = temporada;
        this.titulo = d.titulo();
        this.numeroDeCapitulo = d.numeroEpisodio();
        try {
            this.evaluacion = Double.valueOf(d.evaluacion());
        } catch (NumberFormatException e){
            this.evaluacion = 0.0;
        }
        try {
            this.fechaDeLanzamiento = LocalDate.parse(d.fechaDeLanzamiento());
        } catch (DateTimeParseException e){
            this.fechaDeLanzamiento = null;
        }
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroDeCapitulo() {
        return numeroDeCapitulo;
    }

    public void setNumeroDeCapitulo(Integer numeroDeCapitulo) {
        this.numeroDeCapitulo = numeroDeCapitulo;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public LocalDate getFechaDeLanzamiento() {
        return fechaDeLanzamiento;
    }

    public void setFechaDeLanzamiento(LocalDate fechaDeLanzamiento) {
        this.fechaDeLanzamiento = fechaDeLanzamiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return  "Temporada: " + getTemporada() + " Capitulo: " + getNumeroDeCapitulo() + ", Titulo: " + getTitulo() +
                ", Calificación: " + getEvaluacion() + ", Fecha de Lanzamiento: " + getFechaDeLanzamiento() + "\n";
    }
}
