package com.personal.ems.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.personal.ems.entities.OneTimePasscode;
import com.personal.ems.entities.Users;
import com.personal.ems.exceptions.InvalidLoginCredentialsException;
import com.personal.ems.exceptions.OtpInvalidException;
import com.personal.ems.exceptions.UserEmailIdAlreadyExistsException;
import com.personal.ems.exceptions.UserNameAlreadyExistsException;
import com.personal.ems.exceptions.UserNotFoundException;
import com.personal.ems.service.UsersService;

import jakarta.validation.Valid;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/")
public class UsersController {
	
	@Autowired
	private UsersService usersService;

	@PostMapping("signup")
	public ResponseEntity<String> signup(@Valid @RequestBody Users user) throws UserNameAlreadyExistsException, UserEmailIdAlreadyExistsException{
		try {
			String ppt= usersService.signup(user);
			return ResponseEntity.ok(ppt);
		}catch(UserNameAlreadyExistsException e) {
			throw new UserNameAlreadyExistsException(e.getMessage());
		}catch(UserEmailIdAlreadyExistsException e) {
			throw new UserEmailIdAlreadyExistsException(e.getMessage());
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("login/{usernameOrEmail}/{password}")
	public ResponseEntity<Users> login(@PathVariable String usernameOrEmail, @PathVariable String password) throws InvalidLoginCredentialsException, UserNotFoundException{
		try {
			Users response = usersService.login(usernameOrEmail, password);
			return ResponseEntity.ok(response);
		}catch(InvalidLoginCredentialsException e) {
			throw new InvalidLoginCredentialsException(e.getMessage());
		}catch(UserNotFoundException e) {
			throw new UserNotFoundException(e.getMessage());
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
	}
	
	@PostMapping("verifyotp/{id}/{otp}")
	public ResponseEntity<String> verifyOtp(@PathVariable Long id,@PathVariable Long otp) throws OtpInvalidException, UserNotFoundException{
		try {
			String response = usersService.verifyOtp(id, otp);
			return ResponseEntity.ok(response);
		}catch(OtpInvalidException e) {
			throw new OtpInvalidException(e.getMessage());
		}catch(UserNotFoundException e) {
			throw new UserNotFoundException(e.getMessage());
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("changepassword/{username}/{newPassword}")
	public ResponseEntity<String> changePassword(@PathVariable String username,@PathVariable String newPassword) throws UserNotFoundException{
		try {
			String response = usersService.changePassword(username, newPassword);
			return ResponseEntity.ok(response);
		}catch(UserNotFoundException e) {
			throw new UserNotFoundException(e.getMessage());
		}catch(Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PostMapping("resendotp/{id}")
	public ResponseEntity<OneTimePasscode> generateAndSaveOtp(@PathVariable Long id) throws UserNotFoundException{
		try {
			OneTimePasscode otp = usersService.generateAndSaveOtp(id);
			return ResponseEntity.ok(otp);
		}catch(UserNotFoundException e) {
			throw new UserNotFoundException(e.getMessage());
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping("getuser/{id}")
	public ResponseEntity<Users> getUserById(@PathVariable Long id) throws UserNotFoundException{
		try {
			Users user = usersService.getUserById(id);
			return ResponseEntity.ok(user);
		}catch(UserNotFoundException e) {
			throw new UserNotFoundException(e.getMessage());
		}catch(Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
}
