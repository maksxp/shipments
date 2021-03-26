package com.siaivo.shipments.controller.salesSupport;

import com.siaivo.shipments.json.JsonReader;
import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.ProductForShipment;
import com.siaivo.shipments.model.Shipment;
import com.siaivo.shipments.service.ContractService;
import com.siaivo.shipments.service.ProductForShipmentService;
import com.siaivo.shipments.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.*;

@Controller
public class SalesSupportShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ProductForShipmentService productForShipmentService;

    @RequestMapping(value="/salesSupport/shipmentRegistration/{id}", method = RequestMethod.GET)
    public ModelAndView registerNewShipment(@PathVariable(value = "id") int id){
        Contract contract = contractService.findContractById(id);
        String country="";
        String place="";
        if (contract.getShipments().size()>0){
            country = contract.getShipments().get(0).getDestinationCountry();
            place = contract.getShipments().get(0).getDestinationPlace();
        }
        int truckNumber = contract.getShipments().size()+1;
        String invoiceNumber = contract.getContractNumber()+"."+truckNumber;
        ModelAndView modelAndView = createModelAndView ();
        Shipment shipment = new Shipment();
        List <ProductForShipment> allProductsForShipment = new ArrayList<>();
        contract.getProducts().forEach(product -> allProductsForShipment.add(new ProductForShipment(product)));
        modelAndView.addObject("country", country);
        modelAndView.addObject("place", place);
        modelAndView.addObject("truck", truckNumber);
        modelAndView.addObject("invoice", invoiceNumber);
        modelAndView.addObject("contractNumber", contract.getContractNumber());
        modelAndView.addObject("allProductsForShipment", allProductsForShipment);
        modelAndView.addObject("contractDate", contract.getContractDate());
        modelAndView.addObject("customerName", contract.getCustomer().getCustomerName());
        modelAndView.addObject("paymentTerms", contract.getPaymentTerms());
        modelAndView.addObject("deliveryTerms", contract.getDeliveryTerms());
        modelAndView.addObject("shipment", shipment);
        modelAndView.addObject("contractId", id);
        modelAndView.addObject("strippedInvoiceSecondPartSum", BigDecimal.ZERO.stripTrailingZeros().toPlainString());
        modelAndView.addObject("strippedInvoiceFirstPartSum", BigDecimal.ZERO.stripTrailingZeros().toPlainString());
        modelAndView.setViewName("/salesSupport/shipmentRegistration");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/allShipments", method = RequestMethod.GET)
    public ModelAndView allShipments(){
        ModelAndView modelAndView = getModelAndViewWithAllShipments();
        modelAndView.setViewName("/salesSupport/allShipments");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/allPlannedPayments", method = RequestMethod.GET)
    public ModelAndView allPlannedPayments(){
        ModelAndView modelAndView = getModelAndViewWithAllPlannedPayments();
        modelAndView.setViewName("/salesSupport/allPlannedPayments");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/allOverduePayments", method = RequestMethod.GET)
    public ModelAndView allOverduePayments(){
        ModelAndView modelAndView = getModelAndViewWithAllOverduePayments();
        modelAndView.setViewName("/salesSupport/allOverduePayments");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/allPaymentsByTheEndOfThisWeek", method = RequestMethod.GET)
        public ModelAndView paymentsByTheEndOfThisWeek(){
        ModelAndView modelAndView = getModelAndViewWithAllPaymentsByTheEndOfThisWeek();
        modelAndView.setViewName("/salesSupport/allPaymentsByTheEndOfThisWeek");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/allPaymentsByTheEndOfThisMonth", method = RequestMethod.GET)
    public ModelAndView paymentsByTheEndOfThisMonth(){
        ModelAndView modelAndView = getModelAndViewWithAllPaymentsByTheEndOfThisMonth();
        modelAndView.setViewName("/salesSupport/allPaymentsByTheEndOfThisMonth");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/allPaymentsByTheEndOfNextWeek", method = RequestMethod.GET)
    public ModelAndView paymentsByTheEndOfNextWeek(){
        ModelAndView modelAndView = getModelAndViewWithAllPaymentsByTheEndOfNextWeek();
        modelAndView.setViewName("/salesSupport/allPaymentsByTheEndOfNextWeek");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/allPaymentsByTheEndOfNextMonth", method = RequestMethod.GET)
    public ModelAndView paymentsByTheEndOfNextMonth(){
        ModelAndView modelAndView = getModelAndViewWithAllPaymentsByTheEndOfNextMonth();
        modelAndView.setViewName("/salesSupport/allPaymentsByTheEndOfNextMonth");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/allInvoices", method = RequestMethod.GET)
    public ModelAndView allInvoices(){
        ModelAndView modelAndView = getModelAndViewWithAllShipments();
        modelAndView.addObject("totalSumOfAllInvoicesInEUR", shipmentService.getTotalSumOfAllInvoicesInEUR());
        modelAndView.addObject("totalSumOfAllInvoicesInUSD", shipmentService.getTotalSumOfAllInvoicesInUSD());
        modelAndView.addObject("totalSumOfAllInvoicesInUAH", shipmentService.getTotalSumOfAllInvoicesInUAH());
        modelAndView.setViewName("/salesSupport/allInvoices");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/paidInvoices", method = RequestMethod.GET)
    public ModelAndView paidInvoices(){
        ModelAndView modelAndView = getModelAndViewWithPaidShipments();
        modelAndView.addObject("totalSumOfPaidInvoicesInEUR", shipmentService.getTotalSumOfPaidInvoicesInEUR());
        modelAndView.addObject("totalSumOfPaidInvoicesInUSD", shipmentService.getTotalSumOfPaidInvoicesInUSD());
        modelAndView.addObject("totalSumOfPaidInvoicesInUAH", shipmentService.getTotalSumOfPaidInvoicesInUAH());
        modelAndView.setViewName("/salesSupport/paidInvoices");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/unpaidInvoices", method = RequestMethod.GET)
    public ModelAndView unpaidInvoices(){
        ModelAndView modelAndView = getModelAndViewWithUnpaidShipments();
        modelAndView.addObject("totalSumOfUnpaidInvoicesInEUR", shipmentService.getTotalSumOfUnpaidInvoicesInEUR());
        modelAndView.addObject("totalSumOfUnpaidInvoicesInUSD", shipmentService.getTotalSumOfUnpaidInvoicesInUSD());
        modelAndView.addObject("totalSumOfUnpaidInvoicesInUAH", shipmentService.getTotalSumOfUnpaidInvoicesInUAH());
        modelAndView.setViewName("/salesSupport/unpaidInvoices");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/openShipments", method = RequestMethod.GET)
    public ModelAndView openShipments(){
        ModelAndView modelAndView = getModelAndViewWithOpenShipments();
        modelAndView.setViewName("/salesSupport/openShipments");
        return modelAndView;
    }

    @RequestMapping(value = "/salesSupport/fulfillShipment/{id}", method = RequestMethod.GET)
    public ModelAndView fulfillShipment(@PathVariable(value = "id") int id){
        Shipment shipment =  shipmentService.findById(id);
        shipmentService.fulfillShipment(shipment);
        return new ModelAndView("redirect:../openShipments");
    }

    @RequestMapping(value = "/salesSupport/returnShipmentToWork/{id}", method = RequestMethod.GET)
    public ModelAndView returnShipmentToWork(@PathVariable(value = "id") int id){
        Shipment shipment =  shipmentService.findById(id);
        shipmentService.returnShipmentToWork(shipment);
        return new ModelAndView("redirect:../openShipments");
    }

    @RequestMapping(value = "/salesSupport/deleteShipment/{id}", method = RequestMethod.GET)
    public ModelAndView deleteShipment(@PathVariable(value = "id") int id){
        Shipment shipment =  shipmentService.findById(id);
        shipmentService.deleteShipment(shipment);
        Contract contract = shipment.getContract();
        int contractId = contract.getId();
        ModelAndView modelAndView = getModelAndViewWithAllShipmentsPerContract(contract);
        modelAndView.setViewName("redirect:/salesSupport/allShipmentsPerContract/"+contractId);
        return modelAndView;
    }

    @RequestMapping(value = "/salesSupport/returnShipmentToWork", method = RequestMethod.POST)
    public ModelAndView returnShipmentToWork (@ModelAttribute("shipment")Shipment shipment) {
        shipmentService.returnShipmentToWork(shipment);
        return new ModelAndView("redirect:salesSupport/openShipments");
    }

    @RequestMapping(value = "/salesSupport/fulfillShipment", method = RequestMethod.POST)
    public ModelAndView fulfillShipment (@ModelAttribute("shipment")Shipment shipment) {
        shipmentService.fulfillShipment(shipment);
        return new ModelAndView("redirect:salesSupport/openShipments");
    }

    @RequestMapping(value = "/salesSupport/deleteShipment", method = RequestMethod.POST)
    public ModelAndView deleteShipment (@ModelAttribute("shipment")Shipment shipment) {
        shipmentService.deleteShipment(shipment);
        return new ModelAndView("redirect:salesSupport/openShipments");
    }

    @RequestMapping(value="/salesSupport/fulfilledShipments", method = RequestMethod.GET)
    public ModelAndView fulfilledShipments(){
        ModelAndView modelAndView = getModelAndViewWithFulfilledShipments();
        modelAndView.setViewName("/salesSupport/fulfilledShipments");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/thisWeekShipments", method = RequestMethod.GET)
    public ModelAndView thisWeekShipments(){
        ModelAndView modelAndView = getModelAndViewWithThisWeekShipments();
        modelAndView.setViewName("/salesSupport/thisWeekShipments");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/thisMonthShipments", method = RequestMethod.GET)
    public ModelAndView thisMonthShipments(){
        ModelAndView modelAndView = getModelAndViewWithThisMonthShipments();
        modelAndView.setViewName("/salesSupport/thisMonthShipments");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/nextWeekShipments", method = RequestMethod.GET)
    public ModelAndView nextWeekShipments(){
        ModelAndView modelAndView = getModelAndViewWithNextWeekShipments();
        modelAndView.setViewName("/salesSupport/nextWeekShipments");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/nextMonthShipments", method = RequestMethod.GET)
    public ModelAndView nextMonthShipments(){
        ModelAndView modelAndView = getModelAndViewWithNextMonthShipments();
        modelAndView.setViewName("/salesSupport/nextMonthShipments");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/allShipmentsPerContract/{id}", method = RequestMethod.GET)
    public ModelAndView allShipmentsPerContract(@PathVariable(value = "id") int id){
        Contract contract = contractService.findContractById(id);
        ModelAndView modelAndView = getModelAndViewWithAllShipmentsPerContract(contract);
        modelAndView.setViewName("/salesSupport/allShipmentsPerContract");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/shipment/{id}", method = RequestMethod.GET)
    public ModelAndView shipment(@PathVariable(value = "id") int id){
        ModelAndView modelAndView = createModelAndView ();
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

    @Transactional
    @RequestMapping(value="/salesSupport/shipmentRegistration", method = RequestMethod.POST)
    public ModelAndView registerNewShipment(@RequestParam(value="contractId") String contractId, @ModelAttribute("shipment")Shipment shipmentFromView,@RequestParam(value="productForShipmentWeight[]") List <BigDecimal> productsForShipmentWeight){
        int id = Integer.parseInt(contractId);
        Contract contract = contractService.findContractById(id);
           shipmentFromView.setContract(contract);
//           shipmentService.saveShipment(shipmentFromView);
        List <ProductForShipment> allProductsForShipment = new ArrayList<>();
        contract.getProducts().forEach(product -> allProductsForShipment.add(new ProductForShipment(product)));
        shipmentFromView.setProductsForShipment(allProductsForShipment);
        allProductsForShipment.forEach(productForShipment -> productForShipment.setShipment(shipmentFromView));
           for (int i=0; i<productsForShipmentWeight.size(); i++){
              shipmentFromView.getAllProductsForShipment().get(i).setQuantity(productsForShipmentWeight.get(i));
              productForShipmentService.saveProductForShipment(shipmentFromView.getAllProductsForShipment().get(i));
          }
        shipmentFromView.setIsFulfilled(false);
        shipmentService.saveShipment(shipmentFromView);
        ModelAndView modelAndView = getModelAndViewWithAllShipmentsPerContract(contract);
        modelAndView.setViewName("redirect:/salesSupport/allShipmentsPerContract/"+id);
        return modelAndView;
    }

    @Transactional
    @RequestMapping(value="/salesSupport/shipment", method = RequestMethod.POST)
    public ModelAndView shipment(@ModelAttribute("shipment")Shipment shipmentFromView, @RequestParam(value="productForShipmentWeight[]") List <BigDecimal> productsForShipmentWeight){
        int id = shipmentFromView.getId();
        Shipment shipmentFromDataBase = shipmentService.findById(id);
        for (int i=0; i<productsForShipmentWeight.size(); i++){
            shipmentFromDataBase.getAllProductsForShipment().get(i).setQuantity(productsForShipmentWeight.get(i));
            productForShipmentService.saveProductForShipment(shipmentFromDataBase.getAllProductsForShipment().get(i));
        }
        shipmentFromDataBase.setTruckNumber(shipmentFromView.getTruckNumber());
        shipmentFromDataBase.setInvoiceNumber(shipmentFromView.getInvoiceNumber());
        shipmentFromDataBase.setDestinationCountry(shipmentFromView.getDestinationCountry());
        shipmentFromDataBase.setDestinationPlace(shipmentFromView.getDestinationPlace());
        shipmentFromDataBase.setPlannedLoadingDate(shipmentFromView.getPlannedLoadingDate());
        shipmentFromDataBase.setActualLoadingDate(shipmentFromView.getActualLoadingDate());
        shipmentFromDataBase.setPlannedUnloadingDate(shipmentFromView.getPlannedUnloadingDate());
        shipmentFromDataBase.setActualUnloadingDate(shipmentFromView.getActualUnloadingDate());
        if ("оплата частинами".equals(shipmentFromDataBase.getPaymentTerms())) {
            shipmentFromDataBase.setPlannedPaymentDateOfFirstPartSum(shipmentFromView.getPlannedPaymentDateOfFirstPartSum());
            shipmentFromDataBase.setActualPaymentDateOfFirstPartSum(shipmentFromView.getActualPaymentDateOfFirstPartSum());
            shipmentFromDataBase.setPlannedPaymentDateOfSecondPartSum(shipmentFromView.getPlannedPaymentDateOfSecondPartSum());
            shipmentFromDataBase.setActualPaymentDateOfSecondPartSum(shipmentFromView.getActualPaymentDateOfSecondPartSum());
        } else {
            shipmentFromDataBase.setPlannedPaymentDateOfWholeSum(shipmentFromView.getPlannedPaymentDateOfWholeSum());
            shipmentFromDataBase.setActualPaymentDateOfWholeSum(shipmentFromView.getActualPaymentDateOfWholeSum());
        }
        shipmentFromDataBase.setShipmentComment(shipmentFromView.getShipmentComment());
        shipmentFromDataBase.setLabelsStatus(shipmentFromView.getLabelsStatus());
        shipmentFromDataBase.setLogisticInstructionStatus(shipmentFromView.getLogisticInstructionStatus());
        shipmentFromDataBase.setInvoiceComment(shipmentFromView.getInvoiceComment());
        shipmentFromDataBase.setInvoiceFirstPartSum(shipmentFromView.getInvoiceFirstPartSum());
        shipmentFromDataBase.setInvoiceSecondPartSum(shipmentFromView.getInvoiceSecondPartSum());
        shipmentService.saveShipment(shipmentFromDataBase);
        ModelAndView modelAndView = getModelAndViewWithAllShipmentsPerContract(shipmentFromDataBase.getContract());
        modelAndView.setViewName("redirect:/salesSupport/allShipmentsPerContract/"+shipmentFromDataBase.getContract().getId());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithAllShipments (){
        ModelAndView modelAndView = createModelAndView ();
        modelAndView.addObject("allShipments", shipmentService.allShipments());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithUnpaidShipments (){
        ModelAndView modelAndView = createModelAndView ();
        modelAndView.addObject("unpaidShipments", shipmentService.unpaidShipments());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithAllPaymentsByTheEndOfThisWeek (){
        ModelAndView modelAndView = createModelAndView ();
        int currentWeekNumber = getCurrentWeekNumber();
        modelAndView.addObject("currentWeekNumber", currentWeekNumber);
        List <Shipment> allPaymentsByTheEndOfThisWeekSorted = shipmentService.allPaymentsByTheEndOfThisWeek();
        allPaymentsByTheEndOfThisWeekSorted.sort(Comparator.comparingInt(Shipment::getId));
        modelAndView.addObject("allPaymentsByTheEndOfThisWeekSorted", allPaymentsByTheEndOfThisWeekSorted);
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithAllPaymentsByTheEndOfThisMonth (){
        ModelAndView modelAndView = createModelAndView ();
        List <Shipment> allPaymentsByTheEndOfThisMonthSorted = shipmentService.allPaymentsByTheEndOfThisMonth();
        allPaymentsByTheEndOfThisMonthSorted.sort(Comparator.comparingInt(Shipment::getId));
        modelAndView.addObject("allPaymentsByTheEndOfThisMonthSorted", allPaymentsByTheEndOfThisMonthSorted);
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithAllPaymentsByTheEndOfNextWeek (){
        ModelAndView modelAndView = createModelAndView ();
        int nextWeekNumber = getNextWeekNumber();
        modelAndView.addObject("nextWeekNumber", nextWeekNumber);
        List <Shipment> allPaymentsByTheEndOfNextWeekSorted = shipmentService.allPaymentsByTheEndOfNextWeek();
        allPaymentsByTheEndOfNextWeekSorted.sort(Comparator.comparingInt(Shipment::getId));
        modelAndView.addObject("allPaymentsByTheEndOfNextWeekSorted", allPaymentsByTheEndOfNextWeekSorted);
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithAllPaymentsByTheEndOfNextMonth (){
        ModelAndView modelAndView = createModelAndView();
        List <Shipment> allPaymentsByTheEndOfNextMonthSorted = shipmentService.allPaymentsByTheEndOfNextMonth ();
        allPaymentsByTheEndOfNextMonthSorted.sort(Comparator.comparingInt(Shipment::getId));
        modelAndView.addObject("allPaymentsByTheEndOfNextMonthSorted", allPaymentsByTheEndOfNextMonthSorted);
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithAllPlannedPayments (){
        ModelAndView modelAndView = createModelAndView ();
        List <Shipment> allPlannedPaymentsSorted = shipmentService.allPlannedPayments();
        allPlannedPaymentsSorted.sort(Comparator.comparingInt(Shipment::getId));
        modelAndView.addObject("allPlannedPaymentsSorted", allPlannedPaymentsSorted);
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithAllOverduePayments (){
        ModelAndView modelAndView = createModelAndView ();
        List <Shipment> allOverduePaymentsSorted = shipmentService.allOverduePayments();
        allOverduePaymentsSorted.sort(Comparator.comparingInt(Shipment::getId));
        modelAndView.addObject("allOverduePaymentsSorted", allOverduePaymentsSorted);
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithPaidShipments (){
        ModelAndView modelAndView = createModelAndView ();
        modelAndView.addObject("paidShipments", shipmentService.paidShipments());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithOpenShipments (){
        ModelAndView modelAndView = createModelAndView ();
        modelAndView.addObject("openShipments", shipmentService.openShipments());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithFulfilledShipments (){
        ModelAndView modelAndView = createModelAndView ();
        modelAndView.addObject("fulfilledShipments", shipmentService.fulfilledShipments());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithThisWeekShipments (){
        ModelAndView modelAndView = createModelAndView ();
        int currentWeekNumber = getCurrentWeekNumber();
        modelAndView.addObject("currentWeekNumber", currentWeekNumber);
        modelAndView.addObject("thisWeekShipments", shipmentService.thisWeekShipments());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithThisMonthShipments (){
        ModelAndView modelAndView = createModelAndView ();
        modelAndView.addObject("thisMonthShipments", shipmentService.thisMonthShipments());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithNextWeekShipments (){
        ModelAndView modelAndView = createModelAndView ();
        int nextWeekNumber = getNextWeekNumber();
        modelAndView.addObject("nextWeekNumber", nextWeekNumber);
        modelAndView.addObject("nextWeekShipments", shipmentService.nextWeekShipments());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithNextMonthShipments (){
        ModelAndView modelAndView = createModelAndView ();
        modelAndView.addObject("nextMonthShipments", shipmentService.nextMonthShipments());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithAllShipmentsPerContract (Contract contract){
        ModelAndView modelAndView = createModelAndView ();
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

    private ModelAndView createModelAndView (){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("rateEUR", JsonReader.getRateEUR("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?"));
        modelAndView.addObject("rateUSD", JsonReader.getRateUSD("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?"));
        return modelAndView;
    }

}
