package com.tfg.project.tfg_backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tfg.project.tfg_backend.model.ChatRequest;


@Service
public class ChatService {
	
	@Value("${ia.server.url}")
    private String apiUrl;

    @Value("${ai.model.name}")
    private String modelName;
    
    private RestTemplate restTemplate;

    public ChatService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String startChat(ChatRequest chatRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ChatRequest> requestEntity = new HttpEntity<>(chatRequest, headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, String.class);

        return responseEntity.getBody(); 
    }
}

