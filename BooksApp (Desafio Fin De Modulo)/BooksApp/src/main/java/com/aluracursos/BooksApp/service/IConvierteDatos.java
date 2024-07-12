package com.aluracursos.BooksApp.service;

public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class<T> clase);
    
}
