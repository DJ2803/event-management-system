package com.personal.ems.exceptions;

public class InvalidLoginCredentialsException extends Exception {

	private static final long serialVersionUID = 1L;
	public InvalidLoginCredentialsException(String message) {
		super(message);
	}
	@Override
	public String toString() {
		return "InvalidLoginCredentialsException" + super.getMessage();
	}
	
}
