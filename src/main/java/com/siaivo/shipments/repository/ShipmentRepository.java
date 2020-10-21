package com.siaivo.shipments.repository;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Invoice;
import com.siaivo.shipments.model.Shipment;
import com.siaivo.shipments.support.LogisticInstructionStatus;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository("shipmentRepository")
public interface ShipmentRepository {

    Shipment findById(int id);
    Shipment findByContract(Contract contract);
    Shipment findByInvoice (Invoice invoice);
    Shipment findByPlannedLoadingDate (Date plannedLoadingDate);
    Shipment findByActualLoadingDate (Date actualLoadingDate);
    Shipment findByPlannedUnloadingDate (Date plannedUnloadingDate);
    Shipment findByActualUnloadingDate (Date actualUnloadingDate);
    Shipment findByLogisticInstructionStatus (LogisticInstructionStatus logisticInstructionStatus);
    Shipment findByLabelsStatus (LogisticInstructionStatus labelsStatus);
}
