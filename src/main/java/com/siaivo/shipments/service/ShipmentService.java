package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Shipment;

import java.util.List;

public interface ShipmentService {

    Shipment findById(int id) ;
    List<Shipment> findByContract (Contract contract);
}
