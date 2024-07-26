package com.tfg.project.tfg_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.project.tfg_backend.dto.PreguntaDTO;
import com.tfg.project.tfg_backend.model.Pregunta;
import com.tfg.project.tfg_backend.service.PreguntaService;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping(value = "api/v1/preguntas")
public class PreguntaController {

	@Autowired
	private PreguntaService preguntaService;


	@GetMapping(value = "/{id}")
	public ResponseEntity<PreguntaDTO> getPregunta(@PathVariable Integer id){
	    PreguntaDTO preguntaDTO = preguntaService.getPregunta(id);
	    if(preguntaDTO == null) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(preguntaDTO);
	}
	
	@GetMapping(value = "/listar")
	public ResponseEntity<List<PreguntaDTO>> getPreguntas() {
	    List<PreguntaDTO> preguntasDTO = preguntaService.getPreguntas();
	    if (preguntasDTO.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(preguntasDTO);
	}
	
	@GetMapping(value = "/pregunta/{id}")
	public ResponseEntity<Pregunta> getPreguntaPorId(@PathVariable("id") Integer id) {
	    Pregunta pregunta = preguntaService.getPreguntaPorId(id);
	    if (pregunta == null) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(pregunta);
	}
	
}