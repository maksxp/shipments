package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Customer;

import java.util.List;

public interface CustomerService {

    void saveCustomer(Customer customer);
    List<Customer> allCustomers();
    Customer findCustomerByCustomerName (String customerName);
    Customer findCustomerById (int id);
    void editCustomerType(String customerType, int customerId);
    void editCustomerName(String customerName, int customerId);
    void editCustomerCountry(String customerCountry, int customerId);
    void editCustomerComment(String comment, int customerId);

}
