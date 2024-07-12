package com.aluracursos.screenmatch.controller;

import com.aluracursos.screenmatch.dto.CapituloDTO;
import com.aluracursos.screenmatch.dto.SerieDTO;
import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/series")
public class SerieController {

    @Autowired
    private SerieService servicio;

    @GetMapping()
    public List<SerieDTO> obtenerTodasLasSeries(){

        return servicio.obtenerTodasLasSeries();

    }
    
    @GetMapping("/top5")
    public List<SerieDTO> obtenerTop5(){

        return servicio.obtenerTop5();

    }

    @GetMapping("/lanzamientos")
    public List<SerieDTO> obtengo5SeriesConEpisodiosMasRecientes(){

        return servicio.obtener5SeriesConCapitulosMasRecientes();

    }

    @GetMapping("/{id}")
    public SerieDTO obtenerPorId(@PathVariable Long id){
        return servicio.obtenerporId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<CapituloDTO> buscarTodasLasTemporadas(@PathVariable Long id){
        return servicio.ObtenerTodasLasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{temporada}")
    public List<CapituloDTO> buscarUnaTemporada(@PathVariable Long id, @PathVariable Long temporada){
        return servicio.buscarUnaTemporada(id, temporada);
    }

    @GetMapping("/categoria/{categoria}")
    public List<SerieDTO> ObtenerSeriesPorCategoria(@PathVariable String categoria){

        return servicio.buscarPorCategoria(categoria);
    }

}
