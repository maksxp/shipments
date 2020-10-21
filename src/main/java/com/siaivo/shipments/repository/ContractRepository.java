package com.siaivo.shipments.repository;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;
import org.springframework.stereotype.Repository;

@Repository("contractRepository")
public interface ContractRepository {
    Contract findById(int id);
    Contract findByCustomer(String customer);
    Contract findByContractNumber(String contractNumber);
    Contract findByProducts(Product products);
    Contract findByIsContractActive (Boolean isContractActive);

}
