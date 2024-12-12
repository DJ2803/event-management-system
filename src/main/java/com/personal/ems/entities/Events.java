package com.personal.ems.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Events {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long eventId;
	
	private String name;
	
	private String description;
	
	private String location;
	
	private Long maxAttendees;
	
	private Long currentAttendees;
	
	private LocalDate date;
	
	private LocalTime startTime;
	
	private LocalTime endTime;
	
	private Boolean isAvailable;
	
	@JsonIgnore
	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Registration> registrations;

	public Events(Long eventId, String name, String description, String location, Long maxAttendees,
			Long currentAttendees, LocalDate date, LocalTime startTime, LocalTime endTime, Boolean isAvailable) {
		super();
		this.eventId = eventId;
		this.name = name;
		this.description = description;
		this.location = location;
		this.maxAttendees = maxAttendees;
		this.currentAttendees = currentAttendees;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isAvailable = isAvailable;
	}

	@Override
	public String toString() {
		return "Events [eventId=" + eventId + ", name=" + name + ", description=" + description + ", location="
				+ location + ", maxAttendees=" + maxAttendees + ", currentAttendees=" + currentAttendees + ", date="
				+ date + ", startTime=" + startTime + ", endTime=" + endTime + ", isAvailable=" + isAvailable + "]";
	}

	
	
	

}
