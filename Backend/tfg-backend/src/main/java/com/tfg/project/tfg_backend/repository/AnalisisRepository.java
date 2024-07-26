package com.tfg.project.tfg_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tfg.project.tfg_backend.model.Analisis;

@Repository
public interface AnalisisRepository extends CrudRepository<Analisis, Integer> {
	List<Analisis> findByClienteId(Integer clienteId);
	Optional<Analisis> findFirstByClienteId(Integer clienteId);
	void deleteByClienteId(Integer clienteId);
}
