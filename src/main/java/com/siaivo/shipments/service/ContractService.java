package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;

import java.util.Date;
import java.util.List;

public interface ContractService {

    void saveContract(Contract contract);
    List<Contract> allContracts();
    List<Contract> openContracts();
    Contract findContractByContractNumberAndContractDate (String contractNumber, Date contractDate);

}
