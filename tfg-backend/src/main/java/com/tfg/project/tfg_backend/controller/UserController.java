package com.tfg.project.tfg_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.project.tfg_backend.dto.UserDTO;
import com.tfg.project.tfg_backend.model.UserRequest;
import com.tfg.project.tfg_backend.service.UserService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(value = "api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class UserController {

	private final UserService userService;
	
	@GetMapping("/user-info")
    public ResponseEntity<UserDTO> getUserInfo() {
        UserDTO userDTO = userService.getCurrentUser();
        return ResponseEntity.ok(userDTO);
    }
	
	@PutMapping("/updateUser")
	public ResponseEntity<UserDTO> updateUser(@RequestBody UserRequest userRequest){
	    UserDTO updatedUserDTO = userService.updateUser(userRequest);
	    return ResponseEntity.ok(updatedUserDTO);
	}
	
	@GetMapping("/user-id")
    public ResponseEntity<Integer> getUserId() {
        Integer userId = userService.getUserId(); 
        return ResponseEntity.ok(userId);
    }

	
	
}
