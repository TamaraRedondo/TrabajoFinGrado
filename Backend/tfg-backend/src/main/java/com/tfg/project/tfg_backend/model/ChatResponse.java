package com.tfg.project.tfg_backend.model;

public class ChatResponse {
    private String model;
    private String role;
    private String content;

    public ChatResponse(String model, String role, String content) {
        this.setModel(model);
        this.setRole(role);
        this.setContent(content);
    }

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

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
