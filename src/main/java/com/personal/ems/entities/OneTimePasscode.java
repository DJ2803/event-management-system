package com.personal.ems.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class OneTimePasscode {
	
	@Id
	private Long id;
	
	private Long otp;
	
	private LocalDateTime generatedTime;
	
	@OneToOne
	@MapsId
	private Users users;

	public OneTimePasscode(Long id, Long otp, LocalDateTime generatedTime, Users users) {
		super();
		this.id = id;
		this.otp = otp;
		this.generatedTime = generatedTime;
		this.users = users;
	}


	@Override
	public String toString() {
		return "OneTimePasscode [id=" + id + ", otp=" + otp + ", generatedTime=" + generatedTime + ", users=" + users
				+ "]";
	}

}
