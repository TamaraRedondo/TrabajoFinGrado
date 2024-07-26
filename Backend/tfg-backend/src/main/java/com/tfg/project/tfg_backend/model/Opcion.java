package com.tfg.project.tfg_backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table(name = "opcion")
public class Opcion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	String texto;
	
	boolean esDesarrollo;  

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "pregunta_id")
	Pregunta pregunta;
	
	
    @Override
    public String toString() {
        return "Opcion{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                '}';
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Pregunta getPregunta() {
		return pregunta;
	}

	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}

}