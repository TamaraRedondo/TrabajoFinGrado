package com.tfg.project.tfg_backend.model;

public class Message {
    private String role;
    private String content;

    // Constructor vacío
    public Message() {
    }

    // Constructor con parámetros
    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    // Getters y Setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
