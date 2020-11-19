package com.siaivo.shipments.controller;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;
import com.siaivo.shipments.service.CommodityService;
import com.siaivo.shipments.service.ContractService;
import com.siaivo.shipments.service.CustomerService;
import com.siaivo.shipments.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller
public class ContractController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CommodityService commodityService;

    @RequestMapping(value="/salesManagement/openContracts", method = RequestMethod.GET)
    public ModelAndView openContracts(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("openContracts", contractService.openContracts());
        modelAndView.setViewName("/salesManagement/openContracts");
        return modelAndView;
    }

    @RequestMapping(value="/salesManagement/allContracts", method = RequestMethod.GET)
    public ModelAndView allContracts(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allContracts", contractService.allContracts());
        modelAndView.setViewName("/salesManagement/allContracts");
        return modelAndView;
    }

    @RequestMapping(value="/salesManagement/contractRegistration", method = RequestMethod.GET)
    public ModelAndView registerNewContract(){
        ModelAndView modelAndView = new ModelAndView();
        Contract contract = new Contract ();
        Product product = new Product();
        return getContractModelAndView(contract, product, modelAndView);
    }

    @RequestMapping(value = "/salesManagement/contractRegistration", method = RequestMethod.POST)
    public ModelAndView registerNewContract (@Valid Contract contract, BindingResult bindingResult, Product product, @RequestParam("customerName") String customerName) {
        ModelAndView modelAndView = new ModelAndView();
        if (isCustomerExists(customerName)!= true) {
              bindingResult
                    .rejectValue("customer", "error.customer",
                            "Необхідно обрати покупця зі списку");
        }
        if (bindingResult.hasErrors()) {
            return getContractModelAndView(contract, product, modelAndView);}
        contract.setCustomer(customerService.findCustomerByCustomerName(customerName));
        contractService.saveContract(contract);
        product.setContract(contract);
        productService.saveProduct(product);
        modelAndView.addObject("successMessage", "Контракт успішно зареєстровано");
        return getContractModelAndView(contract, product, modelAndView);
//         return new ModelAndView( "redirect:/salesManagement/contractRegistration");
    }

    private ModelAndView getContractModelAndView(@Valid Contract contract, Product product, ModelAndView modelAndView) {
        List<String> allCustomersNames = new ArrayList<>();
        customerService.allCustomers().forEach(customer -> allCustomersNames.add(customer.getCustomerName()));
        modelAndView.addObject("allCustomersNames", allCustomersNames);
        modelAndView.addObject("allCommodities", commodityService.allCommodities());
        modelAndView.addObject("contract", contract);
        modelAndView.addObject("product", product);
        modelAndView.setViewName("/salesManagement/contractRegistration");
        return modelAndView;
    }

    private boolean isCustomerExists (String customerName) {
        if (customerService.findCustomerByCustomerName(customerName)!=null) {
            return true;
        } else
            return false;
    }
}
