package com.tfg.project.tfg_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.tfg.project.tfg_backend.model.Analisis;
import com.tfg.project.tfg_backend.model.Ejercicio;
import com.tfg.project.tfg_backend.model.PromptResponse;
import com.tfg.project.tfg_backend.model.PromptResponse2;
import com.tfg.project.tfg_backend.model.Rutina;
import com.tfg.project.tfg_backend.repository.AnalisisRepository;
import com.tfg.project.tfg_backend.repository.EjercicioRepository;
import com.tfg.project.tfg_backend.repository.RutinaRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OllamaService {

	private static final String API_URL = "http://localhost:11434/api/generate";
	private static final String MODEL_NAME = "llama3";

	@Autowired
	private RutinaRepository rutinaRepository;
	@Autowired
	private EjercicioRepository ejercicioRepository;
	@Autowired
	private AnalisisRepository analisisRepository;

	@Transactional 
	public ResponseEntity<PromptResponse> obtenerRespuestaIA(PromptResponse promptResponse) {
		try {
			String prompt = promptResponse.getPrompt();
			Integer clienteId = promptResponse.getClientId();
			
			String requestBody = "{\"model\": \"" + MODEL_NAME + "\", \"prompt\": \"" + prompt + "\"}";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

			RestTemplate restTemplate = new RestTemplate();

			ResponseEntity<String> response = restTemplate.exchange(API_URL, 
					HttpMethod.POST, 
					entity,
					String.class); 

			if (response.getStatusCode() == HttpStatus.OK) {
				StringBuilder respuestaConcatenada = new StringBuilder();
				String[] responses = response.getBody().split("\n");

				for (String resp : responses) {
					JSONObject jsonObject = new JSONObject(resp);
					respuestaConcatenada.append(jsonObject.getString("response"));
				}
				
				String jsonResponse = extractJsonResponse(respuestaConcatenada.toString());
				if (jsonResponse == null) {
				    throw new RuntimeException("No valid JSON found in the AI response.");
				}
				System.out.println("JSON extraído: " + jsonResponse);
				
				JSONArray rutinasArray = new JSONArray(jsonResponse);

				for (int j = 0; j < 2; j++) {
				    for (int i = 0; i < rutinasArray.length(); i++) {
				        JSONObject rutinaObjeto = rutinasArray.getJSONObject(i);

				        Rutina rutina = new Rutina();
				        rutina.setClienteId(clienteId);
				        System.out.println("Guardando rutina: " + rutina);
				        rutinaRepository.save(rutina);
				        
				        GuardarEjercicios(rutinaObjeto, rutina, clienteId);
				    }
				}
			} else {
				throw new RuntimeException("Error al obtener la respuesta de la IA. Código de estado: "
						+ response.getStatusCode().value());
			}
		} catch (Exception e) {
			throw new RuntimeException("Error al procesar el prompt: " + e.getMessage());
		}
		return ResponseEntity.ok(promptResponse);
	}
	
	
	private String extractJsonResponse(String response) {
		try {
			int startIndex = response.indexOf("[");
			int endIndex = response.lastIndexOf("]");
			if (startIndex != -1 && endIndex != -1) {
				return response.substring(startIndex, endIndex + 1);
			}
		} catch (Exception e) {
			System.err.println("Error al extraer el JSON de la respuesta: " + e.getMessage());
		}
		return null;
	}
	
	private void GuardarEjercicios(JSONObject rutinaObjeto, Rutina rutina, Integer clienteId) {
	    JSONObject warmupObject = rutinaObjeto.optJSONObject("Warm-up");
	    if (warmupObject != null) {
	        insertEjercicio("Warm-up", warmupObject.toString(), false, rutina, clienteId, 1); 
	    }

	    for (int k = 1; k <= 5; k++) {
	        JSONObject exerciseObject = rutinaObjeto.optJSONObject("Exercise " + k);
	        if (exerciseObject != null) {
	            insertEjercicio("Exercise" + k, exerciseObject.toString(), false, rutina, clienteId, k+1); 
	        }
	    }
	    
	    JSONObject cardioObject = rutinaObjeto.optJSONObject("Cardio");
	    if (cardioObject != null) {
	        insertEjercicio("Cardio", cardioObject.toString(), false, rutina, clienteId, 7); 
	    }
	}

	private void insertEjercicio(String nombre, String descripcion, boolean realizado, Rutina rutina, Integer clienteId, int orden) {
	    Ejercicio ejercicio = new Ejercicio();
	    ejercicio.setNombre(nombre);
	    ejercicio.setDescripcion(descripcion);
	    ejercicio.setRealizado(realizado);
	    ejercicio.setRutina(rutina);
	    ejercicio.setClienteId(clienteId);
	    ejercicio.setOrden(orden); 
	    System.out.println("Guardando ejercicio: " + ejercicio);
	    ejercicioRepository.save(ejercicio);
	}
    
    public List<String> obtenerDescripcionDeAnalisisPorClienteId(Integer clienteId) {
        List<Analisis> analisis = analisisRepository.findByClienteId(clienteId);
        List<String> descripciones = new ArrayList<>();

        for (Analisis a : analisis) {
            descripciones.add(a.getDescripcion());
        }
        return descripciones;
    }
	
	@Transactional
	public ResponseEntity<PromptResponse2> obtenerRespuestaIAAnalisis(PromptResponse2 promptResponse2) {
	    try {
	        String prompt = promptResponse2.getPrompt();
	        Integer clienteId = promptResponse2.getClientId();
	        Long rutinaId = promptResponse2.getRutinaId();

	        String requestBody = "{\"model\": \"" + MODEL_NAME + "\", \"prompt\": \"" + prompt + "\"}";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

	        RestTemplate restTemplate = new RestTemplate();

	        ResponseEntity<String> response = restTemplate.exchange(API_URL,
	                HttpMethod.POST,
	                entity,
	                String.class);

	        if (response.getStatusCode() == HttpStatus.OK) {
	            StringBuilder respuestaConcatenada = new StringBuilder();
	            String[] responses = response.getBody().split("\n");

	            for (String resp : responses) {
	                JSONObject jsonObject = new JSONObject(resp);
	                respuestaConcatenada.append(jsonObject.getString("response"));
	            }

	            String jsonResponse = extractJsonResponse(respuestaConcatenada.toString());
	            if (jsonResponse == null) {
	                throw new RuntimeException("No se encontró JSON válido en la respuesta de la IA.");
	            }

	            System.out.println("JSON extraído: " + jsonResponse);

	            JSONArray analisisArray = new JSONArray(jsonResponse);

	            for (int i = 0; i < analisisArray.length(); i++) {
	                JSONObject analisisObjeto = analisisArray.getJSONObject(i);

	                String habitKey = "habit";
	                if (!analisisObjeto.has(habitKey)) {
	                    habitKey = habitKey.substring(0, 1).toUpperCase() + habitKey.substring(1);
	                }

	                String tipo = analisisObjeto.optString(habitKey);

                    Analisis nuevoAnalisis = new Analisis();
                    nuevoAnalisis.setTipo(tipo);
                    nuevoAnalisis.setDescripcion(analisisObjeto.toString());
                    nuevoAnalisis.setClienteId(clienteId);
                    analisisRepository.save(nuevoAnalisis);
	            }
	        } else {
	            throw new RuntimeException("Error al obtener la respuesta de la IA. Código de estado: "
	                    + response.getStatusCode().value());
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Error al procesar el prompt: " + e.getMessage());
	    }
	    return ResponseEntity.ok(promptResponse2);
	}
	
	
	
	
	@Transactional
	public ResponseEntity<PromptResponse2> obtenerRespuestaActualizada(PromptResponse2 promptResponse2) {
	    try {
	        String prompt = promptResponse2.getPrompt();
	        Integer clienteId = promptResponse2.getClientId();
	        Long rutinaId = promptResponse2.getRutinaId();

	        String requestBody = "{\"model\": \"" + MODEL_NAME + "\", \"prompt\": \"" + prompt + "\"}";

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

	        RestTemplate restTemplate = new RestTemplate();

	        ResponseEntity<String> response = restTemplate.exchange(API_URL,
	                HttpMethod.POST,
	                entity,
	                String.class);

	        if (response.getStatusCode() == HttpStatus.OK) {
	            StringBuilder respuestaConcatenada = new StringBuilder();
	            String[] responses = response.getBody().split("\n");

	            for (String resp : responses) {
	                JSONObject jsonObject = new JSONObject(resp);
	                respuestaConcatenada.append(jsonObject.getString("response"));
	            }

	            String jsonResponse = extractJsonResponse(respuestaConcatenada.toString());
	            if (jsonResponse == null) {
	                throw new RuntimeException("No se encontró JSON válido en la respuesta de la IA.");
	            }

	            System.out.println("JSON extraído: " + jsonResponse);

	            JSONArray analisisArray = new JSONArray(jsonResponse);

	            for (int i = 0; i < analisisArray.length(); i++) {
	                JSONObject analisisObjeto = analisisArray.getJSONObject(i);

	                String habitKey = "habit";
	                if (!analisisObjeto.has(habitKey)) {
	                    habitKey = habitKey.substring(0, 1).toUpperCase() + habitKey.substring(1);
	                }

	                String tipo = analisisObjeto.optString(habitKey);

	                Optional<Analisis> analisisExistente = analisisRepository.findFirstByClienteId(clienteId);
                    Analisis analisis = analisisExistente.get();
                    analisis.setTipo(tipo);
                    analisis.setDescripcion(analisisObjeto.toString());
                    analisisRepository.save(analisis);
	            }
	        } else {
	            throw new RuntimeException("Error al obtener la respuesta de la IA. Código de estado: "
	                    + response.getStatusCode().value());
	        }
	    } catch (Exception e) {
	        throw new RuntimeException("Error al procesar el prompt: " + e.getMessage());
	    }
	    return ResponseEntity.ok(promptResponse2);
	}
	
	
	
}