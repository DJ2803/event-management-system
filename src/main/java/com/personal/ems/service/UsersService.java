package com.personal.ems.service;

import com.personal.ems.entities.OneTimePasscode;
import com.personal.ems.entities.Users;
import com.personal.ems.exceptions.InvalidLoginCredentialsException;
import com.personal.ems.exceptions.OtpInvalidException;
import com.personal.ems.exceptions.UserEmailIdAlreadyExistsException;
import com.personal.ems.exceptions.UserNameAlreadyExistsException;
import com.personal.ems.exceptions.UserNotFoundException;

public interface UsersService {
	
	public String signup(Users user) throws UserNameAlreadyExistsException, UserEmailIdAlreadyExistsException;
	
	public Users login(String username, String password) throws InvalidLoginCredentialsException,UserNotFoundException;
	
	public String verifyOtp(Long id, Long otp) throws OtpInvalidException, UserNotFoundException;
	
	public String changePassword(String username, String newPassword) throws UserNotFoundException;
	
	public Users getUserById(Long id) throws UserNotFoundException;
	
	public Users getUserByUsername(String username) throws UserNameAlreadyExistsException ;
	
	public Users getUserByEmailId(String emailId) throws UserEmailIdAlreadyExistsException;
	
	public OneTimePasscode generateAndSaveOtp(Long id) throws UserNotFoundException;
	
	public Long getOtpById(Long id) throws UserNotFoundException;
	
	public void sendOtp(String recipientEmail, Long otp, Long userId);
	

}
