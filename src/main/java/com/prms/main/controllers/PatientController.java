package com.prms.main.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prms.main.exporters.ExcelExporter;
import com.prms.main.models.Address;
import com.prms.main.models.Patient;
import com.prms.main.repositories.PatientRepository;
import com.prms.main.services.AddressServices;
import com.prms.main.services.PatientServices;
@RestController
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

   
    @GetMapping("/getAllPatients")
    public List<Patient> all() {
       return pService.listAll();
    }

    // @GetMapping("/address")
    // public List<Address> getAddresses(){
    //     return (List<Address>) aService.listAll();
    // }

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