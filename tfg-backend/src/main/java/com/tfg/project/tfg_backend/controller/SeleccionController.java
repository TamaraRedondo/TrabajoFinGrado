package com.tfg.project.tfg_backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.project.tfg_backend.dto.SeleccionDTO;
import com.tfg.project.tfg_backend.model.SeleccionRequest;
import com.tfg.project.tfg_backend.service.SeleccionService;

import jakarta.persistence.EntityNotFoundException;

@CrossOrigin(origins= {"http://localhost:4200"})
@RestController
@RequestMapping("/api/seleccion")
public class SeleccionController {

	@Autowired
    private SeleccionService seleccionService;
	
	@PostMapping("/guardar-seleccion")
	public ResponseEntity<Map<String, String>> guardarSeleccion(@RequestBody SeleccionDTO seleccionDTO) {
	    try {
	        seleccionService.guardarSeleccion(seleccionDTO);
	        Map<String, String> response = new HashMap<>();
	        response.put("message", "Seleccion guardada correctamente.");
	        return ResponseEntity.ok(response);
	    } catch (IllegalArgumentException e) {
	        return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), HttpStatus.BAD_REQUEST);
	    } catch (Exception e) {
	        return new ResponseEntity<>(Collections.singletonMap("error", "Error interno al procesar la solicitud"), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@GetMapping("/listar-selecciones")
	public ResponseEntity<List<SeleccionDTO>> obtenerSeleccionesDTO() {
	    List<SeleccionDTO> seleccionesDTO = seleccionService.obtenerSeleccionesDTO();
	    if (seleccionesDTO.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(seleccionesDTO);
	}
	
	@GetMapping("/listar-selecciones/{clienteId}")
	public ResponseEntity<List<SeleccionDTO>> obtenerSeleccionesDTO(@PathVariable Integer clienteId) {
	    List<SeleccionDTO> seleccionesDTO = seleccionService.obtenerSeleccionesID(clienteId);
	    if (seleccionesDTO.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }
	    return ResponseEntity.ok(seleccionesDTO);
	}
	
	@PutMapping("/updateSeleccion")
	public ResponseEntity<?> updateSeleccion(@RequestBody SeleccionRequest cambioSeleccionRequest) {
	    try {
	        seleccionService.actualizarSeleccion(cambioSeleccionRequest);
	        return ResponseEntity.ok(Collections.singletonMap("message", "Selección actualizada correctamente."));
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Collections.singletonMap("error", "Error interno al procesar la solicitud"));
	    }
	}
	
	@GetMapping("/opcion-seleccionada/{userId}/{preguntaId}")
    public ResponseEntity<String> obtenerOpcionSeleccionada(@PathVariable Integer userId, @PathVariable Integer preguntaId) {
        try {
            String opcionSeleccionada = seleccionService.obtenerTextoOpcionSeleccionada(userId, preguntaId);
            return ResponseEntity.ok(opcionSeleccionada);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al procesar la solicitud");
        }
    }
	
	
    @GetMapping("/listar-seleccion/{clienteId}/{preguntaId}")
    public ResponseEntity<Integer> obtenerSeleccionId(
            @PathVariable Integer clienteId,
            @PathVariable Integer preguntaId) {
        Integer seleccionId = seleccionService.obtenerSeleccionId(clienteId, preguntaId);
        if (seleccionId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(seleccionId);
    }
	
}