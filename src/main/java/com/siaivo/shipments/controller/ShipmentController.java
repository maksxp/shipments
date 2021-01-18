package com.siaivo.shipments.controller;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Shipment;
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
    private ProductForShipmentService productForShipmentService;

    @RequestMapping(value="/salesSupport/allShipments", method = RequestMethod.GET)
    public ModelAndView allContractsForSalesSupport(){
        ModelAndView modelAndView = getModelAndViewWithAllShipments();
        modelAndView.setViewName("/salesSupport/allShipments");
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
        modelAndView.addObject("shipment", shipment);
        modelAndView.setViewName("/salesSupport/shipment");
        return modelAndView;
    }
    @Transactional
    @RequestMapping(value="/salesSupport/shipment", method = RequestMethod.POST)
    public ModelAndView shipmentForSalesSupport (@ModelAttribute("shipment")Shipment shipment, @RequestParam(value="productForShipment[]") List <BigDecimal> productsForShipment){
          int id = shipment.getId();
          Shipment shipment1 = shipmentService.findById(id);
          System.out.println("number: "+shipment1.getContract().getContractNumber());
          System.out.println("weight: "+productsForShipment.get(0));
          System.out.println("weight: "+productsForShipment.get(1));
          System.out.println("weight: "+productsForShipment.get(2));

        //          shipment.getProductsForShipment().forEach(productForShipment -> System.out.println("prodForShipment: "+productForShipment.getQuantity()));

        Field [] fields = shipment.getClass().getDeclaredFields();
          Arrays.stream(fields).forEach(field -> field.setAccessible(true));
        Field [] fields1 = shipment1.getClass().getDeclaredFields();
        Arrays.stream(fields).forEach(field -> field.setAccessible(true));
          Arrays.stream(fields).forEach(field -> {
              try {
                  System.out.println(field.getName()+": "+field.get(shipment));
              } catch (IllegalAccessException e) {
                  e.printStackTrace();
              }
          });
        Arrays.stream(fields).forEach(field -> {
            try {
                System.out.println(field.getName()+"11: "+field.get(shipment1));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });


//          String comment = shipment.getComment();
//          shipment = shipmentService.findById(id);
//          shipment.setComment(comment);
//          shipment = shipmentService.findById(id);
//          shipmentService.saveShipment(shipment);
//        shipment = shipmentService.findById(id);
//        System.out.println("id: "+id);
//        System.out.println("comment: "+shipment.getComment());
//        System.out.println("invoice: "+shipment.getInvoiceNumber());
//        System.out.println("truck: "+shipment.getTruckNumber());
//        System.out.println("customer: "+shipment.getContract().getCustomer().getCustomerName());
//        shipmentService.saveShipment(shipment);
        //        modelAndView.addObject("successMessage", "Готово");
        ModelAndView modelAndView = getModelAndViewWithAllShipmentsPerContract(shipment1.getContract());
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
