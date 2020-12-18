package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.siaivo.shipments.repository.ContractRepository;

import java.util.ArrayList;
import java.util.List;

@Service("contractService")
public class ContractServiceImpl implements ContractService{
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private ContractService contractService;

    @Override
    public void saveContract(Contract contract) {
        contract.setState("готується");
        contractRepository.save(contract);
    }

    @Override
    public List<Contract> allContracts(){
        return contractRepository.findAll();
    }

    @Override
    public List<Contract> openContracts(){
        List <Contract> closedContracts = new ArrayList<>();
        List <Contract> canceledContracts = new ArrayList<>();
        List <Contract> contractsToReturn = contractService.allContracts();
        closedContracts.addAll(contractRepository.findByStateLike("виконано"));
        canceledContracts.addAll(contractRepository.findByStateLike("відмінено"));
        contractsToReturn.removeAll(closedContracts);
        contractsToReturn.removeAll(canceledContracts);
        return contractsToReturn;
    }

    @Override
    public Contract findContractById(int id) {
        return contractRepository.findById(id);
    }
}
