package com.aluracursos.BooksApp.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResultadosAPI( @JsonAlias("results") List<DatosDeLibro> libros ) {}
