package com.personal.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.personal.ems.entities.OneTimePasscode;

@Repository
public interface OtpReposiroty extends JpaRepository<OneTimePasscode, Long> {

}
