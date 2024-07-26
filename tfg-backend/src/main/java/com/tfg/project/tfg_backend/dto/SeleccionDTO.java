package com.tfg.project.tfg_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeleccionDTO {
	Integer clienteId;
	String preguntaTexto;
	String opcionSeleccionada;
	String respuestaDesarrollo;

	public Integer getClienteId() {
		return clienteId;
	}
	public void setClienteId(Integer clienteId) {
		this.clienteId = clienteId;
	}
	public String getPreguntaTexto() {
		return preguntaTexto;
	}
	public void setPreguntaTexto(String preguntaTexto) {
		this.preguntaTexto = preguntaTexto;
	}
	public String getOpcionSeleccionada() {
		return opcionSeleccionada;
	}
	public void setOpcionSeleccionada(String opcionSeleccionada) {
		this.opcionSeleccionada = opcionSeleccionada;
	}
	
	public String getRespuestaDesarrollo() {
		return respuestaDesarrollo;
	}
	public void setRespuestaDesarrollo(String respuestaDesarrollo) {
		this.respuestaDesarrollo = respuestaDesarrollo;
	}
}
