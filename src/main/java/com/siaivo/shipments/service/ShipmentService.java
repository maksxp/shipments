package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Shipment;

import java.util.List;

public interface ShipmentService {

    Shipment findById(int id) ;

    void saveShipment (Shipment shipment);

    void fulfillShipment (Shipment shipment);

    void deleteShipment (Shipment shipment);

    List<Shipment>thisWeekShipments();

    List<Shipment>nextWeekShipments();

    List<Shipment> allShipments();

    List<Shipment> openShipments();

    List<Shipment> fulfilledShipments();

    List<Shipment> allShipmentsPerContract(Contract contract);
}
