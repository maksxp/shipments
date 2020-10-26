package com.siaivo.shipments.repository;

import com.siaivo.shipments.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("customerRepository")
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
    Customer findByCustomerName(String customerName);
    Customer findByCustomerCountry (String customerCountry);
    Customer findById(int id);

}
