package com.personal.ems.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Users {
	
	public enum Role{
		ORGANIZER,
		ATTENDEE;
			
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false, unique = true)
	@Size(min = 8, message = "Username should contain atleast 8 characters")
	private String username;
	
	@Column(nullable = false)
	@Size(min = 8, max = 64, message = "Password should contain atleast 8 characters")
	private String password;
	
	@Column(nullable = false, unique = true)
	private String emailId;
	
	@Column(nullable = false)
	private Long mobileNumber;
	
	@Column(nullable = false)
	private Role role;
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "users", orphanRemoval = true)
	private List<Registration> registration;

	
	
	@Override
	public String toString() {
		return "Users [id=" + id + ", username=" + username + ", emailId=" + emailId
				+ ", mobileNumber=" + mobileNumber + ", role=" + role + "]";
	}



	public Users(Long id, String username, String password, String emailId, Long mobileNumber, Role role) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.emailId = emailId;
		this.mobileNumber = mobileNumber;
		this.role = role;
	}
	
	

}



