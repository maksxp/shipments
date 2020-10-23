package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Customer;
import com.siaivo.shipments.model.Role;
import com.siaivo.shipments.model.User;
import com.siaivo.shipments.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService{
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public List<Customer> allCustomers(){
        return customerRepository.findAll();
    }

    @Override
    public Customer findCustomerByCustomerName(String customerName) {
        return customerRepository.findByCustomerName(customerName);
    }

    @Override
    public Customer findCustomerById(int id) {
        return customerRepository.findById(id);
    }

    @Override
    public void editCustomerType (String customerType,int customerId){
        Customer customer = customerRepository.findById(customerId);
        customer.setCustomerType(customerType);
    }

    @Override
    public void editCustomerName (String customerName,int customerId){
        Customer customer = customerRepository.findById(customerId);
        customer.setCustomerName(customerName);
    }

    @Override
    public void editCustomerComment (String comment,int customerId){
        Customer customer = customerRepository.findById(customerId);
        customer.setComment(comment);
    }

    @Override
    public void editCustomerCountry (String customerCountry,int customerId){
        Customer customer = customerRepository.findById(customerId);
        customer.setCustomerCountry(customerCountry);
    }

}
