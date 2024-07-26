package com.tfg.project.tfg_backend.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.project.tfg_backend.model.Ejercicio;
import com.tfg.project.tfg_backend.repository.EjercicioRepository;

@Service
public class EjercicioService {
	
	@Autowired
	private EjercicioRepository ejercicioRepository;

	public List<Ejercicio> obtenerEjerciciosPorClienteId(Long clienteId) {
		return ejercicioRepository.findByClienteId(clienteId);
	}

    public List<Ejercicio> obtenerEjerciciosPorRutinaId(Long rutinaId) {
        return ejercicioRepository.findByRutinaId(rutinaId);
    }

    public void actualizarEstadoEjercicio(Long id, Boolean realizado) {
        Ejercicio ejercicio = ejercicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ejercicio no encontrado"));

        ejercicio.setRealizado(realizado);
        if (realizado) {
            ejercicio.setFechaRealizado(new Date()); 
        } else {
            ejercicio.setFechaRealizado(null); 
        }
        ejercicioRepository.save(ejercicio);
    }

    public boolean checkAllExercisesCompleted(Long clienteId) {
        return ejercicioRepository.allEjerciciosCompletados(clienteId);
    }

}
