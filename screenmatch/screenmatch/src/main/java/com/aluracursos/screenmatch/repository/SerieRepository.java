package com.aluracursos.screenmatch.repository;

import com.aluracursos.screenmatch.dto.CapituloDTO;
import com.aluracursos.screenmatch.model.Capitulo;
import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainsIgnoreCase(String serieBuscada);

    Optional<Serie> findByTituloIgnoreCase(String serieBuscada);

    List<Serie> findTop5ByOrderByEvaluacionDesc();

    List<Serie> findByGenero(Categoria genero);

    List<Serie> findByTotalDeTemporadasLessThanEqual(Integer cantDeTemporadas);

    List<Serie> findByEvaluacionGreaterThanEqual(Double evaluacion);

    @Query("SELECT serie FROM Serie serie JOIN serie.capitulos episode GROUP BY serie ORDER BY MAX(episode.fechaDeLanzamiento) DESC LIMIT 5")
    List<Serie> CapitulosFechaDeLanzamientoDesc();

    @Query("SELECT episode FROM Serie serie JOIN serie.capitulos episode WHERE episode.titulo ILIKE %:nombreCapitulo%")
    List<Capitulo> capitulosPorNombre(String nombreCapitulo);

    @Query("SELECT episode FROM Serie serie JOIN serie.capitulos episode WHERE serie = :serie ORDER BY  episode.evaluacion DESC LIMIT 5")
    List<Capitulo> top5CapitulosDeUnaSerie(Serie serie);

    @Query("SELECT episode FROM Serie serie JOIN serie.capitulos episode WHERE serie.id = :id AND episode.temporada = :numeroDeTemporada")
    List<Capitulo> obtenerUnatemporada(Long id, Long numeroDeTemporada);


    /*
    Existen 3 tipos de query a la hora de hacer llamados de registros a una base de datos:

    . Derived query: Son las que se utilizaron hasta ahora. Estas query proveídas por Spring permiten definir métodos de consulta específicos,
                     para situaciones específicas, con un retorno indicado previamente de forma simple y concisa,
                     siempre trabajando con una tabla específica indicada previamente.
                     Ej:
                     List<Serie> findByTotalDeTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(int cantDeTemporadas, Double evaluacion);

    . Query JPQL (Java persistence Query language): Este tipo de query es el lenguaje de consulta orientado a objetos proveído por JPA.
                                                    Es un pseudo lenguaje tipo SQL en el cual se utiliza para realizar consultas en
                                                    bases de datos relacionales en el cual se crea un método con retorno indicado previamente,
                                                    trabajando específicamente con clases, sus atributos y métodos personalizados.
                                                    Ej:
                                                    @Query("SELECT s FROM Serie s WHERE s.totalDeTemporadas <= :temporadasMax AND s.evaluacion >= :evaluacionMin")
                                                    List<Serie> seriesPorTemporadaYEvaluacion(int temporadasMax, Double evaluacionMin);

    . SQL nativo: Este es el query común utilizado en toda base de datos del tipo SQL, donde en java simplemente se escribe el comando a ejecutar,
                  su tabla y atributos correspondientes a ella, se le asocia una función para ejecutarlo y listo.
                  Lo único malo de este tipo de query es que es muy rígido y poco adaptable.
                  Ej:
                  @Query(value = "SELECT * FROM series WHERE series.total_temporadas <= 6 AND series.evaluacion >= 7.5", nativeQuery = true)
                  Lista<Serie> seriesFiltradasArbitrariamente();
     */
}
