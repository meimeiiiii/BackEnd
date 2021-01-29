package com.prms.main.services;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.prms.main.models.Patient;
import com.prms.main.repositories.PatientRepository; 

@Transactional
@Service("patientService")
public class PatientServices {
     
    @Autowired
    private PatientRepository repo;

    public PatientServices(PatientRepository repo) {
        this.repo = repo;
    }
     
    public List<Patient> listAll() {
        return repo.findAll();
    }


    public List<Patient> getActivated() {
        List<Patient> patients = (List<Patient>) repo.findAllActivated();
        return patients;
    }
    
    public List<Patient> getDeactivated() {
        List<Patient> patients = (List<Patient>) repo.findAllDeactivated();
        return patients;
    }

     
}
