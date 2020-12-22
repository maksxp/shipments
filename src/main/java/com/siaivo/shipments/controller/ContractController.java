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
import org.springframework.security.access.method.P;
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
    public ModelAndView openContractsForSalesManagement (){
        ModelAndView modelAndView = getModelAndViewWithOpenContracts();
        modelAndView.setViewName("/salesManagement/openContracts");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/openContracts", method = RequestMethod.GET)
    public ModelAndView openContractsForSalesSupport(){
        ModelAndView modelAndView = getModelAndViewWithOpenContracts();
        modelAndView.setViewName("/salesSupport/openContracts");
        return modelAndView;
    }
    @RequestMapping(value="/salesManagement/fulfilledContracts", method = RequestMethod.GET)
    public ModelAndView fulfilledContractsForSalesManagement (){
        ModelAndView modelAndView = getModelAndViewWithFulfilledContracts();
        modelAndView.setViewName("/salesManagement/fulfilledContracts");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/fulfilledContracts", method = RequestMethod.GET)
    public ModelAndView fulfilledContractsForSalesSupport(){
        ModelAndView modelAndView = getModelAndViewWithFulfilledContracts();
        modelAndView.setViewName("/salesSupport/fulfilledContracts");
        return modelAndView;
    }
    @RequestMapping(value="/salesManagement/contractsForPreparation", method = RequestMethod.GET)
    public ModelAndView contractsForPreparationForSalesManagement (){
        ModelAndView modelAndView = getModelAndViewWithContractsForPreparation();
        modelAndView.setViewName("/salesManagement/contractsForPreparation");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/contractsForPreparation", method = RequestMethod.GET)
    public ModelAndView contractsForPreparationForSalesSupport(){
        ModelAndView modelAndView = getModelAndViewWithContractsForPreparation();
        modelAndView.setViewName("/salesSupport/contractsForPreparation");
        return modelAndView;
    }

    @RequestMapping(value="/salesManagement/allContracts", method = RequestMethod.GET)
    public ModelAndView allContractsForSalesManagement(){
        ModelAndView modelAndView = getModelAndViewWithAllContracts();
        modelAndView.setViewName("/salesManagement/allContracts");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/allContracts", method = RequestMethod.GET)
    public ModelAndView allContractsForSalesSupport(){
        ModelAndView modelAndView = getModelAndViewWithAllContracts();
        modelAndView.setViewName("/salesSupport/allContracts");
        return modelAndView;
    }

    @RequestMapping(value="/salesManagement/contractRegistration", method = RequestMethod.GET)
    public ModelAndView registerNewContractForSalesManagement (){
        ModelAndView modelAndView = new ModelAndView();
        Contract contract = new Contract ();
        ProductForm productForm = new ProductForm();
        modelAndView.setViewName("/salesManagement/contractRegistration");
        return getNewContractModelAndView(contract, productForm, modelAndView);
    }

    @RequestMapping(value="/salesSupport/contractRegistration", method = RequestMethod.GET)
    public ModelAndView registerNewContractForSalesSupport(){
        ModelAndView modelAndView = new ModelAndView();
        Contract contract = new Contract ();
        ProductForm productForm = new ProductForm();
        modelAndView.setViewName("/salesSupport/contractRegistration");
        return getNewContractModelAndView(contract, productForm, modelAndView);
    }
    @RequestMapping(value="/salesManagement/editContract/{id}", method = RequestMethod.GET)
    public ModelAndView editContractForSalesManagement (@PathVariable(value = "id") int id){
        ModelAndView modelAndView = getEditContractModelAndView(id);
        modelAndView.setViewName("/salesManagement/editContract");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/editContract/{id}", method = RequestMethod.GET)
    public ModelAndView editContractForSalesSupport (@PathVariable(value = "id") int id){
        ModelAndView modelAndView = getEditContractModelAndView(id);
        modelAndView.setViewName("/salesSupport/editContract");
        return modelAndView;
    }
    @RequestMapping(value = "/salesManagement/contractRegistration", method = RequestMethod.POST)
    public ModelAndView registerNewContractForSalesManagement (@Valid Contract contract, BindingResult bindingResult, ProductForm productForm, @RequestParam("customerName") String customerName, @RequestParam("paymentTerms") String paymentTerms) {
        ModelAndView modelAndView = saveContractModelAndView(contract, bindingResult, productForm, customerName, paymentTerms);
        modelAndView.setViewName("/salesManagement/contractRegistration");
        return modelAndView;
    }

    @RequestMapping(value = "/salesSupport/contractRegistration", method = RequestMethod.POST)
    public ModelAndView registerNewContractForSalesSupport (@Valid Contract contract, BindingResult bindingResult, ProductForm productForm, @RequestParam("customerName") String customerName, @RequestParam("paymentTerms") String paymentTerms) {
        ModelAndView modelAndView = saveContractModelAndView(contract, bindingResult, productForm, customerName, paymentTerms);
        modelAndView.setViewName("/salesSupport/contractRegistration");
        return modelAndView;
    }

    @RequestMapping(value = "/salesManagement/editContract", method = RequestMethod.POST)
    public ModelAndView editContractForSalesManagement (@Valid Contract contract, BindingResult bindingResult, ProductForm productForm, @RequestParam("customerName") String customerName, @RequestParam("paymentTerms") String paymentTerms) {
        productService.deleteProductsByContract(contract);
        ModelAndView modelAndView = saveContractModelAndView(contract, bindingResult, productForm, customerName, paymentTerms);
        modelAndView.setViewName("/salesManagement/contractRegistration");
        return modelAndView;
    }

    @RequestMapping(value = "/salesSupport/editContract", method = RequestMethod.POST)
    public ModelAndView editContractForSalesSupport (@Valid Contract contract, BindingResult bindingResult, ProductForm productForm, @RequestParam("customerName") String customerName, @RequestParam("paymentTerms") String paymentTerms) {
        productService.deleteProductsByContract(contract);
        ModelAndView modelAndView = saveContractModelAndView(contract, bindingResult, productForm, customerName, paymentTerms);
        modelAndView.setViewName("/salesSupport/contractRegistration");
        return modelAndView;
    }
    private ModelAndView saveContractModelAndView(@Valid Contract contract, BindingResult bindingResult, ProductForm productForm, @RequestParam("customerName") String customerName, @RequestParam("paymentTerms") String paymentTerms) {
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
        contract.setCustomer(customerService.findCustomerByCustomerName(customerName));
        contract.setPaymentTerms(paymentTerms);
        contractService.saveContract(contract);
        products.stream().filter(product -> product.getCommodity()!=null).forEach(product -> productService.saveProduct(product));
        modelAndView.addObject("successMessage", "Готово");
        return getNewContractModelAndView(new Contract(), new ProductForm(), modelAndView);
    }

    private ModelAndView getNewContractModelAndView(@Valid Contract contract, ProductForm productForm, ModelAndView modelAndView) {
        modelAndView.addObject("allCustomersNames", getAllCustomersNames());
        modelAndView.addObject("allCommodities", commodityService.allCommodities());
        modelAndView.addObject("contract", contract);
        modelAndView.addObject("productForm", productForm);
//        modelAndView.setViewName("/salesManagement/contractRegistration");
        return modelAndView;
    }

    private ModelAndView getEditContractModelAndView(int id) {
        ModelAndView modelAndView = new ModelAndView();
        Contract contract = contractService.findContractById(id);
        ProductForm productForm =new ProductForm();
        productForm.setProducts(productService.findProductsByContract(contract));
        int numberOfProducts = productForm.getProducts().size();
        modelAndView.addObject("allCustomersNames", getAllCustomersNames());
        modelAndView.addObject("allCommodities", commodityService.allCommodities());
        modelAndView.addObject("contract", contract);
        modelAndView.addObject("productForm", productForm);
        modelAndView.addObject("numberOfProducts", numberOfProducts);
        return modelAndView;
    }

    private ModelAndView getModelAndViewWithAllContracts (){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allContracts", contractService.allContracts());
        return modelAndView;
    }
    private ModelAndView getModelAndViewWithFulfilledContracts (){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("fulfilledContracts", contractService.fulfilledContracts());
        return modelAndView;
    }
    private ModelAndView getModelAndViewWithOpenContracts (){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("openContracts", contractService.openContracts());
        return modelAndView;
    }
    private ModelAndView getModelAndViewWithContractsForPreparation (){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("contractsForPreparation", contractService.contractsForPreparation());
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
