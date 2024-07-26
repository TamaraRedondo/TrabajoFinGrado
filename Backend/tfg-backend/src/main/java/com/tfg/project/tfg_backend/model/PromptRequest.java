package com.tfg.project.tfg_backend.model;

public class PromptRequest {
    private String enunciado;
    private Integer clienteId;

    // Getters y setters
    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }
}