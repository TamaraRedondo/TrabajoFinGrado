package com.tfg.project.tfg_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.project.tfg_backend.model.ChatRequest;
import com.tfg.project.tfg_backend.model.ChatResponse;
import com.tfg.project.tfg_backend.service.ChatService;

@RestController
public class ChatController {

	 private final ChatService chatService;

	    public ChatController(ChatService chatService) {
	        this.chatService = chatService;
	    }
	    
	    @PostMapping("/api/chat")
	    public ResponseEntity<ChatResponse> initiateChat(@RequestBody ChatRequest chatRequest) {
	        String responseFromIA = chatService.startChat(chatRequest);
	        String formattedContent = formatIAContent(responseFromIA);

	        ChatResponse chatResponse = new ChatResponse("llama3", "assistant", formattedContent);

	        return ResponseEntity.ok(chatResponse);
	    }

	    private String formatIAContent(String jsonResponse) {
	        StringBuilder concatenatedContent = new StringBuilder();
	        try {
	            String[] responses = jsonResponse.split("\n");

	            for (String resp : responses) {
	                ObjectMapper objectMapper = new ObjectMapper();
	                JsonNode root = objectMapper.readTree(resp);

	                JsonNode messageNode = root.get("message");
	                if (messageNode != null && messageNode.has("content")) {
	                    concatenatedContent.append(messageNode.get("content").asText()).append(" ");
	                }
	            }
	        } catch (Exception e) {
	            e.printStackTrace(); 
	            return "";
	        }
	        return concatenatedContent.toString().trim();
	    }
	}
	
