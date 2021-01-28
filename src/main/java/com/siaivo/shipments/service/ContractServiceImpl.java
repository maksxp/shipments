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
    public void saveRequest(Contract contract) {
        contract.setState("готується");
        contractRepository.save(contract);
    }

    @Override
    public void saveContract(Contract contract) {
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
        closedContracts.addAll(contractRepository.findByStateLike("виконаний"));
        canceledContracts.addAll(contractRepository.findByStateLike("скасований"));
        contractsToReturn.removeAll(closedContracts);
        contractsToReturn.removeAll(canceledContracts);
        return contractsToReturn;
    }

    @Override
    public List<Contract> contractsForPreparation() {
        return contractRepository.findByStateLike("готується");
    }

    @Override
    public List<Contract> fulfilledContracts() {
        return contractRepository.findByStateLike("виконаний");
    }

    @Override
    public Contract findContractById(int id) {
        return contractRepository.findById(id);
    }
}
