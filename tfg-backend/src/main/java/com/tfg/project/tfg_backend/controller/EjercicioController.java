package com.tfg.project.tfg_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.project.tfg_backend.model.Ejercicio;
import com.tfg.project.tfg_backend.service.EjercicioService;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
public class EjercicioController {
	
    @Autowired
    private EjercicioService ejercicioService;
    
    
    @GetMapping("/ejercicios/{clienteId}")
    public ResponseEntity<List<Ejercicio>> obtenerEjerciciosPorClienteId(@PathVariable Long clienteId) {
        List<Ejercicio> ejercicios = ejercicioService.obtenerEjerciciosPorClienteId(clienteId);
        if (ejercicios != null && !ejercicios.isEmpty()) {
            return new ResponseEntity<>(ejercicios, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/ejercicio/{rutinaId}")
    public ResponseEntity<List<Ejercicio>> obtenerEjerciciosPorRutinaId(@PathVariable Long rutinaId) {
        List<Ejercicio> ejercicios = ejercicioService.obtenerEjerciciosPorRutinaId(rutinaId);
        if (ejercicios != null && !ejercicios.isEmpty()) {
            return new ResponseEntity<>(ejercicios, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PatchMapping("ejercicio/{id}")
    public ResponseEntity<?> actualizarEstadoEjercicio(@PathVariable Long id, @RequestBody Boolean realizado) {
        try {
        	ejercicioService.actualizarEstadoEjercicio(id, realizado);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("No se pudo actualizar el estado del ejercicio");
        }
    }
    
    @GetMapping("/todosCompletados/{clienteId}") //*
    public ResponseEntity<Boolean> checkAllExercisesCompleted(@PathVariable Long clienteId) {
        boolean allCompleted = ejercicioService.checkAllExercisesCompleted(clienteId);
        return ResponseEntity.ok(allCompleted);
    }
    
}
