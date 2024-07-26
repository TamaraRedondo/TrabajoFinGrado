package com.tfg.project.tfg_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tfg.project.tfg_backend.model.Seleccion;

@Repository
public interface SeleccionRepository extends CrudRepository<Seleccion, Long>{
	List<Seleccion> findAll();
	
	Iterable<Seleccion> findByClienteId(Integer clienteId);

	Optional<Seleccion> findById(Integer id);
	
	@Query("SELECT s FROM Seleccion s WHERE s.clienteId = :clienteId AND s.pregunta.id = :preguntaId")
    Seleccion findByClienteIdAndPreguntaId(@Param("clienteId") Integer clienteId, @Param("preguntaId") Integer preguntaId);
	
	@Query("SELECT s.opcion.id FROM Seleccion s WHERE s.clienteId = :clienteId AND s.pregunta.id = :preguntaId")
    Integer findOpcionIdByClienteIdAndPreguntaId(@Param("clienteId") Integer clienteId, @Param("preguntaId") Long preguntaId);
	
	@Query("SELECT s.opcion.id FROM Seleccion s WHERE s.clienteId = :clienteId AND s.pregunta.id = :preguntaId")
    Integer findOpcionIdByClienteIdAndPreguntaId(@Param("clienteId") Integer clienteId, @Param("preguntaId") Integer preguntaId);
}