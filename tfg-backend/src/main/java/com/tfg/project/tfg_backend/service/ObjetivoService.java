package com.tfg.project.tfg_backend.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg.project.tfg_backend.model.Ejercicio;
import com.tfg.project.tfg_backend.model.Objetivo;
import com.tfg.project.tfg_backend.repository.EjercicioRepository;
import com.tfg.project.tfg_backend.repository.ObjetivoRepository;
import com.tfg.project.tfg_backend.repository.SeleccionRepository;

@Service
public class ObjetivoService {
	
	@Autowired
	private EjercicioRepository ejercicioRepository;
	@Autowired
	private SeleccionRepository seleccionRepository;
	@Autowired
	private ObjetivoRepository objetivoRepository;

	public int getTotalEjerciciosRealizadosPorCliente(Integer clienteId) {
	    int totalEjercicios = ejercicioRepository.countByClienteIdAndRealizadoTrue(clienteId);
	    System.out.println("Total de ejercicios realizados por el cliente " + clienteId + ": " + totalEjercicios);
	    return totalEjercicios;
	}

	public int getRutinasCompletadasPorCliente(Integer clienteId) {
	    int rutinasCompletadas = ejercicioRepository.countRutinasCompletadasByClienteId(clienteId);
	    System.out.println("Total de rutinas completadas por el cliente " + clienteId + ": " + rutinasCompletadas);
	    return rutinasCompletadas;
	}

	public Double getHorasTotalesSeleccionadasPorCliente(Integer clienteId) {
	    Integer opcionId = seleccionRepository.findOpcionIdByClienteIdAndPreguntaId(clienteId, 11L);
	    int rutinasRealizadas = objetivoRepository.findRutinasRealizadasByClienteId(clienteId);
	    double horasTotales = 0;

	    switch (opcionId) {
	        case 51:
	            horasTotales = 0.5 * rutinasRealizadas;
	            break;
	        case 52:
	            horasTotales = 1.0 * rutinasRealizadas;
	            break;
	        case 53:
	            horasTotales = 1.5 * rutinasRealizadas;
	            break;
	        case 54:
	            horasTotales = 1.5 * rutinasRealizadas;
	            break;
	        default:
	            horasTotales = 0;
	            break;
	    }

	    System.out.println("Horas totales seleccionadas por el cliente " + clienteId + ": " + horasTotales);
	    return horasTotales;
	}

	public int getPuntosPorCliente(Integer clienteId) {
	    List<Ejercicio> ejerciciosRealizados = ejercicioRepository.findByClienteIdAndRealizadoTrue(clienteId);
	    int puntosTotales = 0;

	    for (Ejercicio ejercicio : ejerciciosRealizados) {
	        puntosTotales += getPuntosPorEjercicio(ejercicio);
	    }

	    System.out.println("Puntos totales para el cliente " + clienteId + ": " + puntosTotales);
	    return puntosTotales;
	}
	
	private int getPuntosPorEjercicio(Ejercicio ejercicio) {
	    int puntos = 0;
	    
	    if (ejercicio.getDescripcion() != null && !ejercicio.getDescripcion().isEmpty()) {
	        try {
	            JSONObject json = new JSONObject(ejercicio.getDescripcion());
	            
	            if (json.has("Difficulty level")) {
	                int nivelDificultad = json.getInt("Difficulty level");
	                puntos = nivelDificultad;
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    return puntos;
	}

	@Transactional
    public void actualizarObjetivo(Integer clienteId) {
        int totalEjerciciosRealizados = getTotalEjerciciosRealizadosPorCliente(clienteId);
        int totalRutinasRealizadas = getRutinasCompletadasPorCliente(clienteId);
        Objetivo objetivo = objetivoRepository.findByClienteId(clienteId);
        if (objetivo == null) {
            objetivo = new Objetivo();
            objetivo.setClienteId(clienteId);
        }
        objetivo.setEjerciciosRealizados(totalEjerciciosRealizados);
        objetivo.setRutinasRealizadas(totalRutinasRealizadas);
        objetivoRepository.save(objetivo);
        actualizarObjetivo(clienteId, totalRutinasRealizadas);
    }
	
	@Transactional
	public void actualizarObjetivo(Integer clienteId, Integer totalRutinasRealizadas) {
	    double horasTotalesSeleccionadas = getHorasTotalesSeleccionadasPorCliente(clienteId);
	    int puntosObtenidos = getPuntosPorCliente(clienteId);
	    
	    Objetivo objetivo = objetivoRepository.findByClienteId(clienteId);
	    if (objetivo == null) {
	        objetivo = new Objetivo();
	        objetivo.setClienteId(clienteId);
	    }
	    objetivo.setHorasTotales(horasTotalesSeleccionadas * totalRutinasRealizadas);
	    objetivo.setPuntosObtenidos(puntosObtenidos);
	    objetivoRepository.save(objetivo);
	}

    public Objetivo obtenerObjetivoPorCliente(Integer clienteId) {
        return objetivoRepository.findByClienteId(clienteId);
    }

}
