package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Shipment;
import com.siaivo.shipments.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("shipmentService")
public class ShipmentServiceImpl implements ShipmentService{
    @Qualifier("shipmentRepository")
    @Autowired
    private ShipmentRepository shipmentRepository;
    @Override
    public Shipment findById(int id) {
        return shipmentRepository.findById(id);
    }

    @Override
    public List<Shipment> findByContract(Contract contract) {
        return shipmentRepository.findByContract(contract);
    }
}
