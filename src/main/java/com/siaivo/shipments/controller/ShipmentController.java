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
import java.util.Arrays;
import java.util.List;

@Controller
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ProductForShipmentService productForShipmentService;

    @RequestMapping(value="/salesSupport/allShipments", method = RequestMethod.GET)
    public ModelAndView allContractsForSalesSupport(){
        ModelAndView modelAndView = getModelAndViewWithAllShipments();
        modelAndView.setViewName("/salesSupport/allShipments");
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
    @Transactional
    @RequestMapping(value="/salesSupport/shipment", method = RequestMethod.POST)
    public ModelAndView shipmentForSalesSupport (@ModelAttribute("shipment")Shipment shipmentFromView, @RequestParam(value="productForShipmentWeight[]") List <BigDecimal> productsForShipmentWeight){
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
        modelAndView.setViewName("/salesSupport/allShipmentsPerContract");
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithAllShipments (){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allShipments", shipmentService.allShipments());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithAllShipmentsPerContract (Contract contract){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("contract", contract);
        modelAndView.addObject("allShipmentsPerContract", shipmentService.allShipmentsPerContract(contract));
        return modelAndView;
    }
}
