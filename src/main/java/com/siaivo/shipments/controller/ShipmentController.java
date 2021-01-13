package com.siaivo.shipments.controller;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @RequestMapping(value="/salesSupport/allShipments", method = RequestMethod.GET)
    public ModelAndView allContractsForSalesSupport(){
        ModelAndView modelAndView = getModelAndViewWithAllShipments();
        modelAndView.setViewName("/salesSupport/allShipments");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/shipment/{id}", method = RequestMethod.GET)
    public ModelAndView getShipmentForSalesSupport(@PathVariable(value = "id") int id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("contractNumber", shipmentService.findById(id).getContract().getContractNumber());
        modelAndView.addObject("contractDate", shipmentService.findById(id).getContract().getContractDate());
        modelAndView.addObject("customerName", shipmentService.findById(id).getContract().getCustomer().getCustomerName());
        modelAndView.addObject("shipment", shipmentService.findById(id));
        modelAndView.setViewName("/salesSupport/shipment");
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithAllShipments (){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allShipments", shipmentService.allShipments());
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithAllShipmentsPerContract (Contract contract){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allShipmentsPerContract", shipmentService.allShipmentsPerContract(contract));
        return modelAndView;
    }
}
