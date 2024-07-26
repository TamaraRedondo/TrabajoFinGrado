package com.tfg.project.tfg_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.project.tfg_backend.model.Objetivo;
import com.tfg.project.tfg_backend.service.ObjetivoService;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
public class ObjetivoController {
	
    @Autowired
    private ObjetivoService objetivoService;
    
    @GetMapping("/total-realizados/{clienteId}")
    public int getTotalEjerciciosRealizados(@PathVariable Integer clienteId) {
        return objetivoService.getTotalEjerciciosRealizadosPorCliente(clienteId);
    }
    
    @GetMapping("/rutinas-completadas/{clienteId}")
    public int getRutinasCompletadas(@PathVariable Integer clienteId) {
        return objetivoService.getRutinasCompletadasPorCliente(clienteId);
    }
    
    @GetMapping("/horasTotales/{clienteId}")
    public Double getHorasTotalesSeleccionadasPorCliente(@PathVariable Integer clienteId) {
        return objetivoService.getHorasTotalesSeleccionadasPorCliente(clienteId);
    }
    
    @GetMapping("/ejercicios/{clienteId}/puntos")
    public int getPuntosPorCliente(@PathVariable Integer clienteId) {
        return objetivoService.getPuntosPorCliente(clienteId);
    }
    
    @PostMapping("/actualizar-objetivo/{clienteId}")
    public void actualizarObjetivo(@PathVariable Integer clienteId) {
        objetivoService.actualizarObjetivo(clienteId);
    }
    
    @GetMapping("/objetivo/{clienteId}")
    public ResponseEntity<Objetivo> obtenerObjetivoPorCliente(@PathVariable Integer clienteId) {
        Objetivo objetivo = objetivoService.obtenerObjetivoPorCliente(clienteId);
        return ResponseEntity.ok().body(objetivo);
    }
    
}
