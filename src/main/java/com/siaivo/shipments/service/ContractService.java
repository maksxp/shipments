package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import java.util.List;

public interface ContractService {

    void saveEditRequest(Contract contract);
    void saveContract (Contract contract);
    List<Contract> allContracts();
    List<Contract> openContracts();
    List <Contract> cancelledContracts();
    List<Contract> unsignedContracts();
    List<Contract> contractsForPreparation();
    List<Contract> fulfilledContracts ();
    Contract findContractById(int id);
}
