package com.tfg.project.tfg_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tfg.project.tfg_backend.dto.UserDTO;
import com.tfg.project.tfg_backend.jwt.JwtService;
import com.tfg.project.tfg_backend.model.UserRequest;
import com.tfg.project.tfg_backend.repository.UserRepository;
import com.tfg.project.tfg_backend.model.User;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private HttpServletRequest request;

	@Transactional
	public UserDTO updateUser(UserRequest userRequest) {

		String token = request.getHeader("Authorization").replace("Bearer ", "");
		String username = jwtService.getUsernameFromToken(token);
		User user = userRepository.findByUsername(username);

		if (user == null) {
			throw new EntityNotFoundException("Usuario no encontrado");
		}
	
		user.setFirstname(userRequest.getFirstname());
		user.setLastname(userRequest.getLastname());
		user.setEmail(userRequest.getEmail());
		userRepository.save(user);
		return new UserDTO(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail());
	}

	public UserDTO getCurrentUser() {
		String token = request.getHeader("Authorization").replace("Bearer ", "");
		String username = jwtService.getUsernameFromToken(token);
		User user = userRepository.findByUsername(username);
		
		return new UserDTO(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail());
	}
	
	public Integer getUserId() {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String username = jwtService.getUsernameFromToken(token);
        
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user.getId();
        } else {
           
            throw new RuntimeException("Cliente no encontrado");
        }
    }

}