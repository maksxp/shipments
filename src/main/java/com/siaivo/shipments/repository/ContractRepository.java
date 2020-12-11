package com.siaivo.shipments.repository;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("contractRepository")
public interface ContractRepository extends JpaRepository<Contract, Long> {
    Contract findById(int id);
    List<Contract> findByCustomer(String customer);
    List<Contract> findByContractNumber(String contractNumber);
    List<Contract> findByProducts(Product products);
    List<Contract> findByState (String state);
    List<Contract> findByStateNotLike (String state);
    List<Contract> findByStateLike (String state);
    List<Contract> findByContractDate(Date contractDate);
    Contract findContractByContractNumberAndContractDate (String contractNumber, Date contractDate);
}
