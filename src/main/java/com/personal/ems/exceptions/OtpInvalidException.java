package com.personal.ems.exceptions;

public class OtpInvalidException extends Exception {

	private static final long serialVersionUID = 1L;
	public OtpInvalidException(String errorMessage) {
	
		super(errorMessage);
	}
	@Override
	public String toString() {
		return "OtpInvalidException" + super.getMessage();
	}
	
	
}
