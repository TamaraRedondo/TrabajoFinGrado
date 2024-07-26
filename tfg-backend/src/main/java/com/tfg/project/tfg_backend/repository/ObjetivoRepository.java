package com.tfg.project.tfg_backend.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tfg.project.tfg_backend.model.Objetivo;

@Repository
public interface ObjetivoRepository extends CrudRepository<Objetivo, Integer> {
	 Objetivo findByClienteId(Integer clienteId);
	 
	 @Query("SELECT o.rutinasRealizadas FROM Objetivo o WHERE o.clienteId = ?1")
	    int findRutinasRealizadasByClienteId(Integer clienteId);
	 
}
