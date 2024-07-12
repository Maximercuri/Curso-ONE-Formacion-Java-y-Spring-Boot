package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoApi;
import com.aluracursos.screenmatch.service.ConvierteDatos;

import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    Scanner teclado = new Scanner(System.in);
    private ConsumoApi busqueda = new ConsumoApi();
    private ConvierteDatos conversor = new ConvierteDatos();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=9fe87ada";
    private SerieRepository repositorio;
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private Optional<Serie> serieBuscada;
    private List<Serie> seriesList = new ArrayList<>();

    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void menu() {

        var opcion = -1;
        while (opcion != 0) {

            var menu = """
                       Ingrese el numero de la opción elegida:
                       
                       1) Buscar Datos Generales De Una Serie
                       2) Comprobar Existencia de una Serie en la Base de Datos
                       3) Buscar Episodios De Una Serie
                       4) Mostrar historial de búsquedas
                       5) Mostrar Top 5 Series en la Base de Datos por Calificación
                       6) Mostrar Series de un Genero Especifico en la Base de Datos
                       7) Mostrar Series con un Máximo de Temporadas en la Base de Datos
                       8) Mostrar Series con una Calificación Minima en la Base de Datos
                       9) Mostrar un Capitulo en Especifico
                       10) Mostrar un top 5 Mejores Episodios de Una serie en la Base de Datos
                       
                       0) Salir""";

            System.out.println("\n" + menu + "\n");
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerie();
                    break;

                case 2:
                    buscarSerieEnBaseDeDAtos();
                    break;

                case 3:
                    buscarEpisodiosDeUnaSerie();
                    break;

                case 4:
                    mostrarSeriesBuscadas();
                    break;

                case 5:
                    mostrarTop5();
                    break;

                case 6:
                    mostrarSeriesDeUnaCategoria();
                    break;

                case 7:
                    mostrarSeriesConCiertasTemporadas();
                    break;

                case 8:
                    mostrarSeriesDeMinimaCalificacion();
                    break;

                case 9:
                    mostrarCapituloEspecifico();
                    break;

                case 10:
                    mostrarTop5Capitulos();
                    break;

                case 0:
                    System.out.println("\n" + "Saliendo del programa.");
                    break;

                default:
                    System.out.println("\n" + "Opción invalida, por favor elige otra.");
            }
        }

        System.out.println("\n" + "Fin del Programa, ¡Hasta Luego!");

    }

    private DatosSerie getDatosSerie(){

        // El usuario ingresa el nombre de una serie, se buscan sus datos y se retorna ese objeto de clase DatosSerie
        System.out.println("\n" + "Por favor, ingrese el nombre de la serie que desea buscar: ");

        var nombreSerie = teclado.nextLine().replace(" ","+");
        var json = busqueda.obtenerDatos(URL_BASE + nombreSerie + API_KEY);

        return conversor.obtenerDatos(json, DatosSerie.class);
    }

    private void buscarSerie(){

        DatosSerie datos = getDatosSerie();
        //datosSeries.add(datos);
        Serie serie = new Serie(datos);
        if (repositorio.findByTituloIgnoreCase(serie.getTitulo()).isEmpty()) {
            repositorio.save(serie);
        }

        System.out.println( "\n" +
                "Titulo de la serie: " + datos.titulo() + "\n" +
                "Sinopsis: " + datos.sinopsis() + "\n" +
                "Temporadas: " + datos.totalDeTemporadas() + "\n" +
                "Calificación: " + datos.evaluacion() + "\n" +
                "Géneros: " + datos.genero() + "\n" +
                "Actores Principales: " + datos.actores() + "\n" +
                "Portada: " + datos.imagenPromocional() + "\n");

    }

    private void buscarSerieEnBaseDeDAtos() {

        System.out.println("\n" + "Ingrese el Nombre de la Serie a Buscar: ");
        var nombreSerie = teclado.nextLine();

        serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

        if (serieBuscada.isPresent()){
            System.out.println("Serie encontrada! " + "\n" + serieBuscada.get());
        } else {
            System.out.println("\n" + "La Serie Buscada no se Encuentra en la Base de Datos");
        }
    }

    private void buscarEpisodiosDeUnaSerie(){

        System.out.println("Repertorio de Series: ");
        mostrarSeriesBuscadas();
        System.out.println("Por favor selecciona a continuación una de las series anteriores, escribiendo su nombre, para ver sus episodios");
        var nombreSerieBuscada = teclado.nextLine();

        Optional<Serie> serie = seriesList.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerieBuscada.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {

            var serieHallada = serie.get();

            // Lista para guardar los datos de todas las temporadas
            List<DatosTemporada> temporadas = new ArrayList<>();

            // Busca los datos de cada temporada de la serie ingresada por el usuario en getDatosSerie y los almacena en una lista
            for (int i = 1; i <= serieHallada.getTotalDeTemporadas(); i++) {
                var json = busqueda.obtenerDatos(URL_BASE + serieHallada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                var temporada = conversor.obtenerDatos(json, DatosTemporada.class);
                temporadas.add(temporada);
            }

            List<Capitulo> capituloList = temporadas.stream()
                    .flatMap(d -> d.capitulos().stream()
                            .map(e -> new Capitulo(d.temporada(), e)))
                    .collect(Collectors.toList());

            /* Muestra cada uno de los valores adentro de la lista (al ser una lista de objetos de clase
           "Datos temporada", mostrará una lista de objetos con sus respectivas clases */
            capituloList.forEach(e -> System.out.println(e.toString()));

            if (serieHallada.getCapitulos().isEmpty()) {

                serieHallada.setCapitulos(capituloList);
                repositorio.save(serieHallada);

            }

        } else {
            System.out.println("\n" + "No se ha encontrado ninguna serie con ese titulo.");
        }

    }

    private void mostrarSeriesBuscadas() {

        seriesList = repositorio.findAll();

        seriesList.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(e-> System.out.println(e.toString()));


    }

    private void mostrarTop5() {

        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(e -> System.out.println( "\n" + "Titulo: " + e.getTitulo() + ", Evaluación: " + e.getEvaluacion()));

    }

    private void mostrarSeriesDeUnaCategoria() {

        System.out.println("\n" + "Ingrese el Genero de las Series a Buscar: ");
        var generoIngresado = teclado.nextLine();
        var generoNormalizado = Normalizer.normalize(generoIngresado, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        var generoBuscado = Categoria.fromEspanol(generoNormalizado);
        List<Serie> seriesDeUnaCategoria = repositorio.findByGenero(generoBuscado);

        System.out.println("\n" + "Series encontradas con el genero " + generoIngresado + ": \n" );
        seriesDeUnaCategoria.forEach(System.out::println);

    }

    private void mostrarSeriesConCiertasTemporadas() {

        System.out.println("\n" + "Ingrese el numero de Temporadas Como Máximo a Tener en las Series a Buscar: ");
        var temporadasMax = teclado.nextInt();
        teclado.nextLine();
        List<Serie> seriesMenorOIgualTemporadas = repositorio.findByTotalDeTemporadasLessThanEqual(temporadasMax);

        System.out.println("\n" + "Series encontradas con " + temporadasMax + " temporadas o menos: \n");
        seriesMenorOIgualTemporadas.forEach(e -> System.out.println(e.getTitulo() + ", Temporadas: " + e.getTotalDeTemporadas()));

    }

    private void mostrarSeriesDeMinimaCalificacion() {

        System.out.println("\n" + "Ingrese la Calificación de las Series a Buscar: ");
        var calificacionMin = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> seriesDeMinimaCalificacion = repositorio.findByEvaluacionGreaterThanEqual(calificacionMin);

        System.out.println("\n" + "Series encontradas con mínimo " + calificacionMin + " de calificación: \n");
        seriesDeMinimaCalificacion.forEach(e -> System.out.println(e.getTitulo() + ", Calificación: " + e.getEvaluacion()));

    }

    private void mostrarCapituloEspecifico() {

        System.out.println("\n" + "Ingrese el Nombre del capitulo: ");
        var capituloBuscado = teclado.nextLine();

        List<Capitulo> capitulosCoincidentes = repositorio.capitulosPorNombre(capituloBuscado);

        capitulosCoincidentes.forEach(e -> System.out.printf("\n" + "Serie: %s, Temporada: %s, Episodio: %s, Titulo: %s Evaluación: %s \n",
                                                             e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroDeCapitulo(), e.getTitulo(), e.getEvaluacion()));

    }

    private void mostrarTop5Capitulos() {

        buscarSerieEnBaseDeDAtos();

        if(serieBuscada.isPresent()){

            Serie serie = serieBuscada.get();
            List<Capitulo> top5Capitulos = repositorio.top5CapitulosDeUnaSerie(serie);

            top5Capitulos.forEach(e -> System.out.println(e.toString()));

        }

    }

}



/*

       Muestra solo el título de los capítulos de cada temporada mediante una función lambda

       [Para cada elemento "t" (que representa cada temporada) dentro de la lista "temporadas", toma su atributo "capitulos",
       y para cada elemento "e" (que representa cada capítulo dentro de esa temporada "t") del atributo "capitulos",
       imprime en pantalla su atributo "título"]

       temporadas.forEach(t -> t.capitulos().forEach(e -> System.out.println(e.titulo())));

*/

/*

       Se crea una lista de capítulos que usa la lista "temporadas", toma de cada elemento "t",
       su atributo llamado "capitulos" y lo guarda en una colección que luego lo transforma en una lista mutable (es decir que se puede modificar)

       List<DatosCapitulo> listaDeCapitulos = temporadas.stream()
            .flatMap(t -> t.capitulos().stream())
            .collect(Collectors.toList());

*/

/*

      Muestra el top de mejores 5 capítulos:

      System.out.println("Top 5 episodios:");
      listaDeCapitulos.stream()
               ( Se puede utilizar la función "peek" entre funciones para comprobar como java realiza las funciones proveídas)
              .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
              (sorted ordena de menor a mayor, pero el atributo reversed lo invierte)
              .sorted(Comparator.comparing(DatosCapitulo::evaluacion).reversed())
              .limit(5)
              .forEach(System.out::println);

*/

/*

      Convierte los datos de la lista "temporadas" en una lista de elementos clase "Capitulo"

      List<Capitulo> capitulosList = temporadas.stream()
              .flatMap(t -> t.capitulos().stream()
                      .map(d -> new Capitulo(t.temporada(), d)))
              .collect(Collectors.toList());

*/

/*
      Búsqueda de capítulos mediante un año proveído por el usuario

      System.out.println("Por favor ingrese a partir de qué año prefiere ver los capítulos: ");
      var fecha = teclado.nextInt();
      teclado.nextLine();
      LocalDate fechaDeBusqueda = LocalDate.of(fecha, 1, 1);
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      capitulosList.stream()
              .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fechaDeBusqueda))
              .forEach(e -> System.out.println(
                             "Temporada: " + e.getTemporada() + "\n" +
                             "Episodio: " + e.getTitulo() + "\n" +
                             "Fecha de Lanzamiento: " + e.getFechaDeLanzamiento().format(dtf)
              ));

*/

/*
      Búsqueda de un capítulo específico mediante título sin necesidad de ingresarlo por completo

      System.out.println("Ingrese el nombre del episodio; ya sea titulo completo, parcial o alguna palabra clave; del capitulo a buscar: ");
      var busquedaDeTitulo = teclado.nextLine();
      Optional<Capitulo> capituloResultadoDeBusqueda = capitulosList.stream()
              .filter(e -> e.getTitulo().toUpperCase().contains(busquedaDeTitulo.toUpperCase()))
              .findFirst();
      if (capituloResultadoDeBusqueda.isPresent()){
          System.out.println("Episodio encontrado! ");
          System.out.println("Resultado: " + capituloResultadoDeBusqueda.get());
      } else {
          System.out.println("No se ha encontrado ningún capitulo que contenga la búsqueda realizada");
      }

*/

/*

      Se crea un mapa que toma como clave el número de temporada, el promedio de las evaluaciones por temporada como valor y lo imprime en pantalla

        Map<Integer, Double> evaluacionTotalPorTemporada = capitulosList.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.groupingBy(Capitulo::getTemporada,
                        Collectors.averagingDouble(Capitulo::getEvaluacion)));
        System.out.println(evaluacionTotalPorTemporada + "\n");

*/

/*

       Se crea un objeto que muestra las estadísticas de la serie ingresada

    DoubleSummaryStatistics est = capitulosList.stream()
                .filter(e -> e.getEvaluacion() > 0.0)
                .collect(Collectors.summarizingDouble(Capitulo::getEvaluacion));
        System.out.println("Evaluación promedio de la serie: " + est.getAverage() + "\n" +
                           "Capitulo con peor evaluación: " + est.getMin() + "\n" +
                           "Capitulo con mejor evaluación: " + est.getMax() + "\n");

*/

/*
Fechas

G: Designador de era, ejemplo: DC o AC
y: año, ejemplo: 2010
M: mes con respecto al año, ejemplo: julio, Jul o 10
w: semana con respecto al año, ejemplo: 27 (semana 27 del año)
W: semana con respecto al mes, ejemplo: 3 (tercera semana del mes)
D: dia con respecto al año, ejemplo: 189 (día 189 del año)
d: dia con respecto al mes, ejemplo: 14 (día 14 del mes)
F: dia con respecto a la semana del mes: ejemplo 5 (quinto día de la semana)
E: dia de la semana, ejemplo: Martes o Mar.
a: marcador de AM/PM, ejemplo: AM
H: Hora en el dia (formato 0 - 23), ejemplo: 22
k: Hora en el dia (formato 1 - 24), ejemplo: 7
K: Hora en el dia (formato 0 - 11), ejemplo: 4
h: Hora en el dia (formato 1 - 12), ejemplo: 3
m: minuto en la hora, ejemplo 30
s: segundo en el minuto, ejemplo 59
S: microsegundo en el segundo, ejemplo 978590
z: huso horario, ejemplo GMT-3 (huso horario argentina) o BRT (huso horario brasil)

MMMM significa mostrar mes completo, ejemplo: Febrero
MMM significa mostrar mes abreviado, ejemplo: Feb.
MM significa mostrar mes en número, ejemplo: 2
*/