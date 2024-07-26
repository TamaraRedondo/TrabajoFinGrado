package com.tfg.project.tfg_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.project.tfg_backend.model.AuthResponse;
import com.tfg.project.tfg_backend.model.LoginRequest;
import com.tfg.project.tfg_backend.model.RegisterRequest;
import com.tfg.project.tfg_backend.service.AuthService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class AuthController {
	
	private final AuthService authService;
	private final HttpSession session;
	
	@PostMapping(value = "login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
		AuthResponse response = authService.login(request);
		session.setAttribute("usuario", request.getUsername());
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value = "register")
	public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
		AuthResponse response = authService.register(request);
		session.setAttribute("usuario", request.getUsername());
		return ResponseEntity.ok(response);
	}
	
}