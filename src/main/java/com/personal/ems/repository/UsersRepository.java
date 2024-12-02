package com.personal.ems.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.personal.ems.entities.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
	
	Optional<Users> findByUsername(String username);
	
	Optional<Users> findByEmailId(String emailId);
	
	boolean existsByUsername(String username);
	
	boolean existsByEmailId(String emailId);
	

}
