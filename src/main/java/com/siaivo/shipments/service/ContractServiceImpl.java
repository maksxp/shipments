package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Customer;
import com.siaivo.shipments.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.siaivo.shipments.repository.ContractRepository;

import java.util.ArrayList;
import java.util.List;

@Service("contractService")
public class ContractServiceImpl implements ContractService{

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ShipmentService shipmentService;

    @Override
    public void saveEditRequest(Contract contract) {
        contract.setState("готується");
        contractRepository.save(contract);
    }

    @Override
    public void saveContract(Contract contract) {
        if (!contract.getState().equals("скасований")){
            contractRepository.save(contract);
        } else {
            shipmentService.notLoadedAndWithoutAnyPaymentShipments().forEach(shipment -> shipmentService.deleteShipment(shipment));
            contractRepository.save(contract);
        }
    }

    @Override
    public List<Contract> allContracts(){
        return contractRepository.findAll();
    }

    @Override
    public List<Contract> allContractsPerCustomer(Customer customer) {
        return contractRepository.findByCustomer(customer);
    }

    @Override
    public List<Contract> openContracts(){
        List <Contract> closedContracts = new ArrayList<>();
        List <Contract> cancelledContracts = new ArrayList<>();
        List <Contract> contractsToReturn = contractRepository.findAll();
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

    @Override
    public Contract findByContractNumberAndContractDate(String contractNumber, String contractDate) {
        return contractRepository.findByContractNumberAndContractDate(contractNumber,contractDate);
    }



}
