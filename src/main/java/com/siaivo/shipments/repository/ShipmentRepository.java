package com.siaivo.shipments.repository;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("shipmentRepository")
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {

    Shipment findById(int id);
    List <Shipment> findByContract (Contract contract);
    List <Shipment> findByPlannedLoadingDate(String date);
    List <Shipment> findByActualLoadingDate(String date);
    List <Shipment> findShipmentsByIsFulfilledIsFalse();
    List <Shipment> findShipmentsByIsFulfilledIsTrue();
    // List <Shipment> findShipmentsByIsAndActualPaymentDateOfSecondPartSumContains(String parameter);
}
