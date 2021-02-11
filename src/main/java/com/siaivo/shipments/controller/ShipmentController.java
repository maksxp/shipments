package com.siaivo.shipments.controller;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Shipment;
import com.siaivo.shipments.service.ContractService;
import com.siaivo.shipments.service.ProductForShipmentService;
import com.siaivo.shipments.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

@Controller
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ProductForShipmentService productForShipmentService;

    @RequestMapping(value="/salesSupport/allShipments", method = RequestMethod.GET)
    public ModelAndView allShipmentsForSalesSupport(){
        ModelAndView modelAndView = getModelAndViewWithAllShipments();
        modelAndView.setViewName("/salesSupport/allShipments");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/openShipments", method = RequestMethod.GET)
    public ModelAndView openShipmentsForSalesSupport(){
        ModelAndView modelAndView = getModelAndViewWithOpenShipments();
        modelAndView.setViewName("/salesSupport/openShipments");
        return modelAndView;
    }

    @RequestMapping(value = "/salesSupport/fulfillShipment/{id}", method = RequestMethod.GET)
    public ModelAndView fulfillShipmentForSalesSupport(@PathVariable(value = "id") int id){
        Shipment shipment =  shipmentService.findById(id);
        shipmentService.fulfillShipment(shipment);
        return new ModelAndView("redirect:/salesSupport/openShipments");
    }

    @RequestMapping(value = "/salesSupport/fulfillShipment", method = RequestMethod.POST)
    public ModelAndView fulfillShipmentForSalesSupport (@ModelAttribute("shipment")Shipment shipment) {
        shipmentService.fulfillShipment(shipment);
        return new ModelAndView("redirect:/salesSupport/openShipments");
    }

    @RequestMapping(value="/salesSupport/fulfilledShipments", method = RequestMethod.GET)
    public ModelAndView fulfilledShipmentsForSalesSupport(){
        ModelAndView modelAndView = getModelAndViewWithFulfilledShipments();
        modelAndView.setViewName("/salesSupport/fulfilledShipments");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/thisWeekShipments", method = RequestMethod.GET)
    public ModelAndView thisWeekShipmentsForSalesSupport(){
        ModelAndView modelAndView = getModelAndViewWithThisWeekShipments();
        modelAndView.setViewName("/salesSupport/thisWeekShipments");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/nextWeekShipments", method = RequestMethod.GET)
    public ModelAndView nextWeekShipmentsForSalesSupport(){
        ModelAndView modelAndView = getModelAndViewWithNextWeekShipments();
        modelAndView.setViewName("/salesSupport/nextWeekShipments");
        return modelAndView;
    }

    @RequestMapping(value="/salesManagement/allShipments", method = RequestMethod.GET)
    public ModelAndView allShipmentsForSalesManagement(){
        ModelAndView modelAndView = getModelAndViewWithAllShipments();
        modelAndView.setViewName("/salesManagement/allShipments");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/allShipmentsPerContract/{id}", method = RequestMethod.GET)
    public ModelAndView allShipmentsPerContractForSalesSupport(@PathVariable(value = "id") int id){
        Contract contract = contractService.findContractById(id);
        ModelAndView modelAndView = getModelAndViewWithAllShipmentsPerContract(contract);
        modelAndView.setViewName("/salesSupport/allShipmentsPerContract");
        return modelAndView;
    }

    @RequestMapping(value="/salesManagement/allShipmentsPerContract/{id}", method = RequestMethod.GET)
    public ModelAndView allShipmentsPerContractForSalesManagement(@PathVariable(value = "id") int id){
        Contract contract = contractService.findContractById(id);
        ModelAndView modelAndView = getModelAndViewWithAllShipmentsPerContract(contract);
        modelAndView.setViewName("/salesManagement/allShipmentsPerContract");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/shipment/{id}", method = RequestMethod.GET)
    public ModelAndView shipmentForSalesSupport(@PathVariable(value = "id") int id){
        ModelAndView modelAndView = new ModelAndView();
        Shipment shipment = shipmentService.findById(id);
        modelAndView.addObject("contractNumber", shipmentService.findById(id).getContract().getContractNumber());
        modelAndView.addObject("allProductsForShipment", productForShipmentService.findByShipment(shipment));
        modelAndView.addObject("contractDate", shipmentService.findById(id).getContract().getContractDate());
        modelAndView.addObject("customerName", shipmentService.findById(id).getContract().getCustomer().getCustomerName());
        modelAndView.addObject("paymentTerms", shipmentService.findById(id).getContract().getPaymentTerms());
        modelAndView.addObject("deliveryTerms", shipmentService.findById(id).getContract().getDeliveryTerms());
        modelAndView.addObject("invoiceWholeSum", shipment.getInvoiceWholeSum(shipment));
        if (shipment.getContract().getPaymentTerms().equals("оплата частинами")) {
            modelAndView.addObject("strippedInvoiceFirstPartSum", shipment.getInvoiceFirstPartSum().stripTrailingZeros().toPlainString());
            modelAndView.addObject("strippedInvoiceSecondPartSum", shipment.getInvoiceSecondPartSum().stripTrailingZeros().toPlainString());
        } else {
            modelAndView.addObject("strippedInvoiceFirstPartSum", BigDecimal.ZERO.stripTrailingZeros().toPlainString());
            modelAndView.addObject("strippedInvoiceSecondPartSum", BigDecimal.ZERO.stripTrailingZeros().toPlainString());
        }
        modelAndView.addObject("shipment", shipment);
        modelAndView.setViewName("/salesSupport/shipment");
        return modelAndView;
    }

    @RequestMapping(value="/salesManagement/shipment/{id}", method = RequestMethod.GET)
    public ModelAndView shipmentForSalesManagement(@PathVariable(value = "id") int id){
        ModelAndView modelAndView = new ModelAndView();
        Shipment shipment = shipmentService.findById(id);
        modelAndView.addObject("contractNumber", shipmentService.findById(id).getContract().getContractNumber());
        modelAndView.addObject("allProductsForShipment", productForShipmentService.findByShipment(shipment));
        modelAndView.addObject("contractDate", shipmentService.findById(id).getContract().getContractDate());
        modelAndView.addObject("customerName", shipmentService.findById(id).getContract().getCustomer().getCustomerName());
        modelAndView.addObject("paymentTerms", shipmentService.findById(id).getContract().getPaymentTerms());
        modelAndView.addObject("deliveryTerms", shipmentService.findById(id).getContract().getDeliveryTerms());
        modelAndView.addObject("invoiceWholeSum", shipment.getInvoiceWholeSum(shipment));
        if (shipment.getContract().getPaymentTerms().equals("оплата частинами")) {
            modelAndView.addObject("strippedInvoiceFirstPartSum", shipment.getInvoiceFirstPartSum().stripTrailingZeros().toPlainString());
            modelAndView.addObject("strippedInvoiceSecondPartSum", shipment.getInvoiceSecondPartSum().stripTrailingZeros().toPlainString());
        } else {
            modelAndView.addObject("strippedInvoiceFirstPartSum", BigDecimal.ZERO.stripTrailingZeros().toPlainString());
            modelAndView.addObject("strippedInvoiceSecondPartSum", BigDecimal.ZERO.stripTrailingZeros().toPlainString());
        }
        modelAndView.addObject("shipment", shipment);
        modelAndView.setViewName("/salesManagement/shipment");
        return modelAndView;
    }

    @Transactional
    @RequestMapping(value="/salesSupport/shipment", method = RequestMethod.POST)
    public ModelAndView shipmentForSalesSupport (@ModelAttribute("shipment")Shipment shipmentFromView, @RequestParam(value="productForShipmentWeight[]") List <BigDecimal> productsForShipmentWeight){
          int id = shipmentFromView.getId();
          Shipment shipmentFromDataBase = shipmentService.findById(id);
          for (int i=0; i<productsForShipmentWeight.size(); i++){
              shipmentFromDataBase.getProductsForShipment().get(i).setQuantity(productsForShipmentWeight.get(i));
              productForShipmentService.saveProductForShipment(shipmentFromDataBase.getProductsForShipment().get(i));
          }
          shipmentFromDataBase.setDestinationCountry(shipmentFromView.getDestinationCountry());
          shipmentFromDataBase.setDestinationPlace(shipmentFromView.getDestinationPlace());
          shipmentFromDataBase.setPlannedLoadingDate(shipmentFromView.getPlannedLoadingDate());
          shipmentFromDataBase.setActualLoadingDate(shipmentFromView.getActualLoadingDate());
          shipmentFromDataBase.setPlannedUnloadingDate(shipmentFromView.getPlannedUnloadingDate());
          shipmentFromDataBase.setActualUnloadingDate(shipmentFromView.getActualUnloadingDate());
          shipmentFromDataBase.setPlannedPaymentDateOfFirstPartSum(shipmentFromView.getPlannedPaymentDateOfFirstPartSum());
          shipmentFromDataBase.setActualPaymentDateOfFirstPartSum(shipmentFromView.getActualPaymentDateOfFirstPartSum());
          shipmentFromDataBase.setPlannedPaymentDateOfSecondPartSum(shipmentFromView.getPlannedPaymentDateOfSecondPartSum());
          shipmentFromDataBase.setActualPaymentDateOfSecondPartSum(shipmentFromView.getActualPaymentDateOfSecondPartSum());
          shipmentFromDataBase.setPlannedPaymentDateOfWholeSum(shipmentFromView.getPlannedPaymentDateOfWholeSum());
          shipmentFromDataBase.setActualPaymentDateOfWholeSum(shipmentFromView.getActualPaymentDateOfWholeSum());
          shipmentFromDataBase.setShipmentComment(shipmentFromView.getShipmentComment());
          shipmentFromDataBase.setLabelsStatus(shipmentFromView.getLabelsStatus());
          shipmentFromDataBase.setLogisticInstructionStatus(shipmentFromView.getLogisticInstructionStatus());
          shipmentFromDataBase.setInvoiceComment(shipmentFromView.getInvoiceComment());
          shipmentFromDataBase.setInvoiceFirstPartSum(shipmentFromView.getInvoiceFirstPartSum());
          shipmentFromDataBase.setInvoiceSecondPartSum(shipmentFromView.getInvoiceSecondPartSum());
          shipmentService.saveShipment(shipmentFromDataBase);
//        Field [] fields = shipmentFromView.getClass().getDeclaredFields();
//          Arrays.stream(fields).forEach(field -> field.setAccessible(true));
//        Field [] fields1 = shipmentFromDataBase.getClass().getDeclaredFields();
//        Arrays.stream(fields1).forEach(field -> field.setAccessible(true));
//          Arrays.stream(fields).forEach(field -> {
//              try {
//                  System.out.println(field.getName()+": "+field.get(shipmentFromView));
//              } catch (IllegalAccessException e) {
//                  e.printStackTrace();
//              }
//          });
//        Arrays.stream(fields1).forEach(field -> {
//            try {
//                System.out.println(field.getName()+"11: "+field.get(shipmentFromDataBase));
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        });

        ModelAndView modelAndView = getModelAndViewWithAllShipmentsPerContract(shipmentFromDataBase.getContract());
        modelAndView.setViewName("/salesSupport/allShipmentsPerContract");
        return modelAndView;
    }

    @Transactional
    @RequestMapping(value="/salesManagement/shipment", method = RequestMethod.POST)
    public ModelAndView shipmentForSalesManagement (@ModelAttribute("shipment")Shipment shipmentFromView, @RequestParam(value="productForShipmentWeight[]") List <BigDecimal> productsForShipmentWeight){
        int id = shipmentFromView.getId();
        Shipment shipmentFromDataBase = shipmentService.findById(id);
        for (int i=0; i<productsForShipmentWeight.size(); i++){
            shipmentFromDataBase.getProductsForShipment().get(i).setQuantity(productsForShipmentWeight.get(i));
            productForShipmentService.saveProductForShipment(shipmentFromDataBase.getProductsForShipment().get(i));
        }
        shipmentFromDataBase.setPlannedLoadingDate(shipmentFromView.getPlannedLoadingDate());
        shipmentFromDataBase.setActualLoadingDate(shipmentFromView.getActualLoadingDate());
        shipmentFromDataBase.setPlannedUnloadingDate(shipmentFromView.getPlannedUnloadingDate());
        shipmentFromDataBase.setActualUnloadingDate(shipmentFromView.getActualUnloadingDate());
        shipmentFromDataBase.setPlannedPaymentDateOfFirstPartSum(shipmentFromView.getPlannedPaymentDateOfFirstPartSum());
        shipmentFromDataBase.setActualPaymentDateOfFirstPartSum(shipmentFromView.getActualPaymentDateOfFirstPartSum());
        shipmentFromDataBase.setPlannedPaymentDateOfSecondPartSum(shipmentFromView.getPlannedPaymentDateOfSecondPartSum());
        shipmentFromDataBase.setActualPaymentDateOfSecondPartSum(shipmentFromView.getActualPaymentDateOfSecondPartSum());
        shipmentFromDataBase.setPlannedPaymentDateOfWholeSum(shipmentFromView.getPlannedPaymentDateOfWholeSum());
        shipmentFromDataBase.setActualPaymentDateOfWholeSum(shipmentFromView.getActualPaymentDateOfWholeSum());
        shipmentFromDataBase.setShipmentComment(shipmentFromView.getShipmentComment());
        shipmentFromDataBase.setLabelsStatus(shipmentFromView.getLabelsStatus());
        shipmentFromDataBase.setLogisticInstructionStatus(shipmentFromView.getLogisticInstructionStatus());
        shipmentFromDataBase.setInvoiceComment(shipmentFromView.getInvoiceComment());
        shipmentFromDataBase.setInvoiceFirstPartSum(shipmentFromView.getInvoiceFirstPartSum());
        shipmentFromDataBase.setInvoiceSecondPartSum(shipmentFromView.getInvoiceSecondPartSum());
        shipmentService.saveShipment(shipmentFromDataBase);
//        Field [] fields = shipmentFromView.getClass().getDeclaredFields();
//          Arrays.stream(fields).forEach(field -> field.setAccessible(true));
//        Field [] fields1 = shipmentFromDataBase.getClass().getDeclaredFields();
//        Arrays.stream(fields1).forEach(field -> field.setAccessible(true));
//          Arrays.stream(fields).forEach(field -> {
//              try {
//                  System.out.println(field.getName()+": "+field.get(shipmentFromView));
//              } catch (IllegalAccessException e) {
//                  e.printStackTrace();
//              }
//          });
//        Arrays.stream(fields1).forEach(field -> {
//            try {
//                System.out.println(field.getName()+"11: "+field.get(shipmentFromDataBase));
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        });

        ModelAndView modelAndView = getModelAndViewWithAllShipmentsPerContract(shipmentFromDataBase.getContract());
        modelAndView.setViewName("/salesManagement/allShipmentsPerContract");
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithAllShipments (){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allShipments", shipmentService.allShipments());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithOpenShipments (){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("openShipments", shipmentService.openShipments());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithFulfilledShipments (){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("fulfilledShipments", shipmentService.fulfilledShipments());
        shipmentService.fulfilledShipments().forEach(shipment -> System.out.println(shipment.getFullSettlementDate()));
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithThisWeekShipments (){
        ModelAndView modelAndView = new ModelAndView();
        int currentWeekNumber = getCurrentWeekNumber();
        modelAndView.addObject("currentWeekNumber", currentWeekNumber);
        modelAndView.addObject("thisWeekShipments", shipmentService.thisWeekShipments());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithNextWeekShipments (){
        ModelAndView modelAndView = new ModelAndView();
        int nextWeekNumber = getNextWeekNumber();
        modelAndView.addObject("nextWeekNumber", nextWeekNumber);
        modelAndView.addObject("nextWeekShipments", shipmentService.nextWeekShipments());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithAllShipmentsPerContract (Contract contract){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("contract", contract);
        modelAndView.addObject("customerName", contract.getCustomer().getCustomerName());
        modelAndView.addObject("allShipmentsPerContract", shipmentService.allShipmentsPerContract(contract));
        return modelAndView;
    }

    private int getCurrentWeekNumber () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        Date today = new Date();
        calendar.setTime(today);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    private int getNextWeekNumber () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        Date today = new Date();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, 7);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

}
