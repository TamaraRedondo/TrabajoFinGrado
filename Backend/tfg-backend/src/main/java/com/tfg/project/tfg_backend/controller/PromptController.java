package com.tfg.project.tfg_backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.project.tfg_backend.dto.SeleccionDTO;
import com.tfg.project.tfg_backend.model.PromptRequest;
import com.tfg.project.tfg_backend.model.PromptResponse;
import com.tfg.project.tfg_backend.model.PromptResponse2;
import com.tfg.project.tfg_backend.service.OllamaService;
import com.tfg.project.tfg_backend.service.SeleccionService;

@RestController
@CrossOrigin(origins = { "http://localhost:4200" })
public class PromptController {

	@Autowired
	private SeleccionService seleccionService;

	@Autowired
	private OllamaService ollamaService;

	/*
	 * @PostMapping("/generate-prompt") public ResponseEntity<PromptResponse>
	 * generatePrompt(@RequestBody PromptRequest promptRequest) { Integer clienteId
	 * = promptRequest.getClienteId(); int diasPorSemana = 0;
	 * 
	 * StringBuilder promptBuilder = new StringBuilder();
	 * promptBuilder.append(promptRequest.getEnunciado()).append(": ");
	 * 
	 * List<SeleccionDTO> seleccionesDTO =
	 * seleccionService.obtenerSeleccionesID(clienteId);
	 * 
	 * Map<String, String> preguntasTextoMap = new HashMap<>();
	 * preguntasTextoMap.put("What is your level of physical activity?",
	 * "Level of physical activity");
	 * preguntasTextoMap.put("Do you have any injuries?", "Injury");
	 * preguntasTextoMap.put("Where is your injury located?", "Injury location");
	 * preguntasTextoMap.put("Where do you usually exercise?", "Training facility");
	 * preguntasTextoMap.put("How long can your workout last?",
	 * "Training duration");
	 * 
	 * Pregunta pregunta =
	 * preguntaRepository.findByTexto("How many days per week can you train?");
	 * Integer preguntaId = pregunta.getId();
	 * 
	 * Map<Integer, String> opcionesTextoMap = new HashMap<>();
	 * opcionesTextoMap.put(46, "3 days per week"); opcionesTextoMap.put(47,
	 * "4 days per week"); opcionesTextoMap.put(48, "5 days per week");
	 * opcionesTextoMap.put(49, "6 days per week"); opcionesTextoMap.put(50,
	 * "Every day");
	 * 
	 * Map<Integer, Integer> opcionesRutinasMap = new HashMap<>();
	 * opcionesRutinasMap.put(46, 3); opcionesRutinasMap.put(47, 4);
	 * opcionesRutinasMap.put(48, 5); opcionesRutinasMap.put(49, 6);
	 * opcionesRutinasMap.put(50, 7);
	 * 
	 * for (SeleccionDTO seleccion : seleccionesDTO) { String preguntaTexto =
	 * seleccion.getPreguntaTexto(); if
	 * (preguntasTextoMap.containsKey(preguntaTexto)) {
	 * promptBuilder.append(preguntasTextoMap.get(preguntaTexto)).append(": "); if
	 * (seleccion.getRespuestaDesarrollo() == null) {
	 * promptBuilder.append(seleccion.getOpcionSeleccionada()).append(", "); } else
	 * { promptBuilder.append(seleccion.getRespuestaDesarrollo()).append(", "); } }
	 * 
	 * if (preguntaTexto.equals("How many days per week can you train?")) {
	 * diasPorSemana = opcionesRutinasMap.getOrDefault(seleccionRepository.
	 * findOpcionIdByClienteIdAndPreguntaId(clienteId, preguntaId ), 0); } }
	 * 
	 * if (promptBuilder.length() > 0) {
	 * promptBuilder.setLength(promptBuilder.length() - 2); }
	 * 
	 * promptBuilder.
	 * append(". The JSON object must have the following structure: The root object is named 'routine'. This root object contains an array called 'Day', where each element represents a training day."
	 * ) .append(" Each 'Day' object must include the following properties:")
	 * .append(" - 'Warm-up': An object detailing the warm-up exercise.")
	 * .append(" - 'Exercise 1' to 'Exercise 5': Make sure to include all five exercises in each training day, each containing the properties: 'Name', 'Sets', 'Repetitions', 'Technical explanation', and 'Difficulty level', in numbers from 1 to 7."
	 * ) .append(" - 'Cardio': An object detailing the cardio exercise.");
	 * 
	 * 
	 * int rutinasMensuales = diasPorSemana * 4/2;
	 * 
	 * promptBuilder.append(" You need to create all ") .append(rutinasMensuales)
	 * .append(" 'Day' objects. I want the specified days complete, don't indicate it to me."
	 * );
	 * 
	 * 
	 * String prompt = promptBuilder.toString();
	 * System.out.println("Prompt generado: "); System.out.println(prompt);
	 * 
	 * 
	 * PromptResponse promptResponse = new PromptResponse();
	 * promptResponse.setPrompt(prompt); promptResponse.setClientId(clienteId);
	 * 
	 * return ollamaService.obtenerRespuestaIA(promptResponse); }
	 */

	@PostMapping("/generate-prompt")
	public ResponseEntity<PromptResponse> generatePrompt(@RequestBody PromptRequest promptRequest) {
		Integer clienteId = promptRequest.getClienteId();

		StringBuilder promptBuilder = new StringBuilder();
		promptBuilder.append(promptRequest.getEnunciado()).append(":");

		List<SeleccionDTO> seleccionesDTO = seleccionService.obtenerSeleccionesID(clienteId);
		for (SeleccionDTO seleccion : seleccionesDTO) {
			String preguntaTexto = seleccion.getPreguntaTexto();
			if (!preguntaTexto.equalsIgnoreCase("Enter your height (cm)")
					&& !preguntaTexto.equalsIgnoreCase("Enter your weight (kg)")
					&& !preguntaTexto.equalsIgnoreCase("What is your age range?")
					&& !preguntaTexto.equalsIgnoreCase("What is your gender?")
					&& !preguntaTexto.equalsIgnoreCase("What is your goal?")
					&& !preguntaTexto.equalsIgnoreCase("Select your additional goal")
					&& !preguntaTexto.equalsIgnoreCase("How much do you walk per day?")
					&& !preguntaTexto.equalsIgnoreCase("How much do you sleep per day?")
					&& !preguntaTexto.equalsIgnoreCase("How much water do you consume per day?")) {
				promptBuilder.append(preguntaTexto).append(":");
				if (seleccion.getRespuestaDesarrollo() == null) {
					promptBuilder.append(seleccion.getOpcionSeleccionada()).append(", ");
				} else {
					promptBuilder.append(seleccion.getRespuestaDesarrollo()).append(", ");
				}
			}
		}

		promptBuilder.append(
				"The JSON has a structure where the root object is called 'routine'. This object contains an array named 'Day', which in turn contains objects representing each training day. Each training day includes a warm-up (Warm-up), up to five exercises (Exercise 1 to Exercise 5), and a cardio session (Cardio).");
		promptBuilder.append(
				"Additionally, each exercise within a training day includes the following properties: Name, Sets, Repetitions, Technical explanation, and Difficulty level.");
		promptBuilder.append(
				"You need to create routines for as many days as indicated in the previous question (How many days per week can you train?).");
		  
		String prompt = promptBuilder.toString();

		System.out.println("Prompt generado:");
		System.out.println(prompt);

		PromptResponse promptResponse = new PromptResponse();
		promptResponse.setPrompt(prompt);
		promptResponse.setClientId(clienteId);

		return ollamaService.obtenerRespuestaIA(promptResponse);
	}

	/*
	 * @PostMapping("/generate-prompt2") public ResponseEntity<PromptResponse2>
	 * generatePrompt2(@RequestBody PromptRequest promptRequest) { Integer clienteId
	 * = promptRequest.getClienteId();
	 * 
	 * StringBuilder promptBuilder = new StringBuilder();
	 * promptBuilder.append(promptRequest.getEnunciado()).append(":");
	 * 
	 * List<SeleccionDTO> seleccionesDTO =
	 * seleccionService.obtenerSeleccionesID(clienteId); for (SeleccionDTO seleccion
	 * : seleccionesDTO) { String preguntaTexto = seleccion.getPreguntaTexto(); if
	 * (!preguntaTexto.equalsIgnoreCase("What is your level of physical activity?")
	 * && !preguntaTexto.equalsIgnoreCase("Do you have any injuries?") &&
	 * !preguntaTexto.equalsIgnoreCase("Where is your injury located?") &&
	 * !preguntaTexto.equalsIgnoreCase("Where do you usually exercise?") &&
	 * !preguntaTexto.equalsIgnoreCase("How many days per week can you train?") &&
	 * !preguntaTexto.equalsIgnoreCase("How long can your workout last?")) {
	 * promptBuilder.append(preguntaTexto).append(":"); if
	 * (seleccion.getRespuestaDesarrollo() == null) {
	 * promptBuilder.append(seleccion.getOpcionSeleccionada()).append(", "); } else
	 * { promptBuilder.append(seleccion.getRespuestaDesarrollo()).append(", "); } }
	 * }
	 * 
	 * promptBuilder.
	 * append("The JSON has a structure where the root object is named 'extra'. Within this object, there's an array named 'Analitic', which contains objects representing each analytical aspect. Each analytic aspect includes details such as the habit (Body Type, Goals, Walking, Sleeping, Water Consumption, BMI Calculation), descriptions, opinions, and potential improvements."
	 * ); promptBuilder.
	 * append("The JSON must include the detailed calculation of the BMI, along with a detailed opinion about each habit."
	 * ); String prompt = promptBuilder.toString();
	 * 
	 * System.out.println("Prompt generado:"); System.out.println(prompt);
	 * 
	 * PromptResponse2 promptResponse2 = new PromptResponse2();
	 * promptResponse2.setPrompt(prompt); promptResponse2.setClientId(clienteId);
	 * 
	 * return ollamaService.obtenerRespuestaIAAnalisis(promptResponse2); }
	 */

	@PostMapping("/generate-prompt2")
	public ResponseEntity<PromptResponse2> generatePrompt2(@RequestBody PromptRequest promptRequest) {
		Integer clienteId = promptRequest.getClienteId();

		StringBuilder promptBuilder = new StringBuilder();
		promptBuilder.append(promptRequest.getEnunciado()).append(": ");

		List<SeleccionDTO> seleccionesDTO = seleccionService.obtenerSeleccionesID(clienteId);

		Map<String, String> preguntasTextoMap = new HashMap<>();
		preguntasTextoMap.put("Enter your height (cm)", "Height");
		preguntasTextoMap.put("Enter your weight (kg)", "Weight");
		preguntasTextoMap.put("What is your age range?", "Age range");
		preguntasTextoMap.put("What is your gender?", "Gender");
		preguntasTextoMap.put("What is your goal?", "Goal");
		preguntasTextoMap.put("Select your additional goal", "Additional goal");
		preguntasTextoMap.put("How much do you walk per day?", "Daily walking frequency");
		preguntasTextoMap.put("How much do you walk per day?", "Daily sleep duration");
		preguntasTextoMap.put("How much water do you consume per day?", "Water consumption per day");

		for (SeleccionDTO seleccion : seleccionesDTO) {
			String preguntaTexto = seleccion.getPreguntaTexto();
			if (preguntasTextoMap.containsKey(preguntaTexto)) {
				promptBuilder.append(preguntasTextoMap.get(preguntaTexto)).append(": ");
				if (seleccion.getRespuestaDesarrollo() == null) {
					promptBuilder.append(seleccion.getOpcionSeleccionada()).append(", ");
				} else {
					promptBuilder.append(seleccion.getRespuestaDesarrollo()).append(", ");
				}
			}

		}

		if (promptBuilder.length() > 0) {
			promptBuilder.setLength(promptBuilder.length() - 2);
		}

		promptBuilder.append(
				". The JSON object must have the following structure: The root object is named 'extra'. This root object contains an array called 'Analitic', which contains objects representing each analytical aspect.")
				.append(" Each analytic aspect includes details such as the habit (Body Type, Goals, Walking, Sleeping, Water Consumption, BMI Calculation), descriptions, opinions, and potential improvements.")
				.append(" The JSON should include the detailed calculation of the BMI, along with a detailed opinion about each habit.");

		String prompt = promptBuilder.toString();
		System.out.println("Prompt generado:");
		System.out.println(prompt);

		PromptResponse2 promptResponse2 = new PromptResponse2();
		promptResponse2.setPrompt(prompt);
		promptResponse2.setClientId(clienteId);

		return ollamaService.obtenerRespuestaIAAnalisis(promptResponse2);
	}

	@GetMapping("/analisis/{clienteId}")
	public ResponseEntity<List<String>> obtenerDescripcionDeAnalisisPorClienteId(@PathVariable Integer clienteId) {
		List<String> descripciones = ollamaService.obtenerDescripcionDeAnalisisPorClienteId(clienteId);

		if (!descripciones.isEmpty()) {
			return ResponseEntity.ok(descripciones);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/generate-prompt3")
	public ResponseEntity<PromptResponse2> generatePrompt3(@RequestBody PromptRequest promptRequest) {
		Integer clienteId = promptRequest.getClienteId();

		StringBuilder promptBuilder = new StringBuilder();
		promptBuilder.append(promptRequest.getEnunciado()).append(": ");

		List<SeleccionDTO> seleccionesDTO = seleccionService.obtenerSeleccionesID(clienteId);

		Map<String, String> preguntasTextoMap = new HashMap<>();
		preguntasTextoMap.put("Enter your height (cm)", "Height");
		preguntasTextoMap.put("Enter your weight (kg)", "Weight");
		preguntasTextoMap.put("What is your age range?", "Age range");
		preguntasTextoMap.put("What is your gender?", "Gender");
		preguntasTextoMap.put("What is your goal?", "Goal");
		preguntasTextoMap.put("Select your additional goal", "Additional goal");
		preguntasTextoMap.put("How much do you walk per day?", "Daily walking frequency");
		preguntasTextoMap.put("How much do you walk per day?", "Daily sleep duration");
		preguntasTextoMap.put("How much water do you consume per day?", "Water consumption per day");

		for (SeleccionDTO seleccion : seleccionesDTO) {
			String preguntaTexto = seleccion.getPreguntaTexto();
			if (preguntasTextoMap.containsKey(preguntaTexto)) {
				promptBuilder.append(preguntasTextoMap.get(preguntaTexto)).append(": ");
				if (seleccion.getRespuestaDesarrollo() == null) {
					promptBuilder.append(seleccion.getOpcionSeleccionada()).append(", ");
				} else {
					promptBuilder.append(seleccion.getRespuestaDesarrollo()).append(", ");
				}
			}

		}

		if (promptBuilder.length() > 0) {
			promptBuilder.setLength(promptBuilder.length() - 2);
		}

		promptBuilder.append(
				". The JSON object must have the following structure: The root object is named 'extra'. This root object contains an array called 'Analitic', which contains objects representing each analytical aspect.")
				.append(" Each analytic aspect includes details such as the habit (Body Type, Goals, Walking, Sleeping, Water Consumption, BMI Calculation), descriptions, opinions, and potential improvements.")
				.append(" The JSON should include the detailed calculation of the BMI, along with a detailed opinion about each habit.");

		String prompt = promptBuilder.toString();
		System.out.println("Prompt generado:");
		System.out.println(prompt);

		PromptResponse2 promptResponse2 = new PromptResponse2();
		promptResponse2.setPrompt(prompt);
		promptResponse2.setClientId(clienteId);

		return ollamaService.obtenerRespuestaActualizada(promptResponse2);
	}
	
}