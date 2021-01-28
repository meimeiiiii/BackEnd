package com.prms.main.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prms.main.models.Patient;


@Repository("patientRepository")
public interface PatientRepository extends JpaRepository<Patient, Long> {
	
}