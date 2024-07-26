package com.tfg.project.tfg_backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.project.tfg_backend.model.Opcion;
import com.tfg.project.tfg_backend.repository.OpcionRepository;

@Service
public class OpcionService {

	@Autowired
    private OpcionRepository opcionRepository;


	@Transactional(readOnly = true)
    public Opcion getOpcionById(Long id) {
    	Opcion opcion = opcionRepository.findById(id).orElse(null);
        return opcion;
    }
	
	public List<String> obtenerTextosDeOpcionesDePregunta(Integer preguntaId) {
	    List<Opcion> opciones = opcionRepository.findByPreguntaId(preguntaId);
	    List<String> textosOpciones = new ArrayList<>();
	    for (Opcion opcion : opciones) {
	        textosOpciones.add(opcion.getTexto());
	    }
	    return textosOpciones;
	}
}

