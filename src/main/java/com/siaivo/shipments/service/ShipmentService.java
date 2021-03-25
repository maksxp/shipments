package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;
import com.siaivo.shipments.model.Shipment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface ShipmentService {

    Shipment findById(int id) ;

    void saveShipment (Shipment shipment);

    void fulfillShipment (Shipment shipment);

    void returnShipmentToWork (Shipment shipment);

    void deleteShipment (Shipment shipment);

    List<Shipment>thisWeekShipments();

    List<Shipment> allPlannedPayments();

    List<Shipment> allOverduePayments();

    List<Shipment> allPaymentsByTheEndOfThisWeek();

    List<Shipment> allPaymentsByTheEndOfThisMonth();

    List<Shipment> allPaymentsByTheEndOfNextWeek();

    List<Shipment> allPaymentsByTheEndOfNextMonth();

//    List <Shipment> firstSumPaymentsByTheEndOfThisWeek();
//
//    List <Shipment> secondSumPaymentsByTheEndOfThisWeek();
//
//    List <Shipment> wholeSumPaymentsByTheEndOfThisWeek();

    List<Shipment>thisMonthShipments();

    List<Shipment>nextMonthShipments();

    List<Shipment>nextWeekShipments();

    List<Shipment> allShipments();

    List<Shipment> openShipments();

    List<Shipment> unpaidShipments();

    List<Shipment> notLoadedAndWithoutAnyPaymentShipments();

    List<Shipment> paidShipments();

    List<Shipment> fulfilledShipments();

    List<Shipment> allShipmentsPerContract(Contract contract);

    BigDecimal getTotalSumOfAllInvoicesInEUR ();

    BigDecimal getTotalSumOfPaidInvoicesInEUR ();

    BigDecimal getTotalSumOfUnpaidInvoicesInEUR ();

    BigDecimal getTotalSumOfAllInvoicesInUSD ();

    BigDecimal getTotalSumOfPaidInvoicesInUSD ();

    BigDecimal getTotalSumOfUnpaidInvoicesInUSD ();

    BigDecimal getTotalSumOfAllInvoicesInUAH ();

    BigDecimal getTotalSumOfPaidInvoicesInUAH ();

    BigDecimal getTotalSumOfUnpaidInvoicesInUAH ();

}
