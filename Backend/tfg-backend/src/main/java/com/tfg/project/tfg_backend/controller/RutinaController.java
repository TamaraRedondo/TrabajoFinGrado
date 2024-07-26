package com.tfg.project.tfg_backend.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.project.tfg_backend.model.Ejercicio;
import com.tfg.project.tfg_backend.model.Rutina;
import com.tfg.project.tfg_backend.repository.RutinaRepository;
import com.tfg.project.tfg_backend.service.RutinaService;

@RestController
@CrossOrigin(origins = {"http://localhost:4200"})
public class RutinaController {
	
	@Autowired
	private RutinaRepository rutinaRepository;
	
    @Autowired
    private RutinaService rutinaService;
    
    @GetMapping("/rutina/{clienteId}")
    public ResponseEntity<List<Rutina>> obtenerRutinaPorClienteId(@PathVariable Integer clienteId) {
        List<Rutina> rutina = rutinaService.obtenerRutinaPorClienteId(clienteId);
        if (rutina != null) {
            return ResponseEntity.ok(rutina);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/rutina-id/{clienteId}")
    public ResponseEntity<Long> getRutinaId(@PathVariable Long clienteId) {
        Long rutinaId = rutinaService.obtenerIdDeRutinaPorClienteId(clienteId);
        if (rutinaId != null) {
            return new ResponseEntity<>(rutinaId, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/rutina-id/{clienteId}/{rutinaActualId}")
    public ResponseEntity<Long> getRutinaId(@PathVariable Integer clienteId, @PathVariable Long rutinaActualId) {
        Optional<Long> siguienteRutinaIdOptional = rutinaService.obtenerSiguienteRutinaPorClienteId(clienteId, rutinaActualId);
        if (siguienteRutinaIdOptional.isPresent()) {
            return new ResponseEntity<>(siguienteRutinaIdOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/ultrutina-id/{clienteId}")
    public ResponseEntity<Long> getUltRutinaId(@PathVariable Long clienteId) {
        Long rutinaId = rutinaService.obtenerIdDeUltimaRutinaPorClienteId(clienteId);
        if (rutinaId != null) {
            return new ResponseEntity<>(rutinaId, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/rutina/{rutinaId}/completada")
    public boolean isRutinaCompleted(@PathVariable Long rutinaId) {
        return rutinaService.isRutinaCompleted(rutinaId);
    }
    
    @PutMapping("/actualizar-ultima-visualizacion/{rutinaId}") //*
    public ResponseEntity<Void> actualizarUltimaVisualizacion(@PathVariable Long rutinaId) throws Exception {
        rutinaService.actualizarUltimaVisualizacion(rutinaId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/ultima-visualizada/{clienteId}")
    public ResponseEntity<Rutina> obtenerUltimaVisualizada(@PathVariable Integer clienteId) throws Exception {
        Rutina rutina = rutinaService.recuperarUltimaRutinaVisualizada(clienteId);
        return ResponseEntity.ok(rutina);
    }
    
    @GetMapping("/rutina-fecha/{clienteId}")
    public ResponseEntity<List<Date>> obtenerRutinaRealizadaPorClienteId(@PathVariable Integer clienteId) {
        List<Date> rutinasRealizadas = rutinaRepository.findRutinaRealizadaByClienteIdIsNotNull(clienteId);
        if (!rutinasRealizadas.isEmpty()) {
            return ResponseEntity.ok(rutinasRealizadas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{clienteId}/{rutinaRealizada}")
    public ResponseEntity<Rutina> obtenerRutinaPorClienteIdYFecha(@PathVariable Integer clienteId, @PathVariable String rutinaRealizada) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        Date fecha;
        try {
            fecha = formatter.parse(rutinaRealizada);
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(null); 
        }

        Optional<Rutina> rutina = rutinaRepository.findByClienteIdAndRutinaRealizada(clienteId, fecha);
        if (rutina.isPresent()) {
            return ResponseEntity.ok(rutina.get()); 
        } else {
            return ResponseEntity.notFound().build(); 
        }
    }
    
    @GetMapping("/buscar-por-fecha/{rutinaRealizada}")
    public ResponseEntity<List<Ejercicio>> buscarPorFecha(@PathVariable("rutinaRealizada") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rutinaRealizada) {
        Date fechaBusqueda = Date.from(rutinaRealizada.atZone(ZoneId.systemDefault()).toInstant());
        Optional<Rutina> rutina = rutinaRepository.findByRutinaRealizada(fechaBusqueda);
        if (rutina.isPresent()) {
            List<Ejercicio> ejercicios = rutina.get().getEjercicios();
            return ResponseEntity.ok(ejercicios);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/contar-no-realizadas/{clienteId}")
    public ResponseEntity<Integer> contarRutinasNoRealizadasPorClienteId(@PathVariable Integer clienteId) {
        int cantidadRutinasNoRealizadas = rutinaRepository.countByClienteIdAndRutinaRealizadaIsNull(clienteId);
        return ResponseEntity.ok(cantidadRutinasNoRealizadas);
    } 
}
