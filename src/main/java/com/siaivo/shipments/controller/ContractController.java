package com.siaivo.shipments.controller;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;
import com.siaivo.shipments.service.CommodityService;
import com.siaivo.shipments.service.ContractService;
import com.siaivo.shipments.service.CustomerService;
import com.siaivo.shipments.service.ProductService;
import com.siaivo.shipments.support.ProductForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;


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
        System.out.println("test "+contractService.openContracts());
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
        ProductForm productForm = new ProductForm();
        return getContractModelAndView(contract, productForm, modelAndView);
    }

    @RequestMapping(value = "/salesManagement/contractRegistration", method = RequestMethod.POST)
    public ModelAndView registerNewContract (@Valid Contract contract, BindingResult bindingResult, ProductForm productForm, @RequestParam("customerName") String customerName) {
        ModelAndView modelAndView = new ModelAndView();
        if (isCustomerExists(customerName)!= true) {
              bindingResult
                    .rejectValue("customer", "error.customer",
                            "*Необхідно обрати покупця зі списку");
        }
        List<Product> products = productForm.getProducts();
        if (bindingResult.hasErrors()) {
            return getContractModelAndView(contract, productForm, modelAndView);}
        products.stream().filter(product -> product.getCommodity()!=null).forEach(product -> product.setContract(contract));
        products.forEach(System.out::println);
        while (products.remove(null)) {
        }
        System.out.println("size "+products.size());
        products.forEach(System.out::println);
        products.stream().forEach(product -> product.setContract(contract));
        contract.setCustomer(customerService.findCustomerByCustomerName(customerName));
//        products.get(0).setContract(contract);
//        products.get(1).setContract(contract);
        contractService.saveContract(contract);
        products.stream().filter(product -> product.getCommodity()!=null).forEach(product -> productService.saveProduct(product));
//        productService.saveProduct(products);
//        productService.saveProduct(products.get(0));
//        productService.saveProduct(products.get(1));
        modelAndView.addObject("successMessage", "Контракт успішно зареєстровано");
        return getContractModelAndView(new Contract(), new ProductForm(), modelAndView);
    }

    private ModelAndView getContractModelAndView(@Valid Contract contract, ProductForm productForm, ModelAndView modelAndView) {
        modelAndView.addObject("allCustomersNames", getAllCustomersNames());
        modelAndView.addObject("allCommodities", commodityService.allCommodities());
        modelAndView.addObject("contract", contract);
        modelAndView.addObject("productForm", productForm);
        modelAndView.setViewName("/salesManagement/contractRegistration");
        return modelAndView;
    }

    private boolean isCustomerExists (String customerName) {
        if (customerService.findCustomerByCustomerName(customerName)!=null) {
            return true;
        } else
            return false;
    }
    private List<String> getAllCustomersNames (){
        List<String> allCustomersNames = new ArrayList<>();
        customerService.allCustomers().forEach(customer -> allCustomersNames.add(customer.getCustomerName()));
        return allCustomersNames;
    }
}
