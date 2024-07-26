package com.tfg.project.tfg_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "seleccion")
public class Seleccion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@JoinColumn(name = "cliente_id")
	Integer clienteId;

	@ManyToOne
	@JoinColumn(name = "pregunta_id")
	Pregunta pregunta;

	@ManyToOne
	@JoinColumn(name = "opcion_id")
	Opcion opcion;
	
	// Si es una opción de desarrollo, esta columna almacenará la respuesta proporcionada por el usuario
    @Column(name="respuesta_desarrollo")
	String opcionSeleccionada;

	public Pregunta getPregunta() {
		return pregunta;
	}

	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}

	public Opcion getOpcion() {
		return opcion;
	}

	public void setOpcion(Opcion opcion) {
		this.opcion = opcion;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getClienteId() {
		return clienteId;
	}

	public void setClienteId(Integer clienteId) {
		this.clienteId = clienteId;
	}
	
    public String getOpcionSeleccionada() {
        return opcionSeleccionada;
    }

    public void setOpcionSeleccionada(String opcionSeleccionada) {
        this.opcionSeleccionada = opcionSeleccionada;
    }
}
