package com.aluracursos.screenmatchFrases.service;

import com.aluracursos.screenmatchFrases.DTO.FraseDTO;
import com.aluracursos.screenmatchFrases.model.Frase;
import com.aluracursos.screenmatchFrases.repository.FrasesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FrasesService {

    @Autowired
    private FrasesRepository repository;

    public FraseDTO convertirAFraseDTO(Frase frase){
        return new FraseDTO(frase.getTitulo(),
                            frase.getFrase(),
                            frase.getAutor(),
                            frase.getposter() );
    }

    public FraseDTO obtenerFraseRandom(){

        return convertirAFraseDTO(repository.ObtenerRegistroRandom());
    }

}
