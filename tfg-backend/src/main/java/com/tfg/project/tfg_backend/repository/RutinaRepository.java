package com.tfg.project.tfg_backend.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tfg.project.tfg_backend.model.Rutina;

@Repository
public interface RutinaRepository extends CrudRepository<Rutina, Integer> {
    List<Rutina> findByClienteId(Integer clienteId);
   
    Optional<Rutina> findFirstByClienteIdOrderById(Long clienteId);
	Optional<Rutina> findFirstByClienteIdOrderByIdDesc(Long clienteId);
	Optional<Rutina> findById(Long rutinaId);
	Optional<Rutina> findFirstByClienteIdOrderByLastViewedAtDesc(Integer clienteId);

	@Query("SELECT r.rutinaRealizada FROM Rutina r WHERE r.clienteId = :clienteId AND r.rutinaRealizada IS NOT NULL")
    List<Date> findRutinaRealizadaByClienteIdIsNotNull(Integer clienteId);
	
	Optional<Rutina> findByClienteIdAndRutinaRealizada(Integer clienteId, Date rutinaRealizada);
	
	Optional<Rutina> findByRutinaRealizada(Date rutinaRealizada);
	 
	int countByClienteIdAndRutinaRealizadaIsNull(Integer clienteId);
}
