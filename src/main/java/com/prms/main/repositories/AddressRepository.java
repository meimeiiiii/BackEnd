package com.prms.main.repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prms.main.models.Address;

@Repository("addressRepository")
public interface AddressRepository extends JpaRepository<Address, Long> {

//	Optional<Address> findById(long id);
	
//	@Query(value = "SELECT a.address, p.address FROM address a "
//			+ "JOIN patients p "
//			+ "ON a.p_id = p.p_id",nativeQuery=true)
//	List<Address> findNewAddress();
	
	
	@Query(value ="SELECT * FROM address WHERE p_id = :p_id" ,nativeQuery=true)
	public List<Address> getAllAddressByID(@Param("p_id") long p_id);
	

}