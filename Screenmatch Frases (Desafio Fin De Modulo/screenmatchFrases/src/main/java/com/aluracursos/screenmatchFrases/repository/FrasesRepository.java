package com.aluracursos.screenmatchFrases.repository;

import com.aluracursos.screenmatchFrases.DTO.FraseDTO;
import com.aluracursos.screenmatchFrases.model.Frase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FrasesRepository extends JpaRepository<Frase, Long> {

    @Query("SELECT frase FROM Frase frase ORDER BY function('RANDOM') LIMIT 1")
    Frase ObtenerRegistroRandom();

}
