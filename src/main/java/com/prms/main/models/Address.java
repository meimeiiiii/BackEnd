package com.prms.main.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "address", uniqueConstraints={@UniqueConstraint(columnNames ={"address"})})
public class Address {
	
	@Id
	@Column(name = "a_id")
	private long addressId;
	
	@Column(name = "p_id")
	private long patientId;
	
	@Column(name = "address" , unique=true)
	private String address;


	public long getAddressId() {
		return addressId;
	}

	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}

    public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

    public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public Address(String address, long patientId)
	{
		super();
		this.address = address;
		this.patientId = patientId;
	}
	
	public Address()
	{
		
	}

	
	

}
	