package com.tfg.project.tfg_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pregunta")
public class Pregunta {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	String texto;
	
    boolean esDesarrollo;

    @JsonManagedReference
	@OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL)
	List<Opcion> opciones;
	
	@Override
    public String toString() {
        return "Pregunta{" +
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
	
    public boolean isEsDesarrollo() {
        return esDesarrollo;
    }

    public void setEsDesarrollo(boolean esDesarrollo) {
        this.esDesarrollo = esDesarrollo;
    }

	public List<Opcion> getOpciones() {
		return opciones;
	}

	public void setOpciones(List<Opcion> opciones) {
		this.opciones = opciones;
	}

}