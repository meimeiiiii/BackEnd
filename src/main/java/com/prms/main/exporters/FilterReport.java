package com.prms.main.exporters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.prms.main.models.Filter;
import com.prms.main.models.Patient;

public class FilterReport {
		
		private List<Patient> patients;
		private boolean filterValue;
		private boolean status;
		private boolean gender;
		private boolean birthdate;
		private boolean male;
		private boolean female;
		private boolean others;
		private int statusValue;
		private Date dateStart;
		private Date dateEnd;

		public FilterReport(List<Patient> patients,
				Filter filter){
				this.patients = patients;
				this.filterValue = filter.isFilterValue();
				this.status = filter.isStatus();
				this.gender = filter.isGender();
				this.birthdate = filter.isBirthdate();
				this.male = filter.isMale();
				this.female = filter.isFemale();
				this.others = filter.isOthers();
				this.statusValue = filter.getStatusValue().equals("active") ? 1:0;
				try {
					this.dateStart = new SimpleDateFormat("yyyy-MM-dd").parse(filter.getStartDate());
					this.dateEnd = new SimpleDateFormat("yyyy-MM-dd").parse(filter.getEndDate());
				} catch (ParseException e) {
					this.dateStart = new Date();
					this.dateEnd = new Date();
				}
			}
		
		public List<Patient> filter() {
			if (filterValue == true) {
				if (status == true) {
					filterStatus();
				}
				if (gender == true) {
					filterGender();
				}
				if (birthdate == true) {
					filterBirthdate();
				}
			}
					
			return patients;
		}
		
		private void filterStatus() {
			List<Patient> patientsTemp = new ArrayList<Patient>();
			for(Patient p : patients) {
				if (statusValue == p.getStatus()) {
					patientsTemp.add(p);
				}
			}		
			patients.clear();
			patients = new ArrayList<Patient>(patientsTemp);
		}
		
		private void filterGender() {
			List<Patient> patientsTemp = new ArrayList<Patient>();
			List<String> genderValues = new ArrayList<String>();
			if (male == true) {
				genderValues.add("Male");
			}
			if (female == true) {
				genderValues.add("Female");
			}
			if (others == true) {
				genderValues.add("Others");
			}
			
			for(Patient p : patients) {
				for (String g : genderValues) {
					if(g.equals(p.getGender())) {
						patientsTemp.add(p);
					}
				}
			}
			patients.clear();
			patients = new ArrayList<Patient>(patientsTemp);
		}
		
		private void filterBirthdate() {
			Calendar c = Calendar.getInstance();
			c.setTime(dateStart);
			c.add(Calendar.DATE, -1);
			dateStart = c.getTime();
			
			c.setTime(dateEnd);
			c.add(Calendar.DATE, 1);
			dateEnd = c.getTime();
			
			List<Patient> patientsTemp = new ArrayList<Patient>();
			for(Patient p : patients) {
				if (p.getBirthdate().after(dateStart) && p.getBirthdate().before(dateEnd)) {
					patientsTemp.add(p);
				}
			}
			patients.clear();
			patients = new ArrayList<Patient>(patientsTemp);
		}

	}