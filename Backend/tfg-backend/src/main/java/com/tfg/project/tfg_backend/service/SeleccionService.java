package com.tfg.project.tfg_backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.project.tfg_backend.dto.SeleccionDTO;
import com.tfg.project.tfg_backend.model.Opcion;
import com.tfg.project.tfg_backend.model.Pregunta;
import com.tfg.project.tfg_backend.model.Seleccion;
import com.tfg.project.tfg_backend.model.SeleccionRequest;
import com.tfg.project.tfg_backend.repository.OpcionRepository;
import com.tfg.project.tfg_backend.repository.PreguntaRepository;
import com.tfg.project.tfg_backend.repository.SeleccionRepository;

@Service
public class SeleccionService{
	@Autowired
	private SeleccionRepository seleccionDao;
	@Autowired
	private PreguntaRepository preguntaDao;
	@Autowired
	private OpcionRepository opcionDao;
	
	public List<SeleccionDTO> obtenerSeleccionesDTO() {
	    Iterable<Seleccion> selecciones = seleccionDao.findAll();
	    return SeleccionesADTOs(selecciones);
	}
	
	public List<SeleccionDTO> obtenerSeleccionesID(Integer clienteId) {
	    Iterable<Seleccion> selecciones = seleccionDao.findByClienteId(clienteId);
	    return SeleccionesADTOs(selecciones);
	}
	
	
	private List<SeleccionDTO> SeleccionesADTOs(Iterable<Seleccion> selecciones) {
	    List<SeleccionDTO> seleccionesDTO = new ArrayList<>();
	    for (Seleccion seleccion : selecciones) {
	        SeleccionDTO seleccionDTO = SeleccionDTO.builder()
	                .clienteId(seleccion.getClienteId())
	                .preguntaTexto(seleccion.getPregunta().getTexto())
	                .opcionSeleccionada(seleccion.getOpcion().getTexto())
	                .respuestaDesarrollo(seleccion.getOpcionSeleccionada())
	                .build();
	        seleccionesDTO.add(seleccionDTO);
	    }
	    return seleccionesDTO;
	}
	
	@Transactional
	public void guardarSeleccion(SeleccionDTO seleccionDTO) {
        Pregunta pregunta = preguntaDao.findByTexto(seleccionDTO.getPreguntaTexto());

        if (pregunta != null) {
            if (pregunta.isEsDesarrollo()) {
                Opcion opcionDesarrollo = opcionDao.findByPreguntaIdAndEsDesarrolloTrue(pregunta.getId());
                if (opcionDesarrollo != null) {
                    
                    Seleccion seleccion = new Seleccion();
                    seleccion.setClienteId(seleccionDTO.getClienteId());
                    seleccion.setPregunta(pregunta);
                    seleccion.setOpcion(opcionDesarrollo);
                    seleccion.setOpcionSeleccionada(seleccionDTO.getOpcionSeleccionada());
                    seleccionDao.save(seleccion);
                } else {
                	throw new IllegalStateException("Opción de desarrollo no encontrada para la pregunta: " + pregunta.getTexto());
                }
            } else {
            	    Seleccion seleccion = new Seleccion();
            	    seleccion.setClienteId(seleccionDTO.getClienteId());
            	    seleccion.setPregunta(pregunta);
            	    
            	    Opcion opcion = opcionDao.findByTextoAndPregunta(seleccionDTO.getOpcionSeleccionada(), pregunta);
            	    if (opcion == null) {
            	        System.out.println("La opción no fue encontrada en la base de datos: " + seleccionDTO.getOpcionSeleccionada());
            	        throw new IllegalArgumentException("Opción no encontrada: " + seleccionDTO.getOpcionSeleccionada());
            	    }
            	    seleccion.setOpcion(opcion);
            	    
            	    System.out.println("Pregunta recibida en el servicio: " + pregunta);
            	    System.out.println("Opción recibida en el servicio: " + opcion);
            	    
            	    seleccionDao.save(seleccion);
            }
        } else {
        	throw new IllegalArgumentException("Pregunta no encontrada: " + seleccionDTO.getPreguntaTexto());
        }
    }
	
	@Transactional
	public SeleccionDTO actualizarSeleccion(SeleccionRequest cambioSeleccionRequest) {
	    Seleccion seleccion = seleccionDao.findById(cambioSeleccionRequest.getSeleccionId())
	            .orElseThrow(() -> new EntityNotFoundException("La selección no fue encontrada"));

	    if (!seleccion.getPregunta().getTexto().equals(cambioSeleccionRequest.getPreguntaTexto())) {
	        throw new IllegalArgumentException("La pregunta asociada a la selección no coincide con la pregunta especificada");
	    }

	    Opcion nuevaOpcion = opcionDao.findByTextoAndPregunta(cambioSeleccionRequest.getNuevaOpcionTexto(), seleccion.getPregunta());
	    if (nuevaOpcion == null) {
	        throw new IllegalArgumentException("La nueva opción no fue encontrada en la base de datos");
	    }

	    seleccion.setOpcion(nuevaOpcion);
	    seleccion.setOpcionSeleccionada(cambioSeleccionRequest.getNuevaOpcionTexto());

	    seleccionDao.save(seleccion);

	    return new SeleccionDTO(seleccion.getClienteId(), seleccion.getPregunta().getTexto(), seleccion.getOpcion().getTexto(), seleccion.getOpcionSeleccionada());
	}

	
    @Transactional(readOnly = true)
    public String obtenerTextoOpcionSeleccionada(Integer clienteId, Integer preguntaId) {
        Seleccion seleccion = seleccionDao.findByClienteIdAndPreguntaId(clienteId, preguntaId);
        if (seleccion != null) {
            return seleccion.getOpcion().getTexto();
        } else {
            throw new EntityNotFoundException("Selección no encontrada para el clienteId y preguntaId proporcionados");
        }
    }

	@Transactional
	public Integer obtenerSeleccionId(Integer clienteId, Integer preguntaId) {
        Seleccion seleccion = seleccionDao.findByClienteIdAndPreguntaId(clienteId, preguntaId);
        if (seleccion != null) {
            return seleccion.getId();
        } else {
            return null; 
        }
    }


}