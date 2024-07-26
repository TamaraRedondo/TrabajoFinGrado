package com.tfg.project.tfg_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.project.tfg_backend.model.Opcion;
import com.tfg.project.tfg_backend.service.OpcionService;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api")
public class OpcionController {
    @Autowired
    private OpcionService opcionService;
    
    @GetMapping("/opcion/{id}")
    public Opcion getOpcionById(@PathVariable Long id) {
        return opcionService.getOpcionById(id);
    }
    
    @GetMapping("/preguntas/{preguntaId}/opciones")
    public ResponseEntity<List<String>> obtenerTextosDeOpcionesDePregunta(@PathVariable Integer preguntaId) {
        List<String> textosOpciones = opcionService.obtenerTextosDeOpcionesDePregunta(preguntaId);
        if (textosOpciones.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(textosOpciones);
    }
}