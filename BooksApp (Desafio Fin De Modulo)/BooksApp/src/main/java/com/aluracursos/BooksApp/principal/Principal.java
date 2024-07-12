package com.aluracursos.BooksApp.principal;

import com.aluracursos.BooksApp.model.DatosDeLibro;
import com.aluracursos.BooksApp.model.ResultadosAPI;
import com.aluracursos.BooksApp.service.ConversorDeDatos;
import com.aluracursos.BooksApp.service.ConsumoAPI;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    Scanner teclado = new Scanner(System.in);
    private ConsumoAPI busqueda = new ConsumoAPI();
    private ConversorDeDatos conversor = new ConversorDeDatos();
    private final String URL_BASE = "https://gutendex.com/books/";

    public void ejecutarPrograma() {

        var json = busqueda.obtenerDatos(URL_BASE);

        System.out.println(json + "\n");

        var datosObtenidos = conversor.obtenerDatos(json, ResultadosAPI.class);

        System.out.println(datosObtenidos + "\n");

        //Top 10 libros mas descargados
        System.out.println("Top 10 libros mas descargados: \n");

        datosObtenidos.libros().stream()
                .sorted(Comparator.comparing(DatosDeLibro::descargasTotales).reversed())
                .limit(10)
                .map(DatosDeLibro::titulo)
                .forEach(System.out::println);

        //Búsqueda de libro por nombre o parte de él
        System.out.println("\n" + "Por favor ingrese el titulo o parte de él del libro a buscar: ");

        var tituloDeBusqueda = teclado.nextLine().toLowerCase();
        json = busqueda.obtenerDatos( URL_BASE + "?search=" + tituloDeBusqueda.replace(" ", "+") );
        var busquedaPorTitulo = conversor.obtenerDatos(json, ResultadosAPI.class);
        Optional<DatosDeLibro> libroBuscado = busquedaPorTitulo.libros().stream()
                .filter(l -> l.titulo().toLowerCase().contains(tituloDeBusqueda))
                .findFirst();

        if (libroBuscado.isPresent()){
            System.out.println("\n" + "Libro encontrado!");
            System.out.println("Resultado: \n" + libroBuscado.get() + "\n");
        } else {
            System.out.println("\n" + "No se han encontrado coincidencias" + "\n");
        }

        //Estadísticas
        IntSummaryStatistics est = datosObtenidos.libros().stream()
                .filter(e -> e.descargasTotales() > 0)
                .collect(Collectors.summarizingInt(DatosDeLibro::descargasTotales));
        System.out.println("Promedio de descargas totales: " + est.getAverage() + "\n" +
                           "Máximo de descargas: " + est.getMax() + "\n" +
                           "Mínimo de descargas: " + est.getMin() + "\n" +
                           "Descargas Totales: " + est.getSum());

        //Búsqueda por fechas del autor (top 10 ordenado por descargas)
        System.out.println("Ingresa el inicio del periodo de búsqueda: ");
        var inicioPeriodo = teclado.nextInt();
        teclado.nextLine();

        System.out.println("Ingresa fin del periodo de búsqueda: ");
        var finPeriodo = teclado.nextInt();
        teclado.nextLine();

        json = busqueda.obtenerDatos(URL_BASE + "?author_year_start=" + inicioPeriodo + "&author_year_end=" + finPeriodo);
        datosObtenidos = conversor.obtenerDatos(json, ResultadosAPI.class);

        datosObtenidos.libros().stream()
                .sorted(Comparator.comparing(DatosDeLibro::descargasTotales).reversed())
                .limit(10)
                .forEach(e -> System.out.println( "Titulo: " + e.titulo() + "\n" +
                                                  "Autores: " + e.autores() + "\n" +
                                                  "Id: " + e.id() + "\n" +
                                                  "Descargas: " + e.descargasTotales() + "\n" ));

    }

}
