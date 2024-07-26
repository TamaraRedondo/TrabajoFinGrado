package com.tfg.project.tfg_backend.model;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rutina")
public class Rutina {

	public Rutina(Long id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JoinColumn(name = "cliente_id")
	Integer clienteId;

	@JsonBackReference
	@OneToMany(mappedBy = "rutina", cascade = CascadeType.ALL)
	private List<Ejercicio> ejercicios;

	@Temporal(TemporalType.TIMESTAMP)
	private Date rutinaRealizada;

	@Column(name = "last_viewed_at")
	private Date lastViewedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getClienteId() {
		return clienteId;
	}

	public void setClienteId(Integer clienteId) {
		this.clienteId = clienteId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Rutina rutina = (Rutina) o;
		return Objects.equals(id, rutina.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	public List<Ejercicio> getEjercicios() {
		return ejercicios;
	}

	public void setEjercicios(List<Ejercicio> ejercicios) {
		this.ejercicios = ejercicios;
	}

	public Date getRutinaRealizada() {
		return rutinaRealizada;
	}

	public void setRutinaRealizada(Date rutinaRealizada) {
		this.rutinaRealizada = rutinaRealizada;
	}

	public Date getLastViewedAt() {
		return lastViewedAt;
	}

	public void setLastViewedAt(Date lastViewedAt) {
		this.lastViewedAt = lastViewedAt;
	}

}