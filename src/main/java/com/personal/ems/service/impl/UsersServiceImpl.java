package com.personal.ems.service.impl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.personal.ems.entities.OneTimePasscode;
import com.personal.ems.entities.Users;
import com.personal.ems.exceptions.InvalidLoginCredentialsException;
import com.personal.ems.exceptions.OtpInvalidException;
import com.personal.ems.exceptions.UserEmailIdAlreadyExistsException;
import com.personal.ems.exceptions.UserNameAlreadyExistsException;
import com.personal.ems.exceptions.UserNotFoundException;
import com.personal.ems.repository.OtpReposiroty;
import com.personal.ems.repository.UsersRepository;
import com.personal.ems.service.UsersService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsersServiceImpl implements UsersService {

	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private OtpReposiroty otpRepository;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	@Value("${twilio.account.sid}")
    private String twilioSid;

    @Value("${twilio.auth.token}")
    private String twilioAuthToken;

    @Value("${twilio.phone.number}")
    private String twilioPhoneNumber;
    
    @Override
	public String signup(Users user) throws UserNameAlreadyExistsException, UserEmailIdAlreadyExistsException {
		try {
			if(usersRepository.existsByUsername(user.getUsername())) {
				return "Username already exists";
			}else if(usersRepository.existsByEmailId(user.getEmailId())) {
				return "Email ID already exists";
			}else {
//				String hashPassword = passwordEncoder.encode(user.getPassword());
//				user.setPassword(hashPassword);
				usersRepository.save(user);
				otpRepository.save(new OneTimePasscode(user.getId(),0L, LocalDateTime.now(), user));
				return "User added successfully";
			}
			
		}catch(Exception e) {
			throw new UserNameAlreadyExistsException(e.getMessage());
		}
	}

	@Override
	public Users login(String usernameOrEmail, String password)
			throws InvalidLoginCredentialsException, UserNotFoundException {
		try {
			var user = usersRepository.findByUsername(usernameOrEmail).or(()->usersRepository.findByEmailId(usernameOrEmail));
			if(user.isPresent()) {
				Users temp = user.get();
//				if(passwordEncoder.matches(password, user.get().getPassword()))
				if(temp.getPassword().equals(password)){
					generateAndSaveOtp(user.get().getId());
//			User userObj=user.get();
//			UserData data= new UserData();
//			data.setUserId(userObj.getUserId());
//			data.setEmailId(userObj.getEmailId());
//			data.setFirstName(userObj.getFirstName());
//			data.setLastName(userObj.getLastName());
//			data.setMobileNumber(userObj.getMobileNumber());
//			data.setPassword(userObj.getPassword());
//			data.setTypeOfUser(userObj.getTypeOfUser());
//			data.setZipcode(userObj.getZipcode());
//			data.setUserId(userObj.getUserId());
//			data.setMessage("Enter OTP generated");
//			data.setUserName(userObj.getUserName());
					return temp;
				}else {
					throw new InvalidLoginCredentialsException("Wrong password");
				}
			}else {
				throw new UserNotFoundException("User doesn't exist");
			}
		}catch(InvalidLoginCredentialsException e) {
			throw new InvalidLoginCredentialsException(e.getMessage());
		}catch(UserNotFoundException e) {
			throw new UserNotFoundException(e.getMessage());
		}
	}
	
	@Override
	public String verifyOtp(Long id, Long otp) throws OtpInvalidException, UserNotFoundException {
		try {
			var user = otpRepository.findById(id);
			if(user.isPresent()) {
				var temp = user.get();
				if(temp.getGeneratedTime().isBefore(temp.getGeneratedTime().plusMinutes(5))) {
					if(temp.getOtp()==otp) {
						var role = getUserById(id);
						return role.getRole()+" Login successful";
					}else {
						throw new OtpInvalidException("Invalid OTP");
					}
				}else {
					throw new OtpInvalidException("OTP expired");
				}
			}else {
				throw new UserNotFoundException("User doesn't exist");
			}
		}catch(OtpInvalidException e) {
			throw new OtpInvalidException(e.getMessage());
		}catch(UserNotFoundException e) {
			throw new UserNotFoundException(e.getMessage());
		}
	}

	@Override
	public String changePassword(String username, String newPassword) throws UserNotFoundException {
		try {
			var user = usersRepository.findByUsername(username);
			if(user.isPresent()) {
//				String hashPassword = passwordEncoder.encode(newPassword);
//				user.get().setPassword(hashPassword);
				usersRepository.save(user.get());
//				var role = user.get().getRole();
//				if(role.toLowerCase().equals("customer")) {
//					var customer = customerRepository.findById(user.get().getUserId()).get();
//					customer.setPassword(newPassword);
//					customerRepository.save(customer);
//				}else if(role.toLowerCase().equals("employee")) {
//					var employee = employeeRepository.findById(user.get().getUserId()).get();
//					employee.setPassword(newPassword);
//					employeeRepository.save(employee);
//				}
				return "Password changed successfully";
			}else {
				throw new UserNotFoundException("User doesn't exist");
			}
		}catch(UserNotFoundException e) {
			throw new UserNotFoundException(e.getMessage());
		}
	}

	@Override
	public Users getUserById(Long id) throws UserNotFoundException {
		return usersRepository.findById(id).orElseThrow(()->new UserNotFoundException("User doesn't exist"));
	}

	@Override
	public Users getUserByUsername(String username) throws UserNameAlreadyExistsException {
		return usersRepository.findByUsername(username).orElseThrow(()->new UserNameAlreadyExistsException("Username already exists"));
	}

	@Override
	public OneTimePasscode generateAndSaveOtp(Long id) throws UserNotFoundException {
		try {
			if(getUserById(id)!=null) {
				Random random = new Random();
				Long otp = (long) random.nextInt(999999);
		        LocalDateTime time = LocalDateTime.now();
		        OneTimePasscode temp = otpRepository.findById(id).get();
		        temp.setGeneratedTime(time);
		        temp.setOtp(otp);
	        	otpRepository.save(temp);
	        	sendOtp(getUserById(id).getEmailId(), otp, id);
	        	return temp;
	        }else {
	        	throw new UserNotFoundException("User doesn't exist");
	        }
		}catch(UserNotFoundException e) {
			throw new UserNotFoundException(e.getMessage());
		} 
	}

	@Override
	public Long getOtpById(Long id) throws UserNotFoundException {
		try {
			if(getUserById(id)!=null) {
				Long otp = otpRepository.findById(id).get().getOtp();
				return otp;
			}else {
				throw new UserNotFoundException("User doesn't exist");
			}
		}catch(UserNotFoundException e) {
			throw new UserNotFoundException(e.getMessage());
		}
	}

	@Override
	public Users getUserByEmailId(String emailId) throws UserEmailIdAlreadyExistsException {
		return usersRepository.findByEmailId(emailId).orElseThrow(()->new UserEmailIdAlreadyExistsException("User Email ID already exists"));
	}
	
	@Override
	public void sendOtp(String recipientEmail, Long otp, Long userId) {
        // Create email message
        SimpleMailMessage message = new SimpleMailMessage();
        String otpMessage = "Your OTP is: " + otp + " and your user ID is: " + userId;
        message.setTo(recipientEmail);
        message.setSubject("Your OTP Verification Code");
        message.setText(otpMessage);
        try {
        	Twilio.init(twilioSid, twilioAuthToken);
            Message.creator(
                    new PhoneNumber("+1"+String.valueOf(usersRepository.findById(userId).get().getMobileNumber())),
                    new PhoneNumber(twilioPhoneNumber),
                    otpMessage
            ).create();
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
        javaMailSender.send(message);
    }
	
}
