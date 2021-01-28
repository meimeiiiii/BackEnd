package com.prms.main.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.prms.main.exporters.ExcelExporter;
import com.prms.main.models.Address;
import com.prms.main.models.Patient;
import com.prms.main.repositories.AddressRepository;
import com.prms.main.repositories.PatientRepository;
import com.prms.main.services.AddressServices;
import com.prms.main.services.PatientServices;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/patients")
public class PatientController {
	
    private PatientServices pService;
    private AddressServices aService;
    
    @Autowired
	PatientRepository patientRepository;
    
    @Autowired
    public PatientController(PatientServices pService, AddressServices aService) {
        this.pService = pService;
        this.aService = aService;
    }
    

    @GetMapping("/getPatient/{p_id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable("p_id") long p_id){
    	Optional<Patient> patientData = patientRepository.findById(p_id);
		if (patientData.isPresent()) {
			return new ResponseEntity<>(patientData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
    }
    
    @PostMapping("/addPatients")
	public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {
		try {
			Patient _patient = patientRepository
					.save(new Patient(patient.getFirstName(), patient.getMiddleName(), patient.getLastName(),
							patient.getEmail(),patient.getContactNumber(),patient.getBirthdate(),patient.getGender(),patient.getStatus(),patient.getAddress()));
			return new ResponseEntity<>(_patient, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
    

    
    @Autowired
    AddressRepository AddressRepository;
    PatientRepository PatientRepository;
   
    @GetMapping()
    public List<Patient> all() {
       return pService.listAll();
    }
    

    @GetMapping("/activated")
    public List<Patient> activatedPatients() {
       return pService.getActivated();
    }
    @GetMapping("/deactivated")
    public List<Patient> deactivatedPatients() {
       return pService.getDeactivated();
    }
    
    


    //medyo redundant pero xge,,
     @GetMapping("/getAllAddress")
     public List<Address> getAddresses(){
         return (List<Address>) aService.listAll();
     }
     
     
     @PostMapping("/addAddress")
     public ResponseEntity<Address> addAddress(@RequestBody Address address)
     {
    	 try {
    		 Address _address = AddressRepository
    				 .save(new Address(address.getAddress(), address.getPatientId()));
    		 return new ResponseEntity<>(_address, HttpStatus.CREATED);
    	 }
    	 
    	 catch (Exception e)
    	 {
    		 return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    	 }

     }
     
     
//     @PostMapping("/addPatients")
//     public ResponseEntity<Patient> addPatient(@RequestBody Patient patient) {​​
//     try {​​
//     Patient _patient = patientRepository
//     .save(new Patient(patient.getFirstName(), patient.getMiddleName(), patient.getLastName(),
//     patient.getEmail(),patient.getContactNumber(),patient.getBirthdate(),patient.getGender(),patient.getStatus()));
//     return new ResponseEntity<>(_patient, HttpStatus.CREATED);
//     }​​ catch (Exception e) {​​
//     return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//     }​​
//     }
     
//     @PostMapping("/addPatients")
//     public ResponseEntity<Patient> addPatient(@RequestBody Patient patient)​​
//     {
//    	 Patient _patient = PatientRepository;
//     }
//     
   

    @GetMapping("/export")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/octet-stream");

            DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            String filename = "PatientRecords_" + df.format(LocalDateTime.now());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=" + filename + ".xlsx";
            response.setHeader(headerKey, headerValue);

            List<Patient> listPatient = pService.listAll();
            List<Address> listAddress = aService.listAll();

            ExcelExporter excelExporter = new ExcelExporter(listPatient, listAddress);

            excelExporter.export(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}