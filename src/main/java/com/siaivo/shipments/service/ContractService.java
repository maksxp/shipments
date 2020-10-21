package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import java.util.List;

public interface ContractService {

    void saveContract(Contract contract);
    List<Contract> allContracts();
    List<Contract> openContracts();

}