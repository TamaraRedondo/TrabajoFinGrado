package com.tfg.project.tfg_backend.model;

public class AnalisisRequest {
	private Long analisisId;
    private String nuevaDescripcion;

    public AnalisisRequest() {
    }
    
    public AnalisisRequest(Long analisisId, String nuevaDescripcion) {
        this.analisisId = analisisId;
        this.nuevaDescripcion = nuevaDescripcion;
    }

    public Long getAnalisisId() {
        return analisisId;
    }

    public void setAnalisisId(Long analisisId) {
        this.analisisId = analisisId;
    }

    public String getNuevaDescripcion() {
        return nuevaDescripcion;
    }

    public void setNuevaDescripcion(String nuevaDescripcion) {
        this.nuevaDescripcion = nuevaDescripcion;
    }
}
