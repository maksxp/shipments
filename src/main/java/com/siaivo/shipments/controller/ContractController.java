package com.siaivo.shipments.controller;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Customer;
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
import javax.validation.Valid;
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
        modelAndView.setViewName("/salesManagement/openContracts");
        return modelAndView;
    }

    @RequestMapping(value="/salesManagement/allContracts", method = RequestMethod.GET)
    public ModelAndView allContractsForSalesManagement(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allContracts", contractService.allContracts());
        modelAndView.setViewName("/salesManagement/allContracts");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/allContracts", method = RequestMethod.GET)
    public ModelAndView allContractsForSalesSupport(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allContracts", contractService.allContracts());
        modelAndView.setViewName("/salesSupport/allContracts");
        return modelAndView;
    }

    @RequestMapping(value="/salesManagement/contractRegistration", method = RequestMethod.GET)
    public ModelAndView registerNewContract(){
        ModelAndView modelAndView = new ModelAndView();
        Contract contract = new Contract ();
        ProductForm productForm = new ProductForm();
        return getNewContractModelAndView(contract, productForm, modelAndView);
    }
    @RequestMapping(value="/salesManagement/editContract/{id}", method = RequestMethod.GET)
    public ModelAndView editContract(@PathVariable(value = "id") int id){
        ModelAndView modelAndView = new ModelAndView();
        Contract contract = contractService.findContractById(id);
        ProductForm productForm =new ProductForm();
        productForm.setProducts(productService.findProductsByContract(contract));
        productForm.getProducts().forEach(product -> System.out.println("index: "+productForm.getProducts().indexOf(product)));
        int numberOfProducts = productForm.getProducts().size();
        System.out.println(numberOfProducts);
        return getEditContractModelAndView(contract, productForm, numberOfProducts, modelAndView);
    }

    @RequestMapping(value = "/salesManagement/contractRegistration", method = RequestMethod.POST)
    public ModelAndView registerNewContract (@Valid Contract contract, BindingResult bindingResult, ProductForm productForm, @RequestParam("customerName") String customerName, @RequestParam("paymentTerms") String paymentTerms) {
        ModelAndView modelAndView = new ModelAndView();
        if (isCustomerExists(customerName)!= true) {
              bindingResult
                    .rejectValue("customer", "error.customer",
                            "*Необхідно обрати покупця зі списку");
        }
        List<Product> products = productForm.getProducts();
        if (bindingResult.hasErrors()) {
            return getNewContractModelAndView(contract, productForm, modelAndView);}
        products.stream().filter(product -> product.getCommodity()!=null).forEach(product -> product.setContract(contract));
//        products.forEach(System.out::println);
//        while (products.remove(null)) {
//        }
//        products.forEach(System.out::println);
//        products.stream().forEach(product -> product.setContract(contract));
//        products.forEach(System.out::println);
        contract.setCustomer(customerService.findCustomerByCustomerName(customerName));
        contract.setPaymentTerms(paymentTerms);
        contractService.saveContract(contract);
        products.stream().filter(product -> product.getCommodity()!=null).forEach(product -> productService.saveProduct(product));
        modelAndView.addObject("successMessage", "Контракт успішно зареєстровано");
        return getNewContractModelAndView(new Contract(), new ProductForm(), modelAndView);
    }

    private ModelAndView getNewContractModelAndView(@Valid Contract contract, ProductForm productForm, ModelAndView modelAndView) {
        modelAndView.addObject("allCustomersNames", getAllCustomersNames());
        modelAndView.addObject("allCommodities", commodityService.allCommodities());
        modelAndView.addObject("contract", contract);
        modelAndView.addObject("productForm", productForm);
        modelAndView.setViewName("/salesManagement/contractRegistration");
        return modelAndView;
    }

    private ModelAndView getEditContractModelAndView(@Valid Contract contract, ProductForm productForm, int numberOfProducts, ModelAndView modelAndView) {
        modelAndView.addObject("allCustomersNames", getAllCustomersNames());
        modelAndView.addObject("allCommodities", commodityService.allCommodities());
        modelAndView.addObject("contract", contract);
        modelAndView.addObject("productForm", productForm);
        modelAndView.addObject("numberOfProducts", numberOfProducts);
        modelAndView.setViewName("/salesManagement/editContract");
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
