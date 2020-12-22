package com.siaivo.shipments.repository;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Shipment;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository("shipmentRepository")
public interface ShipmentRepository {

    Shipment findById(int id);
    Shipment findByContract(Contract contract);
    Shipment findByPlannedLoadingDate (Date plannedLoadingDate);
    Shipment findByActualLoadingDate (Date actualLoadingDate);
    Shipment findByPlannedUnloadingDate (Date plannedUnloadingDate);
    Shipment findByActualUnloadingDate (Date actualUnloadingDate);
    Shipment findByLogisticInstructionStatus (String logisticInstructionStatus);
    Shipment findByLabelsStatus (String labelsStatus);
}
