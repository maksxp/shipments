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
    public void saveEditRequest(Contract contract) {
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
        List <Contract> cancelledContracts = new ArrayList<>();
        List <Contract> contractsToReturn = contractService.allContracts();
        closedContracts.addAll(contractRepository.findByStateLike("виконаний"));
        cancelledContracts.addAll(contractRepository.findByStateLike("скасований"));
        contractsToReturn.removeAll(closedContracts);
        contractsToReturn.removeAll(cancelledContracts);
        return contractsToReturn;
    }

    @Override
    public List<Contract> unsignedContracts()  {
        return contractRepository.findByStateLike("підготовлений");
    }

    @Override
    public List<Contract> cancelledContracts()  {
        return contractRepository.findByStateLike("скасований");
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
