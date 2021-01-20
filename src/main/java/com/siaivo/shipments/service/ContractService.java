package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import java.util.List;

public interface ContractService {

    void saveRequest(Contract contract);
    void saveContract (Contract contract);
    List<Contract> allContracts();
    List<Contract> openContracts();
    List<Contract> contractsForPreparation();
    List<Contract> fulfilledContracts ();
    Contract findContractById(int id);
}
