package com.prms.main.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.prms.main.exporters.ExcelExporter;
import com.prms.main.exporters.FilterReport;
import com.prms.main.models.Address;
import com.prms.main.models.Filter;
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
    AddressRepository addressRepository;
    

    @Autowired
    public PatientController(PatientServices pService, AddressServices aService) {
        this.pService = pService;
        this.aService = aService;
    }
    
    
    @GetMapping("/getAllPatients")
    public List<Patient> getPatients(){
        return (List<Patient>) pService.listAll();
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
    
    
   
    
    @GetMapping("/getAllAddress")
    public List<Address> getAddresses(){
        return (List<Address>) aService.listAll();
    }
    
    
  
    @GetMapping("/getAddress/{p_id}")
    public ResponseEntity<Address> getAddressById(@PathVariable("p_id") long p_id)
    {
    	Optional <Address> addressData = addressRepository.findById(p_id);
    	if(addressData.isPresent())
    	{
    		return new ResponseEntity<>(addressData.get(), HttpStatus.OK);
    	}
    	else {
    		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    	}
  		
   }
    
    @GetMapping("/getAllAddressByID")
    public List<Address> getAllAddressByID(@RequestParam("id") long p_id)
    {
    	List<Address> addressList = addressRepository.getAllAddressByID(p_id);
    	return addressList;
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
    


   
    @GetMapping("/activated")
    public List<Patient> activatedPatients() {
       return pService.getActivated();
    }
    @GetMapping("/deactivated")
    public List<Patient> deactivatedPatients() {
       return pService.getDeactivated();
    }
    
    
//    @GetMapping("/addressList")
//    public List<Address> findNewAddress()
//    {
//    	return aService.getNewAddress();
//    }
//   
    
    
     
     @PostMapping("/addAddress")
     public ResponseEntity<Address> addAddress(@RequestBody Address address)
     {
    	 try {
    		 Address _address = addressRepository
    				 .save(new Address(address.getAddress(), address.getPatientId()));
    		 return new ResponseEntity<>(_address, HttpStatus.CREATED);
    	 }
    	 
    	 catch (Exception e)
    	 {
    		 return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    	 }

     }
     

     private List<Patient> filter(Filter filter){
    	 List<Patient> listPatient = pService.listAll();
    	 return new FilterReport(listPatient,filter).filter();
     }
     
     
     @PostMapping("/export")
     public ResponseEntity<ByteArrayResource> filterAndExport(@RequestBody Filter filter) {
 		try {
 			Filter _filter = new Filter(filter.isFilterValue(), filter.isStatus(), filter.isGender(), filter.isBirthdate(),
 					filter.isMale(),filter.isFemale(), filter.isOthers(), filter.getStatusValue(), filter.getStartDate(), filter.getEndDate());
 			
 			List<Patient> listPatient = filter(_filter);
 	    	List<Address> listAddress = aService.listAll();
 	    	
 	    	ExcelExporter excelExproter = new ExcelExporter(listPatient,listAddress);
	         
	        ByteArrayOutputStream baos = excelExproter.export();
 	         
 	        DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
 	        String filename = "PatientRecords_" + df.format(new Date());
 	        
 	        HttpHeaders headers = new HttpHeaders();
 	        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
 	        headers.add("Pragma", "no-cache");
 	        headers.add("Expires", "0");
 	        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; " + filename + ".xlsx");

 	       return ResponseEntity.ok()
 	                .headers(headers)
 	                .contentLength(baos.size())
 	                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
 	                .body(new ByteArrayResource(baos.toByteArray()));
 		} catch (Exception e) {
 	 		return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
 	 	}
 	 }   
     
    @PutMapping("/update/{id}")
	public ResponseEntity<Patient> updatePatient(@PathVariable("id") long id, @RequestBody Patient patient) {
		Optional<Patient> patientData = patientRepository.findById(id);

		if (patientData.isPresent()) {
			Patient _patient = patientData.get();
			_patient.setStatus(patient.getStatus());
			return new ResponseEntity<>(patientRepository.save(_patient), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
    
}