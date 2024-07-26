package com.tfg.project.tfg_backend.model;

public class SeleccionRequest {
	private Integer seleccionId;
    private String preguntaTexto;
    private String nuevaOpcionTexto;

    public SeleccionRequest(Integer seleccionId, String preguntaTexto, String nuevaOpcionTexto) {
        this.seleccionId = seleccionId;
        this.preguntaTexto = preguntaTexto;
        this.nuevaOpcionTexto = nuevaOpcionTexto;
    }

    public Integer getSeleccionId() {
        return seleccionId;
    }

    public void setSeleccionId(Integer seleccionId) {
        this.seleccionId = seleccionId;
    }

    public String getPreguntaTexto() {
        return preguntaTexto;
    }

    public void setPreguntaTexto(String preguntaTexto) {
        this.preguntaTexto = preguntaTexto;
    }

    public String getNuevaOpcionTexto() {
        return nuevaOpcionTexto;
    }

    public void setNuevaOpcionTexto(String nuevaOpcionTexto) {
        this.nuevaOpcionTexto = nuevaOpcionTexto;
    }

}
