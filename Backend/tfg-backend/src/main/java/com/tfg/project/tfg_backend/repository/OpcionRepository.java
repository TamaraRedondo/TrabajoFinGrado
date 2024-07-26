package com.tfg.project.tfg_backend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.tfg.project.tfg_backend.model.Opcion;
import com.tfg.project.tfg_backend.model.Pregunta;

@Repository
public interface OpcionRepository extends CrudRepository<Opcion, Long>{
    Opcion findByTextoAndPregunta(String texto, Pregunta pregunta);
    
    List<Opcion> findByPreguntaId(Integer preguntaId);
    
    Opcion findByPreguntaIdAndEsDesarrolloTrue(Integer preguntaId);
}