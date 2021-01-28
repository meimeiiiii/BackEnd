package com.prms.main.services;
import java.util.List; 
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.prms.main.models.Address;
//import com.prms.main.models.AddressRequest;
import com.prms.main.repositories.AddressRepository; 
 
@Service("addressService")
@Transactional
public class AddressServices {
     
    @Autowired
    private AddressRepository repo;

    public AddressServices(AddressRepository repo) {
        this.repo = repo;
    }
     
    public List<Address> listAll() {
        return repo.findAll();
    }
    
//    public String saveNewAddress(AddressRequest request)
//    {
//    	Address address = new Address();
//    	address.setAddress(request.getAddress());
//    	return repo.save(address).getID();
//    }
     
}