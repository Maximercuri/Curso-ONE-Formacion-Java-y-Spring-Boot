package com.aluracursos.screenmatch.model;

public enum Categoria {

    ACCION("Action", "Accion"),
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comedia"),
    CRIMEN("Crime", "Crimen"),
    DRAMA("Drama", "Drama"),
    ANIMACION("Animation", "Animacion"),
    AVENTURA("Adventure", "Aventura");


    private String categoriaLocal;
    private String categoriaUsuario;

    /*
    Una categoría genérica basándome en estos nombres sería GENERO(categoriaLocal, CategoriaUsuario),
    donde categoríaLocal está siendo definido por categoriaOmdb y categoriaUsuario se define por categoriaEspanol
    en el constructor.
    */

    Categoria(String categoriaOmdb, String categoriaEspanol) {
        categoriaLocal = categoriaOmdb;
        categoriaUsuario = categoriaEspanol;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaLocal.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoría fue encontrada con la etiqueta: " + text);
    }

    public static Categoria fromEspanol(String text){
        for (Categoria categoria : Categoria.values()){
            if (categoria.categoriaUsuario.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoría fue encontrada con la etiqueta: " + text);
    }


}
