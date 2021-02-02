package com.example.securingweb.dao;

import com.example.securingweb.model.Address;
import com.example.securingweb.model.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface AddressRepository  extends CrudRepository<Address, String> {

    Address findAddressByOwnerId(String id);

}
