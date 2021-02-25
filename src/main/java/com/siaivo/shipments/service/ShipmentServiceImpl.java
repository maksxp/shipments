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
import java.util.stream.Collectors;

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
    public List <Shipment> paymentsByTheEndOfThisWeek() {
        List <Shipment> paymentsByTheEndOfThisWeek = new ArrayList<>();
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
        getLastDateOfCurrentWeek();
        String [] allDatesOfWeek = getAllDatesOfCurrentWeek();
        for (int i=0;i<7;i++){
            paymentsByTheEndOfThisWeek.addAll(shipmentRepository.findByPlannedPaymentDateOfFirstPartSum(allDatesOfWeek[i]));
            paymentsByTheEndOfThisWeek.addAll(shipmentRepository.findByPlannedPaymentDateOfSecondPartSum(allDatesOfWeek[i]));
            paymentsByTheEndOfThisWeek.addAll(shipmentRepository.findByPlannedPaymentDateOfWholeSum(allDatesOfWeek[i]));
        }
        return paymentsByTheEndOfThisWeek;
    }


    @Override
    public List<Shipment> thisMonthShipments() {
        List <Shipment> thisMonthShipments = new ArrayList<>();
        String [] allDatesOfMonth = getAllDatesOfCurrentMonth();
        for (int i=0;i<allDatesOfMonth.length;i++){
            thisMonthShipments.addAll(shipmentRepository.findByPlannedLoadingDate(allDatesOfMonth[i]));
        }
        System.out.println(Arrays.toString(allDatesOfMonth));
        return thisMonthShipments;
    }

    @Override
    public List<Shipment> nextMonthShipments() {
        List <Shipment> nextMonthShipments = new ArrayList<>();
        String [] allDatesOfMonth = getAllDatesOfNextMonth();
        for (int i=0;i<allDatesOfMonth.length;i++){
            nextMonthShipments.addAll(shipmentRepository.findByPlannedLoadingDate(allDatesOfMonth[i]));
        }
        System.out.println(Arrays.toString(allDatesOfMonth));
        return nextMonthShipments;
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
    public List<Shipment> unpaidShipments() {
        return shipmentRepository.findUnpaidShipments();
    }

    @Override
    public List<Shipment> paidShipments() {
        return shipmentRepository.findPaidShipments();
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

    private String getLastDateOfCurrentWeek () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        System.out.println("last date of this week is: "+df.format(calendar.getTime()));
        return df.format(calendar.getTime());
    }

    private String getFirstDateOfCurrentMonth () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(calendar.getTime());
    }

    private String getFirstDateOfNextMonth () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
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

    private String [] getAllDatesOfCurrentMonth () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.set(Calendar.MONTH, 1);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String [] allDatesOfCurrentMonth = new String[maxDay];
        allDatesOfCurrentMonth[0] = getFirstDateOfCurrentMonth();
        for (int i=1;i<maxDay;i++){
            calendar.set(Calendar.DAY_OF_MONTH, i+1);
            allDatesOfCurrentMonth[i] = df.format(calendar.getTime());
        }
        return allDatesOfCurrentMonth;
    }

    private String [] getAllDatesOfNextMonth () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.set(Calendar.MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String [] allDatesOfNextMonth = new String[maxDay];
        allDatesOfNextMonth[0] = getFirstDateOfNextMonth();
        for (int i=1;i<maxDay;i++){
            calendar.set(Calendar.DAY_OF_MONTH, i+1);
            allDatesOfNextMonth[i] = df.format(calendar.getTime());
        }
        return allDatesOfNextMonth;
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
