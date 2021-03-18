package com.siaivo.shipments.controller.salesSupport;

import com.siaivo.shipments.model.*;
import com.siaivo.shipments.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SalesSupportProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductForShipmentService productForShipmentService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private CommodityService commodityService;

    @RequestMapping(value = "/salesSupport/deleteProduct/{id}", method = RequestMethod.GET)
    public ModelAndView deleteProduct(@PathVariable(value = "id") int id){
        Product product =  productService.findById(id);
        productService.deleteProduct(product);
        Contract contract = product.getContract();
        int contractId = contract.getId();
        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("contract", contract);
//        modelAndView.addObject("allShipmentsPerContract", shipmentService.allShipmentsPerContract(contract));
//        modelAndView.addObject("allProductsByContract", productService.findProductsByContract(contract));
//        modelAndView.addObject("weightOfAllProductsByContract", productService.findWeightOfAllProductsByContract(contract));
        modelAndView.setViewName("redirect:/salesSupport/contract/"+contractId);
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/productRegistration/{id}", method = RequestMethod.GET)
    public ModelAndView registerNewProduct(@PathVariable(value = "id") int id){
        Contract contract = contractService.findContractById(id);
        ModelAndView modelAndView = new ModelAndView();
        Product product = new Product();
        modelAndView.addObject("product", product);
//        String country="";
//        String place="";
//        if (contract.getShipments().size()>0){
//            country = contract.getShipments().get(0).getDestinationCountry();
//            place = contract.getShipments().get(0).getDestinationPlace();
//        }
//        int truckNumber = contract.getShipments().size()+1;
//        String invoiceNumber = contract.getContractNumber()+"."+truckNumber;
//        ModelAndView modelAndView = new ModelAndView();
//        Shipment shipment = new Shipment();
//        List<ProductForShipment> allProductsForShipment = new ArrayList<>();
//        contract.getProducts().forEach(product -> allProductsForShipment.add(new ProductForShipment(product)));
        modelAndView.addObject("contractNumber", contract.getContractNumber());
        modelAndView.addObject("allCommodities", commodityService.allCommodities());
        modelAndView.addObject("contractDate", contract.getContractDate());
        modelAndView.addObject("customerName", contract.getCustomer().getCustomerName());
        modelAndView.addObject("contractId", id);
        modelAndView.setViewName("/salesSupport/productRegistration");
        return modelAndView;
    }

    @Transactional
    @RequestMapping(value="/salesSupport/productRegistration", method = RequestMethod.POST)
    public ModelAndView registerNewProduct (@RequestParam(value="contractId") String contractId, @ModelAttribute("product") Product product){
        int id = Integer.parseInt(contractId);
        Contract contract = contractService.findContractById(id);
        product.setContract(contract);
        List <Shipment> allShipmentsPerContract = contract.getShipments();
        List <Product> allProductsPerContract = contract.getProducts();
        if (allProductsPerContract.contains(product)){
            Product theSameProduct = allProductsPerContract
                    .stream()
                    .filter(product1 -> product1.equals(product))
                    .findAny()
                    .orElse(null);
            theSameProduct.addQuantity(product.getQuantity());
        } else {
            allShipmentsPerContract.forEach(shipment -> {
            ProductForShipment  productForShipment = new ProductForShipment(product);
            productForShipment.setShipment(shipment);
            productForShipment.setQuantity(BigDecimal.ZERO);
            productForShipmentService.saveProductForShipment(productForShipment);
            productService.saveProduct(product);
            });
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("contract", contract);
        modelAndView.addObject("allShipmentsPerContract", shipmentService.allShipmentsPerContract(contract));
        modelAndView.addObject("allProductsByContract", productService.findProductsByContract(contract));
        modelAndView.addObject("weightOfAllProductsByContract", productService.findWeightOfAllProductsByContract(contract));
        modelAndView.setViewName("redirect:/salesSupport/contract/"+id);
        return modelAndView;
    }
}
