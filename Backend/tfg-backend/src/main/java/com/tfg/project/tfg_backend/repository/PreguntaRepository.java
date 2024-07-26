package com.tfg.project.tfg_backend.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.tfg.project.tfg_backend.model.Pregunta;

public interface PreguntaRepository extends CrudRepository<Pregunta, Long>{
	Optional<Pregunta> findById(Integer id);
	Pregunta findByTexto(String texto);
	Pregunta findByIdAndEsDesarrolloTrue(Integer id); 
}
