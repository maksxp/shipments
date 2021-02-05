package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;
import com.siaivo.shipments.model.Shipment;
import com.siaivo.shipments.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public void fulfillShipment(Shipment shipment) {
        shipment.setIsFulfilled(true);
        shipmentRepository.save(shipment);
    }

    @Override
    public void deleteShipment(Shipment shipment) {
        shipmentRepository.delete(shipment);
    }

    @Override
    public List<Shipment> thisWeekShipments() {
        List <Shipment> thisWeekShipments = new ArrayList<>();
        String [] allDatesOfWeek = getAllDatesOfCurrentWeek();
        for (int i=0;i<7;i++){
            thisWeekShipments.addAll(shipmentRepository.findByPlannedLoadingDate(allDatesOfWeek[i]));
        }
        return thisWeekShipments;
    }

    @Override
    public List<Shipment> nextWeekShipments() {
        List <Shipment> nextWeekShipments = new ArrayList<>();
        String [] allDatesOfWeek = getAllDatesOfNextWeek();
        for (int i=0;i<7;i++){
            nextWeekShipments.addAll(shipmentRepository.findByPlannedLoadingDate(allDatesOfWeek[i]));
        }
        return nextWeekShipments;
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
    public List<Shipment> openShipments() {
        return shipmentRepository.findShipmentsByIsFulfilledIsFalse();
    }

    @Override
    public List<Shipment> fulfilledShipments() {
        return shipmentRepository.findShipmentsByIsFulfilledIsTrue();
    }

    @Override
    public List<Shipment> allShipmentsPerContract(Contract contract) {
        return shipmentRepository.findByContract(contract);
    }



//    private int getCurrentWeekNumber () {
//        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
//        Date today = new Date();
//        calendar.setTime(today);
//        return calendar.get(Calendar.WEEK_OF_YEAR);
//    }
//
//    private int getNextWeekNumber () {
//        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
//        Date today = new Date();
//        calendar.setTime(today);
//        calendar.add(Calendar.DATE, 7);
//        return calendar.get(Calendar.WEEK_OF_YEAR);
//    }

    private String getFirstDateOfCurrentWeek () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(calendar.getTime());
    }

    private String getFirstDateOfNextWeek () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.add(Calendar.WEEK_OF_YEAR,1);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(calendar.getTime());
    }

    private String [] getAllDatesOfCurrentWeek () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String [] allDatesOfCurrentWeek = new String[7];
        allDatesOfCurrentWeek[0] = getFirstDateOfCurrentWeek();
        for (int i=1;i<7;i++){
            calendar.add(Calendar.DATE, 1);
            allDatesOfCurrentWeek[i] = df.format(calendar.getTime());
        }
        return allDatesOfCurrentWeek;
    }

    private String [] getAllDatesOfNextWeek () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.add(Calendar.WEEK_OF_YEAR,1);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        String [] allDatesOfNextWeek = new String[7];
        allDatesOfNextWeek[0] = getFirstDateOfNextWeek();
        for (int i=1;i<7;i++){
            calendar.add(Calendar.DATE, 1);
            allDatesOfNextWeek[i] = df.format(calendar.getTime());
        }
        return allDatesOfNextWeek;
    }
}
