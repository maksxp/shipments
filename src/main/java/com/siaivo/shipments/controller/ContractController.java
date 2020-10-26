package com.siaivo.shipments.controller;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;
import com.siaivo.shipments.service.ContractService;
import com.siaivo.shipments.service.CustomerService;
import com.siaivo.shipments.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContractController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CustomerService customerService;

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
    public ModelAndView registerNewCustomer(){
        ModelAndView modelAndView = new ModelAndView();
        Contract contract = new Contract ();
        Product product = new Product();
        modelAndView.addObject("allCustomers", customerService.allCustomers());
        modelAndView.addObject("contract", contract);
        modelAndView.addObject("product", product);
        modelAndView.setViewName("/salesManagement/contractRegistration");
        return modelAndView;
    }

    @RequestMapping(value = "/salesManagement/contractRegistration", method = RequestMethod.POST)
    public ModelAndView registerNewCustomer (Contract contract, Product product) {
        ModelAndView modelAndView = new ModelAndView();
            contractService.saveContract(contract);
            productService.saveProduct(product);
            modelAndView.addObject("successMessage", "Контракт успішно зареєстровано");
            modelAndView.addObject("contract", new Contract());
            modelAndView.addObject("product", new Product());
            modelAndView.setViewName("/salesManagement/contractRegistration");
            return modelAndView;
    }
}
