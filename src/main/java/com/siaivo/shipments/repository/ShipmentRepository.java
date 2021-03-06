package com.siaivo.shipments.repository;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository("shipmentRepository")
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {

    Shipment findById(int id);
    List <Shipment> findByContract (Contract contract);
    List <Shipment> findByPlannedLoadingDate(String date);
    List <Shipment> findByPlannedPaymentDateOfFirstPartSum(String date);
    List <Shipment> findByPlannedPaymentDateOfSecondPartSum(String date);
    List <Shipment> findByPlannedPaymentDateOfWholeSum(String date);
    List <Shipment> findByActualLoadingDate(String date);
    List <Shipment> findShipmentsByIsFulfilledIsFalse();
    List <Shipment> findShipmentsByIsFulfilledIsTrue();
    @Query ("SELECT s FROM Shipment s WHERE actualPaymentDateOfWholeSum='' OR actualPaymentDateOfWholeSum IS NULL")
    List <Shipment> findUnpaidShipments();
    @Query ("SELECT s FROM Shipment s WHERE actualPaymentDateOfWholeSum<>'' AND actualPaymentDateOfWholeSum IS NOT NULL")
    List <Shipment> findPaidShipments();
    @Query ("SELECT s FROM Shipment s WHERE actualLoadingDate='' OR actualLoadingDate IS NULL")
    List <Shipment> findNotLoadedShipments();
    @Query ("SELECT s FROM Shipment s WHERE actualPaymentDateOfFirstPartSum<>'' AND actualPaymentDateOfFirstPartSum IS NOT NULL")
    List <Shipment> findFirstPartSumPaidShipments();
    @Query ("SELECT s FROM Shipment s WHERE actualPaymentDateOfSecondPartSum<>'' AND actualPaymentDateOfSecondPartSum IS NOT NULL")
    List <Shipment> findSecondPartSumPaidShipments();
    @Query ("SELECT s FROM Shipment s WHERE plannedLoadingDate='' OR plannedLoadingDate IS NULL")
    List <Shipment> findShipmentsWithoutPlannedLoadingDate();

}
