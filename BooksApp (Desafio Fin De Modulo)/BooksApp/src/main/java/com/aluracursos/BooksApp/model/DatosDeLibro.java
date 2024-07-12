package com.aluracursos.BooksApp.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosDeLibro( @JsonAlias("id") Integer id,
                            @JsonAlias("title") String titulo,
                            @JsonAlias("authors") List<DatosAutores> autores,
                            @JsonAlias("languages") List<String> idiomas,
                            @JsonAlias("download_count") Integer descargasTotales ) {}
