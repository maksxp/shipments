package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;
import com.siaivo.shipments.model.ProductForShipment;
import com.siaivo.shipments.model.Shipment;
import com.siaivo.shipments.repository.ProductForShipmentRepository;
import com.siaivo.shipments.repository.ShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLOutput;
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

    @Qualifier("productForShipmentRepository")
    @Autowired
    private ProductForShipmentRepository productForShipmentRepository;

    @Autowired
    private ProductForShipmentService productForShipmentService;

    @Override
    public Shipment findById(int id) {
        return shipmentRepository.findById(id);
    }

    @Override
    public void saveShipment(Shipment shipment) {
        if (!"оплата частинами".equals(shipment.getPaymentTerms())) {
            shipment.setIsInvoiceCorrect(true);
        } else if (0==shipment.getInvoiceWholeSum().compareTo(shipment.getInvoiceFirstPartSum().add(shipment.getInvoiceSecondPartSum()))){
            shipment.setIsInvoiceCorrect(true);
        } else {
            shipment.setIsInvoiceCorrect(false);
        }
        shipmentRepository.save(shipment);
    }

    @Override
    public void fulfillShipment(Shipment shipment) {
        shipment.setIsFulfilled(true);
        shipmentRepository.save(shipment);
    }

    @Override
    public void returnShipmentToWork(Shipment shipment) {
        shipment.setIsFulfilled(false);
        shipmentRepository.save(shipment);
    }

    @Override
    public void deleteShipment(Shipment shipment) {
            productForShipmentRepository.findByShipment(shipment).forEach(productForShipment -> productForShipmentService.deleteProductForShipment(productForShipment));
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

//    @Override
//    public List<Shipment> thisWeekPayments() {
//        List <Shipment> thisWeekPayments = new ArrayList<>();
//        String [] allDatesOfWeek = getAllDatesOfCurrentWeek();
//        for (int i=0;i<7;i++){
//            thisWeekPayments.addAll(shipmentRepository.findByPlannedPaymentDateOfFirstPartSum(allDatesOfWeek[i]));
//        }
//        return thisWeekPayments;
//    }

    @Override
    public List <Shipment> allReleasedShipments() {
        List <Shipment> allReleasedShipments;
        allReleasedShipments = allShipments().stream().filter(shipment -> shipment.isLoaded()).collect(Collectors.toList());
         return allReleasedShipments;
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
//        allPaymentsByTheEndOfThisWeek.forEach(shipment -> System.out.println("invoice is: " +shipment.getInvoiceNumber()+"   sum id is: "+shipment.getSumForPayIdentity()));
        return allPaymentsByTheEndOfThisWeek;
    }

    @Override
    public List <Shipment> allPlannedPayments() {
        List <Shipment> allPlannedPayments = new ArrayList<>();
        allPlannedPayments.addAll(
                firstSumPlannedPayments().stream().filter(shipment -> shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
        allPlannedPayments.addAll(
                secondSumPlannedPayments().stream().filter(shipment -> shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
        allPlannedPayments.addAll(
                wholeSumPlannedPayments().stream().filter(shipment -> !shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
//        allPlannedPayments.forEach(shipment -> System.out.println("invoice is: " +shipment.getInvoiceNumber()+"   sum id is: "+shipment.getSumForPayIdentity()));
        return allPlannedPayments;
    }

    @Override
    public List <Shipment> allOverduePayments() {
        List <Shipment> allOverduePayments = new ArrayList<>();
        allOverduePayments.addAll(
                firstSumOverduePayments().stream().filter(shipment -> shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
        allOverduePayments.addAll(
                secondSumOverduePayments().stream().filter(shipment -> shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
        allOverduePayments.addAll(
                wholeSumOverduePayments().stream().filter(shipment -> !shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
//        allOverduePayments.forEach(shipment -> System.out.println("invoice is: " +shipment.getInvoiceNumber()+"   sum id is: "+shipment.getSumForPayIdentity()));
        return allOverduePayments;
    }

    @Override
    public List <Shipment> allPaymentsByTheEndOfThisMonth() {
        List <Shipment> allPaymentsByTheEndOfThisMonth = new ArrayList<>();
        allPaymentsByTheEndOfThisMonth.addAll(
                firstSumPaymentsByTheEndOfThisMonth().stream().filter(shipment -> shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
        allPaymentsByTheEndOfThisMonth.addAll(
                secondSumPaymentsByTheEndOfThisMonth().stream().filter(shipment -> shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
        allPaymentsByTheEndOfThisMonth.addAll(
                wholeSumPaymentsByTheEndOfThisMonth().stream().filter(shipment -> !shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
//        allPaymentsByTheEndOfThisMonth.forEach(shipment -> System.out.println("invoice is: " +shipment.getInvoiceNumber()+"   sum id is: "+shipment.getSumForPayIdentity()));
        return allPaymentsByTheEndOfThisMonth;
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



    @Override
    public List <Shipment> allPaymentsByTheEndOfNextMonth() {
        List <Shipment> allPaymentsByTheEndOfNextMonth = new ArrayList<>();
        allPaymentsByTheEndOfNextMonth.addAll(
                firstSumPaymentsByTheEndOfNextMonth().stream().filter(shipment -> shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
        allPaymentsByTheEndOfNextMonth.addAll(
                secondSumPaymentsByTheEndOfNextMonth().stream().filter(shipment -> shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
        allPaymentsByTheEndOfNextMonth.addAll(
                wholeSumPaymentsByTheEndOfNextMonth().stream().filter(shipment -> !shipment.getContract().getPaymentTerms().equals("оплата частинами")).collect(Collectors.toList()));
        return allPaymentsByTheEndOfNextMonth;
    }

    public List <Shipment> firstSumPaymentsByTheEndOfThisWeek(){
//        List <Shipment> firstSumPaymentsByTheEndOfThisWeek = new ArrayList<>();
        List <Shipment> firstSumPaymentsByTheEndOfThisWeek;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
//        unpaidShipments.forEach(shipment -> System.out.println("first sumID: "+shipment.getSumForPayIdentity()));
//        System.out.println();
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        firstSumPaymentsByTheEndOfThisWeek = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfFirstPartSum() || shipment.getActualPaymentDateOfFirstPartSum().equals("")).collect(Collectors.toList());
        firstSumPaymentsByTheEndOfThisWeek = firstSumPaymentsByTheEndOfThisWeek.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfFirstPartSum() && !shipment.getPlannedPaymentDateOfFirstPartSum().equals("")).collect(Collectors.toList());
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

    public List <Shipment> firstSumPaymentsByTheEndOfThisMonth (){
//        List <Shipment> firstSumPaymentsByTheEndOfThisMonth = new ArrayList<>();
        List <Shipment> firstSumPaymentsByTheEndOfThisMonth;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
//        unpaidShipments.forEach(shipment -> System.out.println("first sumID: "+shipment.getSumForPayIdentity()));
//        System.out.println();
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        firstSumPaymentsByTheEndOfThisMonth = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfFirstPartSum() || shipment.getActualPaymentDateOfFirstPartSum().equals("")).collect(Collectors.toList());
        firstSumPaymentsByTheEndOfThisMonth = firstSumPaymentsByTheEndOfThisMonth.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfFirstPartSum() && !shipment.getPlannedPaymentDateOfFirstPartSum().equals("")).collect(Collectors.toList());
        firstSumPaymentsByTheEndOfThisMonth = firstSumPaymentsByTheEndOfThisMonth.stream().filter(shipment -> {
            try {
                return 0 <= df.parse(getLastDateOfCurrentMonth()).compareTo(df.parse(shipment.getPlannedPaymentDateOfFirstPartSum()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        firstSumPaymentsByTheEndOfThisMonth.forEach(shipment -> shipment.setSumForPayIdentity("first"));
        return firstSumPaymentsByTheEndOfThisMonth;
    }

    public List <Shipment> firstSumPaymentsByTheEndOfNextWeek(){
//        List <Shipment> firstSumPaymentsByTheEndOfNextWeek = new ArrayList<>();
        List <Shipment> firstSumPaymentsByTheEndOfNextWeek;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        firstSumPaymentsByTheEndOfNextWeek = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfFirstPartSum() || shipment.getActualPaymentDateOfFirstPartSum().equals("")).collect(Collectors.toList());
        firstSumPaymentsByTheEndOfNextWeek = firstSumPaymentsByTheEndOfNextWeek.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfFirstPartSum() && !shipment.getPlannedPaymentDateOfFirstPartSum().equals("")).collect(Collectors.toList());
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

    public List <Shipment> firstSumPaymentsByTheEndOfNextMonth(){
//        List <Shipment> firstSumPaymentsByTheEndOfNextMonth = new ArrayList<>();
        List <Shipment> firstSumPaymentsByTheEndOfNextMonth;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        firstSumPaymentsByTheEndOfNextMonth = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfFirstPartSum() || shipment.getActualPaymentDateOfFirstPartSum().equals("")).collect(Collectors.toList());
        firstSumPaymentsByTheEndOfNextMonth = firstSumPaymentsByTheEndOfNextMonth.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfFirstPartSum() && !shipment.getPlannedPaymentDateOfFirstPartSum().equals("")).collect(Collectors.toList());
        firstSumPaymentsByTheEndOfNextMonth = firstSumPaymentsByTheEndOfNextMonth.stream().filter(shipment -> {
            try {
                return 0 <= df.parse(getLastDateOfNextMonth()).compareTo(df.parse(shipment.getPlannedPaymentDateOfFirstPartSum()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        firstSumPaymentsByTheEndOfNextMonth.forEach(shipment -> shipment.setSumForPayIdentity("first"));
        return firstSumPaymentsByTheEndOfNextMonth;
    }

    public List <Shipment> firstSumPlannedPayments(){
//        List <Shipment> firstSumPaymentsByTheEndOfThisWeek = new ArrayList<>();
        List <Shipment> firstSumPlannedPayments;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
//        unpaidShipments.forEach(shipment -> System.out.println("first sumID: "+shipment.getSumForPayIdentity()));
//        System.out.println();
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        firstSumPlannedPayments = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfFirstPartSum() || shipment.getActualPaymentDateOfFirstPartSum().equals("")).collect(Collectors.toList());
        firstSumPlannedPayments = firstSumPlannedPayments.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfFirstPartSum() && !shipment.getPlannedPaymentDateOfFirstPartSum().equals("")).collect(Collectors.toList());
        firstSumPlannedPayments.forEach(shipment -> shipment.setSumForPayIdentity("first"));
        return firstSumPlannedPayments;
    }

    public List <Shipment> firstSumOverduePayments(){
//        List <Shipment> firstSumOverduePayments = new ArrayList<>();
        List <Shipment> firstSumOverduePayments;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        firstSumOverduePayments = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfFirstPartSum() || shipment.getActualPaymentDateOfFirstPartSum().equals("")).collect(Collectors.toList());
        firstSumOverduePayments = firstSumOverduePayments.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfFirstPartSum() && !shipment.getPlannedPaymentDateOfFirstPartSum().equals("")).collect(Collectors.toList());
        firstSumOverduePayments = firstSumOverduePayments.stream().filter(shipment -> {
            try {
                return 0 < df.parse(getTodayDate()).compareTo(df.parse(shipment.getPlannedPaymentDateOfFirstPartSum()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        firstSumOverduePayments.forEach(shipment -> shipment.setSumForPayIdentity("first"));
        return firstSumOverduePayments;
    }

    public List <Shipment> secondSumPaymentsByTheEndOfThisWeek(){
//        List <Shipment> secondSumPaymentsByTheEndOfThisWeek = new ArrayList<>();
        List <Shipment> secondSumPaymentsByTheEndOfThisWeek;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
//        unpaidShipments.forEach(shipment -> System.out.println("second sumID: "+shipment.getSumForPayIdentity()));
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        secondSumPaymentsByTheEndOfThisWeek = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfSecondPartSum() ||shipment.getActualPaymentDateOfSecondPartSum().equals("")).collect(Collectors.toList());
        secondSumPaymentsByTheEndOfThisWeek = secondSumPaymentsByTheEndOfThisWeek.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfSecondPartSum() && !shipment.getPlannedPaymentDateOfSecondPartSum().equals("")).collect(Collectors.toList());
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

    public List <Shipment> secondSumPaymentsByTheEndOfThisMonth(){
//        List <Shipment> secondSumPaymentsByTheEndOfThisMonth = new ArrayList<>();
        List <Shipment> secondSumPaymentsByTheEndOfThisMonth;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
//        unpaidShipments.forEach(shipment -> System.out.println("second sumID: "+shipment.getSumForPayIdentity()));
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        secondSumPaymentsByTheEndOfThisMonth = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfSecondPartSum() ||shipment.getActualPaymentDateOfSecondPartSum().equals("")).collect(Collectors.toList());
        secondSumPaymentsByTheEndOfThisMonth = secondSumPaymentsByTheEndOfThisMonth.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfSecondPartSum() && !shipment.getPlannedPaymentDateOfSecondPartSum().equals("")).collect(Collectors.toList());
        secondSumPaymentsByTheEndOfThisMonth = secondSumPaymentsByTheEndOfThisMonth.stream().filter(shipment -> {
            try {
                return 0 <= df.parse(getLastDateOfCurrentMonth()).compareTo(df.parse(shipment.getPlannedPaymentDateOfSecondPartSum()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        secondSumPaymentsByTheEndOfThisMonth.forEach(shipment -> shipment.setSumForPayIdentity("second"));
        return secondSumPaymentsByTheEndOfThisMonth;
    }

    public List <Shipment> secondSumPaymentsByTheEndOfNextWeek(){
//       List <Shipment> secondSumPaymentsByTheEndOfNextWeek = new ArrayList<>();
        List <Shipment> secondSumPaymentsByTheEndOfNextWeek;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
//        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        secondSumPaymentsByTheEndOfNextWeek = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfSecondPartSum() || shipment.getActualPaymentDateOfSecondPartSum().equals("")).collect(Collectors.toList());
        secondSumPaymentsByTheEndOfNextWeek = secondSumPaymentsByTheEndOfNextWeek.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfSecondPartSum() && !shipment.getPlannedPaymentDateOfSecondPartSum().equals("")).collect(Collectors.toList());
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

    public List <Shipment> secondSumPaymentsByTheEndOfNextMonth(){
//       List <Shipment> secondSumPaymentsByTheEndOfNextMonth = new ArrayList<>();
        List <Shipment> secondSumPaymentsByTheEndOfNextMonth;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
//        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        secondSumPaymentsByTheEndOfNextMonth = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfSecondPartSum() || shipment.getActualPaymentDateOfSecondPartSum().equals("")).collect(Collectors.toList());
        secondSumPaymentsByTheEndOfNextMonth = secondSumPaymentsByTheEndOfNextMonth.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfSecondPartSum() && !shipment.getPlannedPaymentDateOfSecondPartSum().equals("")).collect(Collectors.toList());
        secondSumPaymentsByTheEndOfNextMonth = secondSumPaymentsByTheEndOfNextMonth.stream().filter(shipment -> {
            try {
                return 0 <= df.parse(getLastDateOfNextMonth()).compareTo(df.parse(shipment.getPlannedPaymentDateOfSecondPartSum()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        secondSumPaymentsByTheEndOfNextMonth.forEach(shipment -> shipment.setSumForPayIdentity("second"));
        return secondSumPaymentsByTheEndOfNextMonth;
    }

    public List <Shipment> secondSumPlannedPayments(){
//        List <Shipment> secondSumPaymentsByTheEndOfThisWeek = new ArrayList<>();
        List <Shipment> secondSumPlannedPayments;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
//        unpaidShipments.forEach(shipment -> System.out.println("second sumID: "+shipment.getSumForPayIdentity()));
//        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
//        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        secondSumPlannedPayments = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfSecondPartSum() ||shipment.getActualPaymentDateOfSecondPartSum().equals("")).collect(Collectors.toList());
        secondSumPlannedPayments = secondSumPlannedPayments.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfSecondPartSum() && !shipment.getPlannedPaymentDateOfSecondPartSum().equals("")).collect(Collectors.toList());
        secondSumPlannedPayments.forEach(shipment -> shipment.setSumForPayIdentity("second"));
        return secondSumPlannedPayments;
    }

    public List <Shipment> secondSumOverduePayments(){
//       List <Shipment> secondSumOverduePayments = new ArrayList<>();
        List <Shipment> secondSumOverduePayments;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
//        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        secondSumOverduePayments = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfSecondPartSum() || shipment.getActualPaymentDateOfSecondPartSum().equals("")).collect(Collectors.toList());
        secondSumOverduePayments = secondSumOverduePayments.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfSecondPartSum() && !shipment.getPlannedPaymentDateOfSecondPartSum().equals("")).collect(Collectors.toList());
        secondSumOverduePayments = secondSumOverduePayments.stream().filter(shipment -> {
            try {
                return 0 < df.parse(getTodayDate()).compareTo(df.parse(shipment.getPlannedPaymentDateOfSecondPartSum()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        secondSumOverduePayments.forEach(shipment -> shipment.setSumForPayIdentity("second"));
        return secondSumOverduePayments;
    }

    public List <Shipment> wholeSumPaymentsByTheEndOfThisWeek(){
//        List <Shipment> wholeSumPaymentsByTheEndOfThisWeek = new ArrayList<>();
        List <Shipment> wholeSumPaymentsByTheEndOfThisWeek;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
//        unpaidShipments.forEach(shipment -> System.out.println("whole sumID: "+shipment.getSumForPayIdentity()));
//        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        wholeSumPaymentsByTheEndOfThisWeek = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfWholeSum() ||shipment.getActualPaymentDateOfWholeSum().equals("")).collect(Collectors.toList());
        wholeSumPaymentsByTheEndOfThisWeek = wholeSumPaymentsByTheEndOfThisWeek.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfWholeSum() && !shipment.getPlannedPaymentDateOfWholeSum().equals("")).collect(Collectors.toList());
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

    public List <Shipment> wholeSumPaymentsByTheEndOfThisMonth (){
//        List <Shipment> wholeSumPaymentsByTheEndOfThisMonth = new ArrayList<>();
        List <Shipment> wholeSumPaymentsByTheEndOfThisMonth;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
//        unpaidShipments.forEach(shipment -> System.out.println("whole sumID: "+shipment.getSumForPayIdentity()));
//        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        wholeSumPaymentsByTheEndOfThisMonth = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfWholeSum() ||shipment.getActualPaymentDateOfWholeSum().equals("")).collect(Collectors.toList());
        wholeSumPaymentsByTheEndOfThisMonth = wholeSumPaymentsByTheEndOfThisMonth.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfWholeSum() && !shipment.getPlannedPaymentDateOfWholeSum().equals("")).collect(Collectors.toList());
        wholeSumPaymentsByTheEndOfThisMonth = wholeSumPaymentsByTheEndOfThisMonth.stream().filter(shipment -> {
            try {
                return 0 <= df.parse(getLastDateOfCurrentMonth()).compareTo(df.parse(shipment.getPlannedPaymentDateOfWholeSum()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        wholeSumPaymentsByTheEndOfThisMonth.forEach(shipment -> shipment.setSumForPayIdentity("whole"));
        return wholeSumPaymentsByTheEndOfThisMonth;
    }

    public List <Shipment> wholeSumPaymentsByTheEndOfNextWeek(){
//        List <Shipment> wholeSumPaymentsByTheEndOfNextWeek = new ArrayList<>();
        List <Shipment> wholeSumPaymentsByTheEndOfNextWeek;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
//        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        wholeSumPaymentsByTheEndOfNextWeek = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfWholeSum() ||shipment.getActualPaymentDateOfWholeSum().equals("")).collect(Collectors.toList());
        wholeSumPaymentsByTheEndOfNextWeek = wholeSumPaymentsByTheEndOfNextWeek.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfWholeSum() && !shipment.getPlannedPaymentDateOfWholeSum().equals("")).collect(Collectors.toList());
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

    public List <Shipment> wholeSumPaymentsByTheEndOfNextMonth (){
//        List <Shipment> wholeSumPaymentsByTheEndOfNextMonth = new ArrayList<>();
        List <Shipment> wholeSumPaymentsByTheEndOfNextMonth;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
//        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        wholeSumPaymentsByTheEndOfNextMonth = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfWholeSum() ||shipment.getActualPaymentDateOfWholeSum().equals("")).collect(Collectors.toList());
        wholeSumPaymentsByTheEndOfNextMonth = wholeSumPaymentsByTheEndOfNextMonth.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfWholeSum() && !shipment.getPlannedPaymentDateOfWholeSum().equals("")).collect(Collectors.toList());
        wholeSumPaymentsByTheEndOfNextMonth = wholeSumPaymentsByTheEndOfNextMonth.stream().filter(shipment -> {
            try {
                return 0 <= df.parse(getLastDateOfNextMonth()).compareTo(df.parse(shipment.getPlannedPaymentDateOfWholeSum()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        wholeSumPaymentsByTheEndOfNextMonth.forEach(shipment -> shipment.setSumForPayIdentity("whole"));
        return wholeSumPaymentsByTheEndOfNextMonth;
    }

    public List <Shipment> wholeSumOverduePayments (){
//        List <Shipment> wholeSumOverduePayments = new ArrayList<>();
        List <Shipment> wholeSumOverduePayments;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
//        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        wholeSumOverduePayments = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfWholeSum() ||shipment.getActualPaymentDateOfWholeSum().equals("")).collect(Collectors.toList());
        wholeSumOverduePayments = wholeSumOverduePayments.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfWholeSum() && !shipment.getPlannedPaymentDateOfWholeSum().equals("")).collect(Collectors.toList());
        wholeSumOverduePayments = wholeSumOverduePayments.stream().filter(shipment -> {
            try {
                return 0 < df.parse(getTodayDate()).compareTo(df.parse(shipment.getPlannedPaymentDateOfWholeSum()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).collect(Collectors.toList());
        wholeSumOverduePayments.forEach(shipment -> shipment.setSumForPayIdentity("whole"));
        return wholeSumOverduePayments;
    }

    public List <Shipment> wholeSumPlannedPayments (){
//        List <Shipment> wholeSumPaymentsByTheEndOfThisWeek = new ArrayList<>();
        List <Shipment> wholeSumPlannedPayments;
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
//        unpaidShipments.forEach(shipment -> System.out.println("whole sumID: "+shipment.getSumForPayIdentity()));
//        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        wholeSumPlannedPayments = unpaidShipments.stream().filter(shipment -> null == shipment.getActualPaymentDateOfWholeSum() ||shipment.getActualPaymentDateOfWholeSum().equals("")).collect(Collectors.toList());
        wholeSumPlannedPayments = wholeSumPlannedPayments.stream().filter(shipment -> null != shipment.getPlannedPaymentDateOfWholeSum() && !shipment.getPlannedPaymentDateOfWholeSum().equals("")).collect(Collectors.toList());
        wholeSumPlannedPayments.forEach(shipment -> shipment.setSumForPayIdentity("whole"));
        return wholeSumPlannedPayments;
    }


    @Override
    public List<Shipment> thisMonthShipments() {
        List <Shipment> thisMonthShipments = new ArrayList<>();
        String [] allDatesOfMonth = getAllDatesOfCurrentMonth();
        for (int i=0;i<allDatesOfMonth.length;i++){
            thisMonthShipments.addAll(shipmentRepository.findByPlannedLoadingDate(allDatesOfMonth[i]));
        }
        return thisMonthShipments;
    }

    @Override
    public List<Shipment> nextMonthShipments() {
        List <Shipment> nextMonthShipments = new ArrayList<>();
        String [] allDatesOfMonth = getAllDatesOfNextMonth();
        for (int i=0;i<allDatesOfMonth.length;i++){
            nextMonthShipments.addAll(shipmentRepository.findByPlannedLoadingDate(allDatesOfMonth[i]));
        }
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
    public List<Shipment> shipmentsWithoutPlannedLoadingDate() {
        return shipmentRepository.findShipmentsWithoutPlannedLoadingDate();
    }

    @Override
    public List<Shipment> notLoadedAndWithoutAnyPaymentShipments() {
        List <Shipment> notLoadedAndWithoutAnyPaymentShipments=shipmentRepository.findNotLoadedShipments();
        notLoadedAndWithoutAnyPaymentShipments.removeAll(shipmentRepository.findFirstPartSumPaidShipments());
        notLoadedAndWithoutAnyPaymentShipments.removeAll(shipmentRepository.findSecondPartSumPaidShipments());
        notLoadedAndWithoutAnyPaymentShipments.removeAll(shipmentRepository.findPaidShipments());
        return notLoadedAndWithoutAnyPaymentShipments;
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

    @Override
    public BigDecimal getTotalSumOfAllInvoicesInEUR () {
        List <BigDecimal> invoiceWholeSums = new ArrayList<>();
        List <Shipment> allShipments = shipmentRepository.findAll();
        allShipments
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> invoiceWholeSums.add(shipment.getInvoiceWholeSum()));
        return invoiceWholeSums
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfPaidInvoicesInEUR () {
        List <BigDecimal> invoiceWholeSums = new ArrayList<>();
        List <Shipment> paidShipments = shipmentRepository.findPaidShipments();
        paidShipments
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> invoiceWholeSums.add(shipment.getInvoiceWholeSum()));
        return invoiceWholeSums
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfUnpaidInvoicesInEUR () {
        List <BigDecimal> invoiceUnpaidSums = new ArrayList<>();
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
        unpaidShipments
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> invoiceUnpaidSums.add(shipment.getUnpaidSum()));
        return invoiceUnpaidSums
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllPaymentsByTheEndOfThisWeekInEUR () {
        List <BigDecimal> allPaymentsByTheEndOfThisWeekInEUR = new ArrayList<>();
        firstSumPaymentsByTheEndOfThisWeek()
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisWeekInEUR.add(shipment.getInvoiceFirstPartSum()));
        secondSumPaymentsByTheEndOfThisWeek()
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisWeekInEUR.add(shipment.getInvoiceSecondPartSum()));
        wholeSumPaymentsByTheEndOfThisWeek()
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisWeekInEUR.add(shipment.getInvoiceWholeSum()));
        return allPaymentsByTheEndOfThisWeekInEUR
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllPaymentsByTheEndOfThisWeekInUSD () {
        List <BigDecimal> allPaymentsByTheEndOfThisWeekInUSD = new ArrayList<>();
        firstSumPaymentsByTheEndOfThisWeek()
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisWeekInUSD.add(shipment.getInvoiceFirstPartSum()));
        secondSumPaymentsByTheEndOfThisWeek()
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisWeekInUSD.add(shipment.getInvoiceSecondPartSum()));
        wholeSumPaymentsByTheEndOfThisWeek()
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisWeekInUSD.add(shipment.getInvoiceWholeSum()));
        return allPaymentsByTheEndOfThisWeekInUSD
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllPaymentsByTheEndOfThisWeekInUAH () {
        List <BigDecimal> allPaymentsByTheEndOfThisWeekInUAH = new ArrayList<>();
        firstSumPaymentsByTheEndOfThisWeek()
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisWeekInUAH.add(shipment.getInvoiceSecondPartSum()));
        secondSumPaymentsByTheEndOfThisWeek()
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisWeekInUAH .add(shipment.getInvoiceSecondPartSum()));
        wholeSumPaymentsByTheEndOfThisWeek()
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisWeekInUAH.add(shipment.getInvoiceWholeSum()));
        return allPaymentsByTheEndOfThisWeekInUAH
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllPaymentsByTheEndOfThisMonthInEUR () {
        List <BigDecimal> allPaymentsByTheEndOfThisMonthInEUR = new ArrayList<>();
        firstSumPaymentsByTheEndOfThisMonth()
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisMonthInEUR.add(shipment.getInvoiceFirstPartSum()));
        secondSumPaymentsByTheEndOfThisMonth()
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisMonthInEUR.add(shipment.getInvoiceSecondPartSum()));
        wholeSumPaymentsByTheEndOfThisMonth()
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisMonthInEUR.add(shipment.getInvoiceWholeSum()));
        return allPaymentsByTheEndOfThisMonthInEUR
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllPaymentsByTheEndOfThisMonthInUSD () {
        List <BigDecimal> allPaymentsByTheEndOfThisMonthInUSD = new ArrayList<>();
        firstSumPaymentsByTheEndOfThisMonth()
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisMonthInUSD.add(shipment.getInvoiceFirstPartSum()));
        secondSumPaymentsByTheEndOfThisMonth()
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisMonthInUSD.add(shipment.getInvoiceSecondPartSum()));
        wholeSumPaymentsByTheEndOfThisMonth()
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisMonthInUSD.add(shipment.getInvoiceWholeSum()));
        return allPaymentsByTheEndOfThisMonthInUSD
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllPaymentsByTheEndOfThisMonthInUAH () {
        List <BigDecimal> allPaymentsByTheEndOfThisMonthInUAH = new ArrayList<>();
        firstSumPaymentsByTheEndOfThisMonth()
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisMonthInUAH.add(shipment.getInvoiceFirstPartSum()));
        secondSumPaymentsByTheEndOfThisMonth()
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisMonthInUAH.add(shipment.getInvoiceSecondPartSum()));
        wholeSumPaymentsByTheEndOfThisMonth()
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfThisMonthInUAH.add(shipment.getInvoiceWholeSum()));
        return allPaymentsByTheEndOfThisMonthInUAH
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllPaymentsByTheEndOfNextWeekInEUR () {

        List <BigDecimal> allPaymentsByTheEndOfNextWeekInEUR = new ArrayList<>();
        firstSumPaymentsByTheEndOfNextWeek()
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextWeekInEUR.add(shipment.getInvoiceFirstPartSum()));
        secondSumPaymentsByTheEndOfNextWeek()
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextWeekInEUR.add(shipment.getInvoiceSecondPartSum()));
        wholeSumPaymentsByTheEndOfNextWeek()
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextWeekInEUR.add(shipment.getInvoiceWholeSum()));

        return allPaymentsByTheEndOfNextWeekInEUR
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllPaymentsByTheEndOfNextWeekInUSD () {
        List <BigDecimal> allPaymentsByTheEndOfNextWeekInUSD = new ArrayList<>();
        firstSumPaymentsByTheEndOfNextWeek()
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextWeekInUSD.add(shipment.getInvoiceFirstPartSum()));
        secondSumPaymentsByTheEndOfNextWeek()
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextWeekInUSD.add(shipment.getInvoiceSecondPartSum()));
        wholeSumPaymentsByTheEndOfNextWeek()
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextWeekInUSD.add(shipment.getInvoiceWholeSum()));
        return allPaymentsByTheEndOfNextWeekInUSD
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllPaymentsByTheEndOfNextWeekInUAH () {
        List <BigDecimal> allPaymentsByTheEndOfNextWeekInUAH = new ArrayList<>();
        firstSumPaymentsByTheEndOfNextWeek()
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextWeekInUAH.add(shipment.getInvoiceFirstPartSum()));
        secondSumPaymentsByTheEndOfNextWeek()
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextWeekInUAH.add(shipment.getInvoiceSecondPartSum()));
        wholeSumPaymentsByTheEndOfNextWeek()
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextWeekInUAH.add(shipment.getInvoiceWholeSum()));
        return allPaymentsByTheEndOfNextWeekInUAH
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllPaymentsByTheEndOfNextMonthInEUR () {
        List <BigDecimal> allPaymentsByTheEndOfNextMonthInEUR = new ArrayList<>();
        firstSumPaymentsByTheEndOfNextMonth()
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextMonthInEUR.add(shipment.getInvoiceFirstPartSum()));
        secondSumPaymentsByTheEndOfNextMonth()
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextMonthInEUR.add(shipment.getInvoiceSecondPartSum()));
        wholeSumPaymentsByTheEndOfNextMonth()
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextMonthInEUR.add(shipment.getInvoiceWholeSum()));
        return allPaymentsByTheEndOfNextMonthInEUR
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllPaymentsByTheEndOfNextMonthInUSD () {
        List <BigDecimal> allPaymentsByTheEndOfNextMonthInUSD = new ArrayList<>();
        firstSumPaymentsByTheEndOfNextMonth()
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextMonthInUSD.add(shipment.getInvoiceFirstPartSum()));
        secondSumPaymentsByTheEndOfNextMonth()
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextMonthInUSD.add(shipment.getInvoiceSecondPartSum()));
        wholeSumPaymentsByTheEndOfNextMonth()
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextMonthInUSD.add(shipment.getInvoiceWholeSum()));
        return allPaymentsByTheEndOfNextMonthInUSD
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllPaymentsByTheEndOfNextMonthInUAH () {
        List <BigDecimal> allPaymentsByTheEndOfNextMonthInUAH = new ArrayList<>();
        firstSumPaymentsByTheEndOfNextMonth()
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextMonthInUAH.add(shipment.getInvoiceFirstPartSum()));
        secondSumPaymentsByTheEndOfNextMonth()
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextMonthInUAH.add(shipment.getInvoiceSecondPartSum()));
        wholeSumPaymentsByTheEndOfNextMonth()
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> allPaymentsByTheEndOfNextMonthInUAH.add(shipment.getInvoiceWholeSum()));
        return allPaymentsByTheEndOfNextMonthInUAH
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllPlannedPaymentsInEUR () {
        List <BigDecimal> allPlannedPaymentsInEUR = new ArrayList<>();
        allPlannedPayments()
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> allPlannedPaymentsInEUR.add(shipment.getUnpaidSum()));
        return allPlannedPaymentsInEUR
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllPlannedPaymentsInUSD () {
        List <BigDecimal> allPlannedPaymentsInUSD = new ArrayList<>();
        allPlannedPayments()
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> allPlannedPaymentsInUSD.add(shipment.getUnpaidSum()));
        return allPlannedPaymentsInUSD
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllPlannedPaymentsInUAH() {
        List <BigDecimal> allPlannedPaymentsInUAH = new ArrayList<>();
        allPlannedPayments()
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> allPlannedPaymentsInUAH.add(shipment.getUnpaidSum()));
        return allPlannedPaymentsInUAH
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllOverduePaymentsInEUR () {
        List <BigDecimal> allOverduePaymentsInEUR = new ArrayList<>();
        allOverduePayments()
                .stream()
                .filter(shipment -> "EUR".equals(shipment.getCurrency()))
                .forEach(shipment -> allOverduePaymentsInEUR.add(shipment.getUnpaidSum()));
        return allOverduePaymentsInEUR
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllOverduePaymentsInUSD () {
        List <BigDecimal> allOverduePaymentsInUSD = new ArrayList<>();
        allOverduePayments()
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> allOverduePaymentsInUSD.add(shipment.getUnpaidSum()));
        return allOverduePaymentsInUSD
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllOverduePaymentsInUAH() {
        List <BigDecimal> allOverduePaymentsInUAH = new ArrayList<>();
        allOverduePayments()
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> allOverduePaymentsInUAH.add(shipment.getUnpaidSum()));
        return allOverduePaymentsInUAH
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllInvoicesInUSD () {
        List <BigDecimal> invoiceWholeSums = new ArrayList<>();
        List <Shipment> allShipments = shipmentRepository.findAll();
        allShipments
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> invoiceWholeSums.add(shipment.getInvoiceWholeSum()));
        return invoiceWholeSums
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfPaidInvoicesInUSD() {
        List <BigDecimal> invoiceWholeSums = new ArrayList<>();
        List <Shipment> paidShipments = shipmentRepository.findPaidShipments();
        paidShipments
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> invoiceWholeSums.add(shipment.getInvoiceWholeSum()));
        return invoiceWholeSums
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfUnpaidInvoicesInUSD () {
        List <BigDecimal> invoiceUnpaidSums = new ArrayList<>();
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
        unpaidShipments
                .stream()
                .filter(shipment -> "USD".equals(shipment.getCurrency()))
                .forEach(shipment -> invoiceUnpaidSums.add(shipment.getUnpaidSum()));
        return invoiceUnpaidSums
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfAllInvoicesInUAH() {
        List <BigDecimal> invoiceWholeSums = new ArrayList<>();
        List <Shipment> allShipments = shipmentRepository.findAll();
        allShipments
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> invoiceWholeSums.add(shipment.getInvoiceWholeSum()));
        return invoiceWholeSums
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfPaidInvoicesInUAH () {
        List <BigDecimal> invoiceWholeSums = new ArrayList<>();
        List <Shipment> paidShipments = shipmentRepository.findPaidShipments();
        paidShipments
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> invoiceWholeSums.add(shipment.getInvoiceWholeSum()));
        return invoiceWholeSums
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalSumOfUnpaidInvoicesInUAH () {
        List <BigDecimal> invoiceUnpaidSums = new ArrayList<>();
        List <Shipment> unpaidShipments = shipmentRepository.findUnpaidShipments();
        unpaidShipments
                .stream()
                .filter(shipment -> "UAH".equals(shipment.getCurrency()))
                .forEach(shipment -> invoiceUnpaidSums.add(shipment.getUnpaidSum()));
        return invoiceUnpaidSums
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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

    private String getTodayDate () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        Date today = new Date();
        calendar.setTime(today);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(calendar.getTime());
    }

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

    private String getLastDateOfCurrentMonth  () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
//        System.out.println("last date of current month is: "+calendar.getTime());
        return df.format(calendar.getTime());
    }

    private String getFirstDateOfCurrentMonth() {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
//        System.out.println("first date of current month is: "+calendar.getTime());
        return df.format(calendar.getTime());
    }

    private String getFirstDateOfNextMonth () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return df.format(calendar.getTime());
    }

    private String getLastDateOfNextMonth () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
//        System.out.println("last date of next month is: "+calendar.getTime());
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
