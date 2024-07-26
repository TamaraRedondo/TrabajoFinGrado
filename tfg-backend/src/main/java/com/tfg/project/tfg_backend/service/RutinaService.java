package com.tfg.project.tfg_backend.service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.project.tfg_backend.model.Ejercicio;
import com.tfg.project.tfg_backend.model.Rutina;
import com.tfg.project.tfg_backend.repository.EjercicioRepository;
import com.tfg.project.tfg_backend.repository.RutinaRepository;

@Service
public class RutinaService {

	@Autowired
	private RutinaRepository rutinaRepository;
	@Autowired
	private EjercicioRepository ejercicioRepository;
	
	public List<Rutina> obtenerRutinaPorClienteId(Integer clienteId) {
		return rutinaRepository.findByClienteId(clienteId);
	}

	public Long obtenerIdDeRutinaPorClienteId(Long clienteId) {

		Optional<Rutina> rutinaOptional = rutinaRepository.findFirstByClienteIdOrderById(clienteId);

		if (rutinaOptional.isPresent()) {
			return rutinaOptional.get().getId();
		} else {
			return null; 
		}
	}


	public Optional<Long> obtenerSiguienteRutinaPorClienteId(Integer clienteId, Long rutinaActualId) {
		List<Rutina> rutinas = rutinaRepository.findByClienteId(clienteId);

		int indiceRutinaActual = rutinas.indexOf(new Rutina(rutinaActualId)); 

		if (indiceRutinaActual != -1 && indiceRutinaActual < rutinas.size() - 1) {
			return Optional.of(rutinas.get(indiceRutinaActual + 1).getId());
		} else {
			return Optional.empty();
		}
	}

	public Long obtenerIdDeUltimaRutinaPorClienteId(Long clienteId) {
		Optional<Rutina> rutinaOptional = rutinaRepository.findFirstByClienteIdOrderByIdDesc(clienteId);

		if (rutinaOptional.isPresent()) {
			return rutinaOptional.get().getId();
		} else {
			return null; 
		}
	}

	public boolean isRutinaCompleted(Long rutinaId) {
		List<Ejercicio> ejercicios = ejercicioRepository.findByRutinaId(rutinaId);

		boolean rutinaCompletada = ejercicios.stream().allMatch(ejercicio -> ejercicio.isRealizado());

		if (rutinaCompletada) {
			actualizarFechaRutina(rutinaId); 
		}

		return rutinaCompletada;
	}
	
    public void actualizarFechaRutina(Long rutinaId) {
        List<Ejercicio> ejercicios = ejercicioRepository.findByRutinaId(rutinaId);

        List<Ejercicio> ejerciciosRealizados = ejercicios.stream()
                .filter(Ejercicio::isRealizado)
                .sorted(Comparator.comparing(Ejercicio::getFechaRealizado).reversed()) 
                .toList();

        Optional<Ejercicio> ultimoEjercicioRealizado = ejerciciosRealizados.stream().findFirst();

        ultimoEjercicioRealizado.ifPresent(ejercicio -> {
            Rutina rutina = rutinaRepository.findById(rutinaId)
                    .orElseThrow(() -> new RuntimeException("Rutina no encontrada"));

            rutina.setRutinaRealizada(ejercicio.getFechaRealizado()); 

            rutinaRepository.save(rutina);
        });
    }

	public void actualizarUltimaVisualizacion(Long rutinaId) throws Exception {
		Optional<Rutina> optionalRutina = rutinaRepository.findById(rutinaId);
		if (optionalRutina.isPresent()) {
			Rutina rutina = optionalRutina.get();
			rutina.setLastViewedAt(new Date());
			rutinaRepository.save(rutina);
		} else {
			throw new Exception("Rutina not found with id: " + rutinaId);
		}
	}

	public Rutina recuperarUltimaRutinaVisualizada(Integer clienteId) throws Exception {
		Optional<Rutina> rutinaOptional = rutinaRepository.findFirstByClienteIdOrderByLastViewedAtDesc(clienteId);
		if (rutinaOptional.isPresent()) {
			return rutinaOptional.get();
		} else {
			throw new Exception("No rutinas found for clienteId: " + clienteId);
		}
	}

}
