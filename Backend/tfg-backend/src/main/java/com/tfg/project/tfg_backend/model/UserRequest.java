package com.tfg.project.tfg_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

	int id;
	String username;
	String firstname;
	String lastname;
	String email;

}
