package com.aluracursos.screenmatch.principal;

import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void muestraEjemplo(){
        List<String> nombres = Arrays.asList("Brenda", "Luis", "Maria Fernanda", "Eric", "Genesys");

        // stream() permite encadenar varias acciones en una sola línea de código
        nombres.stream()
                // sorted permite ordenar los elementos de forma alfabética
                .sorted()
                /* limit(E) permite limitar la cantidad de elementos de la lista
                   a los primeros E elementos indicados */
                .limit(4)
                //filter(...) permite filtrar bajo la regla indicada los elementos de la lista
                .filter(e -> e.startsWith("L"))
                //map(...) permite transformar todos los elementos de la lista en otro
                .map(n -> n.toUpperCase())
                //forEach(...) permite ejecutar una acción a todos los elementos de la lista
                .forEach(System.out::println);
    }
}
