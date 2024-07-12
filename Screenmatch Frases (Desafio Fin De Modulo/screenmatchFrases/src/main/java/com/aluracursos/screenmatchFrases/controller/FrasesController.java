package com.aluracursos.screenmatchFrases.controller;

import com.aluracursos.screenmatchFrases.DTO.FraseDTO;
import com.aluracursos.screenmatchFrases.service.FrasesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController

public class FrasesController {

    @Autowired
    private FrasesService servicio;

    @GetMapping("/series/frases")
    public FraseDTO obtenerFraseRandom(){

        return servicio.obtenerFraseRandom();

    }


}
