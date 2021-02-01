package com.prms.main.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.prms.main.models.Patient;



@Repository("patientRepository")
public interface PatientRepository extends JpaRepository<Patient, Long> {

    
    @Query(value="Select * from Patients where status = 1",nativeQuery=true)
    List<Patient> findAllActivated(); 
    
    @Query(value="Select * from Patients where status = 0",nativeQuery=true)
    List<Patient> findAllDeactivated(); 
    
    @Query(value="Select * from Patients where status = 1 && email = ?",nativeQuery=true)
    List<Patient> findEmailDuplicate(String email); 
    
    @Query(value="Select * from Patients where status = 1 && contact_Number = ?",nativeQuery=true)
    List<Patient> findContactNumDuplicate(String contactNumber); 
}