package com.tfg.project.tfg_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tfg.project.tfg_backend.model.Ejercicio;

@Repository
public interface EjercicioRepository extends CrudRepository<Ejercicio, Long>{
	List<Ejercicio> findByRutinaId(Long rutinaId);
	List<Ejercicio> findByClienteId(Long clienteId);
	List<Ejercicio> findByClienteIdAndRealizadoTrue(Integer clienteId);
	
	@Query("SELECT COUNT(e) = 0 FROM Ejercicio e WHERE e.clienteId = :clienteId AND e.realizado = false")
	boolean allEjerciciosCompletados(Long clienteId);

	@Query("SELECT COUNT(e) FROM Ejercicio e WHERE e.clienteId = :clienteId AND e.realizado = true")
    int countByClienteIdAndRealizadoTrue(Integer clienteId);
	
	@Query("SELECT COUNT(DISTINCT e.rutina.id) FROM Ejercicio e WHERE e.clienteId = ?1 AND e.realizado = true")
    int countRutinasCompletadasByClienteId(Integer clienteId);
	
	
}
