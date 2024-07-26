package com.tfg.project.tfg_backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.project.tfg_backend.dto.PreguntaDTO;
import com.tfg.project.tfg_backend.model.Opcion;
import com.tfg.project.tfg_backend.model.Pregunta;
import com.tfg.project.tfg_backend.repository.PreguntaRepository;

@Service
public class PreguntaService  {

	@Autowired
	private PreguntaRepository preguntaRepository;

	public PreguntaDTO getPregunta(Integer id) {
	    Pregunta pregunta = preguntaRepository.findById(id).orElse(null);
	    
	    if(pregunta != null) {
	        PreguntaDTO preguntaDTO = PreguntaDTO.builder()
	                .id(pregunta.getId())
	                .texto(pregunta.getTexto())
	                .opciones(opcionesACadenas(pregunta.getOpciones()))
	                .build();
	        return preguntaDTO;
	    }
	    
	    return null;
	}

	private List<String> opcionesACadenas(List<Opcion> opciones) {
	    List<String> opcionesCadenas = new ArrayList<>();
	    for (Opcion opcion : opciones) {
	        String textoOpcion = opcion.getTexto();

	        if (!opcionesCadenas.contains(textoOpcion)) {
	            opcionesCadenas.add(textoOpcion);
	        }
	    }
	    return opcionesCadenas;
	}

	public List<PreguntaDTO> getPreguntas() {
	    Iterable<Pregunta> preguntas = preguntaRepository.findAll();
	    List<PreguntaDTO> preguntasDTO = new ArrayList<>();
	    List<String> textosPreguntas = new ArrayList<>(); 

	    for (Pregunta pregunta : preguntas) {
	        String textoPregunta = pregunta.getTexto();
	        
	        if (!textosPreguntas.contains(textoPregunta)) {
	            List<String> opcionesCadenas = opcionesACadenas(pregunta.getOpciones());
	            PreguntaDTO preguntaDTO = PreguntaDTO.builder()
	                    .id(pregunta.getId())
	                    .texto(textoPregunta)
	                    .opciones(opcionesCadenas)
	                    .build();
	            preguntasDTO.add(preguntaDTO);
	            textosPreguntas.add(textoPregunta); 
	        }
	    }
	    return preguntasDTO;
	}
	
    public Pregunta getPreguntaPorId(Integer id) {
        Object preguntaObj = preguntaRepository.findById(id).orElse(null);
        if (preguntaObj instanceof Pregunta) {
            return (Pregunta) preguntaObj;
        } else {
            return null;
        }
    }

}