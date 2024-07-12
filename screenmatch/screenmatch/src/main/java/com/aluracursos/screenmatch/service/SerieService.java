package com.aluracursos.screenmatch.service;

import com.aluracursos.screenmatch.dto.CapituloDTO;
import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private SerieRepository repository;

    public List<SerieDTO> obtenerTodasLasSeries() {

        return convertirASerieDTO(repository.findAll());

    }

    public List<SerieDTO> obtenerTop5() {

        return convertirASerieDTO(repository.findTop5ByOrderByEvaluacionDesc());

    }

    public List<SerieDTO> obtener5SeriesConCapitulosMasRecientes(){

        return convertirASerieDTO(repository.CapitulosFechaDeLanzamientoDesc());

    }

    public List<SerieDTO> convertirASerieDTO(List<Serie> serie){
        
        return serie.stream()
                .map(e -> new SerieDTO(e.getId(),
                                       e.getTitulo(),
                                       e.getTotalDeTemporadas(),
                                       e.getEvaluacion(),
                                       e.getGenero(),
                                       e.getSinopsis(),
                                       e.getActores(),
                                       e.getImagenPromocional()))
                .collect(Collectors.toList());
    
    }


    public SerieDTO obtenerporId(Long id) {

        Optional<Serie> serieOpcional = repository.findById(id);

        if (serieOpcional.isPresent()){
            Serie serie = serieOpcional.get();
            return new SerieDTO(serie.getId(),
                                serie.getTitulo(),
                                serie.getTotalDeTemporadas(),
                                serie.getEvaluacion(),
                                serie.getGenero(),
                                serie.getSinopsis(),
                                serie.getActores(),
                                serie.getImagenPromocional() );
        }

        return null;

    }

    public List<CapituloDTO> ObtenerTodasLasTemporadas(Long id) {

        Optional<Serie> serieOpcional = repository.findById(id);

        if (serieOpcional.isPresent()){
            Serie serie = serieOpcional.get();
            return serie.getCapitulos().stream()
                    .map(episodio -> new CapituloDTO(episodio.getTemporada(), episodio.getTitulo(), episodio.getNumeroDeCapitulo()))
                    .collect(Collectors.toList());
        }

        return null;
    }

    public List<CapituloDTO> buscarUnaTemporada(Long id, Long temporada) {

        return repository.obtenerUnatemporada(id, temporada).stream()
                .map(episodio -> new CapituloDTO(episodio.getTemporada(), episodio.getTitulo(), episodio.getNumeroDeCapitulo()))
                .collect(Collectors.toList());

    }

    public List<SerieDTO> buscarPorCategoria(String categoria) {

        Categoria genero = Categoria.fromEspanol(categoria);

        return convertirASerieDTO(repository.findByGenero(genero));

    }
}
