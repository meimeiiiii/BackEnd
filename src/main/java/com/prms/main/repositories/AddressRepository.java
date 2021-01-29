package com.prms.main.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.prms.main.models.Address;

@Repository("addressRepository")
public interface AddressRepository extends JpaRepository<Address, Long> {

	List<Address> findById(long id);
	
	@Query(value = "Select address from address where p_id = p_id",nativeQuery=true)
	List<Address> findNewAddress();

//	Address save(String address);

//	Address save(String address);

//	Address save(String address);
}