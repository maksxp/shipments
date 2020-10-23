package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.siaivo.shipments.repository.ContractRepository;

import java.util.Date;
import java.util.List;

@Service("contractService")
public class ContractServiceImpl implements ContractService{
    @Autowired
    private ContractRepository contractRepository;

    @Override
    public void saveContract(Contract contract) {
        contract.setContractActive(true);
        contractRepository.save(contract);
    }

    @Override
    public List<Contract> allContracts(){
        return contractRepository.findAll();
    }

    @Override
    public List<Contract> openContracts(){
        return contractRepository.findByIsContractActive(true);
    }

    @Override
    public Contract findContractByContractNumberAndContractDate (String contractNumber, Date contractDate){
        return contractRepository.findContractByContractNumberAndContractDate(contractNumber, contractDate);
    }


}
