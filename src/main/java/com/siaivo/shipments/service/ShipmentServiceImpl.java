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
    public List <Shipment> allPaymentsByTheEndOfThisWeek() {
        List <Shipment> allPaymentsByTheEndOfThisWeek = new ArrayList<>();
        allPaymentsByTheEndOfThisWeek.addAll(
                firstSumPaymentsByTheEndOfThisWeek().stream().filter(shipment -> shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
        allPaymentsByTheEndOfThisWeek.addAll(
                secondSumPaymentsByTheEndOfThisWeek().stream().filter(shipment -> shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
        allPaymentsByTheEndOfThisWeek.addAll(
                wholeSumPaymentsByTheEndOfThisWeek().stream().filter(shipment -> !shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
        allPaymentsByTheEndOfThisWeek.forEach(shipment -> System.out.println("invoice is: " +shipment.getInvoiceNumber()+"   sum id is: "+shipment.getSumForPayIdentity()));
        return allPaymentsByTheEndOfThisWeek;
    }

    @Override
    public List <Shipment> allPaymentsByTheEndOfNextWeek() {
        List <Shipment> allPaymentsByTheEndOfNextWeek = new ArrayList<>();
        allPaymentsByTheEndOfNextWeek.addAll(
                firstSumPaymentsByTheEndOfNextWeek().stream().filter(shipment -> shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
        allPaymentsByTheEndOfNextWeek.addAll(
                secondSumPaymentsByTheEndOfNextWeek().stream().filter(shipment -> shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
        allPaymentsByTheEndOfNextWeek.addAll(
                wholeSumPaymentsByTheEndOfNextWeek().stream().filter(shipment -> !shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
                return allPaymentsByTheEndOfNextWeek;
    }

    public List <Shipment> firstSumPaymentsByTheEndOfThisWeek(){
        List <Shipment> firstSumPaymentsByTheEndOfThisWeek = new ArrayList<>();
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
        unpaidShipments.forEach(shipment -> System.out.println("first sumID: "+shipment.getSumForPayIdentity()));
        System.out.println();
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        firstSumPaymentsByTheEndOfThisWeek = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfFirstPartSum() ||shipment.getActualPaymentDateOfFirstPartSum().equals("")).collect(Collectors.toList());
        firstSumPaymentsByTheEndOfThisWeek = firstSumPaymentsByTheEndOfThisWeek.stream().filter(shipment -> {
            try {
                return 0 <= df.parse(getLastDateOfCurrentWeek()).compareTo(df.parse(shipment.getPlannedPaymentDateOfFirstPartSum()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        firstSumPaymentsByTheEndOfThisWeek.forEach(shipment -> shipment.setSumForPayIdentity("first"));
        return firstSumPaymentsByTheEndOfThisWeek;
    }

    public List <Shipment> firstSumPaymentsByTheEndOfNextWeek(){
        List <Shipment> firstSumPaymentsByTheEndOfNextWeek = new ArrayList<>();
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
                Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        firstSumPaymentsByTheEndOfNextWeek = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfFirstPartSum() ||shipment.getActualPaymentDateOfFirstPartSum().equals("")).collect(Collectors.toList());
        firstSumPaymentsByTheEndOfNextWeek = firstSumPaymentsByTheEndOfNextWeek.stream().filter(shipment -> {
            try {
                return 0 <= df.parse(getLastDateOfNextWeek()).compareTo(df.parse(shipment.getPlannedPaymentDateOfFirstPartSum()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        firstSumPaymentsByTheEndOfNextWeek.forEach(shipment -> shipment.setSumForPayIdentity("first"));
        return firstSumPaymentsByTheEndOfNextWeek;
    }

    public List <Shipment> secondSumPaymentsByTheEndOfThisWeek(){
        List <Shipment> secondSumPaymentsByTheEndOfThisWeek = new ArrayList<>();
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
        unpaidShipments.forEach(shipment -> System.out.println("second sumID: "+shipment.getSumForPayIdentity()));
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        secondSumPaymentsByTheEndOfThisWeek = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfSecondPartSum() ||shipment.getActualPaymentDateOfSecondPartSum().equals("")).collect(Collectors.toList());
        secondSumPaymentsByTheEndOfThisWeek = secondSumPaymentsByTheEndOfThisWeek.stream().filter(shipment -> {
            try {
                return 0 <= df.parse(getLastDateOfCurrentWeek()).compareTo(df.parse(shipment.getPlannedPaymentDateOfSecondPartSum()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        secondSumPaymentsByTheEndOfThisWeek.forEach(shipment -> shipment.setSumForPayIdentity("second"));
        return secondSumPaymentsByTheEndOfThisWeek;
    }

    public List <Shipment> secondSumPaymentsByTheEndOfNextWeek(){
        List <Shipment> secondSumPaymentsByTheEndOfNextWeek = new ArrayList<>();
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        System.out.println("last date of next week: "+getLastDateOfNextWeek());
        secondSumPaymentsByTheEndOfNextWeek = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfSecondPartSum() ||shipment.getActualPaymentDateOfSecondPartSum().equals("")).collect(Collectors.toList());
        secondSumPaymentsByTheEndOfNextWeek = secondSumPaymentsByTheEndOfNextWeek.stream().filter(shipment -> {
            try {
                return 0 <= df.parse(getLastDateOfNextWeek()).compareTo(df.parse(shipment.getPlannedPaymentDateOfSecondPartSum()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        secondSumPaymentsByTheEndOfNextWeek.forEach(shipment -> shipment.setSumForPayIdentity("second"));
        return secondSumPaymentsByTheEndOfNextWeek;
    }

    public List <Shipment> wholeSumPaymentsByTheEndOfThisWeek(){
        List <Shipment> wholeSumPaymentsByTheEndOfThisWeek = new ArrayList<>();
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
        unpaidShipments.forEach(shipment -> System.out.println("whole sumID: "+shipment.getSumForPayIdentity()));
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        wholeSumPaymentsByTheEndOfThisWeek = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfWholeSum() ||shipment.getActualPaymentDateOfWholeSum().equals("")).collect(Collectors.toList());
        wholeSumPaymentsByTheEndOfThisWeek = wholeSumPaymentsByTheEndOfThisWeek.stream().filter(shipment -> {
            try {
                return 0 <= df.parse(getLastDateOfCurrentWeek()).compareTo(df.parse(shipment.getPlannedPaymentDateOfWholeSum()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        wholeSumPaymentsByTheEndOfThisWeek.forEach(shipment -> shipment.setSumForPayIdentity("whole"));
        return wholeSumPaymentsByTheEndOfThisWeek;
    }

    public List <Shipment> wholeSumPaymentsByTheEndOfNextWeek(){
        List <Shipment> wholeSumPaymentsByTheEndOfNextWeek = new ArrayList<>();
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        wholeSumPaymentsByTheEndOfNextWeek = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfWholeSum() ||shipment.getActualPaymentDateOfWholeSum().equals("")).collect(Collectors.toList());
        wholeSumPaymentsByTheEndOfNextWeek = wholeSumPaymentsByTheEndOfNextWeek.stream().filter(shipment -> {
            try {
                return 0 <= df.parse(getLastDateOfNextWeek()).compareTo(df.parse(shipment.getPlannedPaymentDateOfWholeSum()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        wholeSumPaymentsByTheEndOfNextWeek.forEach(shipment -> shipment.setSumForPayIdentity("whole"));
        return wholeSumPaymentsByTheEndOfNextWeek;
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
        return df.format(calendar.getTime());
    }

    private String getLastDateOfNextWeek () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        calendar.add(Calendar.WEEK_OF_YEAR,1);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
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
