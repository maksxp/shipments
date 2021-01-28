package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;
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
    public void saveShipment(Shipment shipment) {
        shipmentRepository.save(shipment);
    }

    @Override
    public void deleteShipment(Shipment shipment) {
        shipmentRepository.delete(shipment);
    }

//    @Override
//    public Shipment getOne(int id) {
//        return shipmentRepository.getOne(id);
//    }



    @Override
    public List<Shipment> allShipments() {
        return shipmentRepository.findAll();
    }

    @Override
    public List<Shipment> allShipmentsPerContract(Contract contract) {
        return shipmentRepository.findByContract(contract);
    }
}
