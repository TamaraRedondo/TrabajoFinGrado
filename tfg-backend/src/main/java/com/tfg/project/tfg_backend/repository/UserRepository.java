package com.tfg.project.tfg_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tfg.project.tfg_backend.model.User;

public interface UserRepository extends JpaRepository<User,Integer>{
	User findByUsername(String username);

	@Modifying()
	@Query("UPDATE User u SET u.firstname=:firstname, u.lastname=:lastname, u.email=:email where u.id =:id ")
	void updateUser(@Param(value = "id") Integer id, @Param(value = "firstname") String firstname,
			@Param(value = "lastname") String lastname, @Param(value = "email") String email);
}
