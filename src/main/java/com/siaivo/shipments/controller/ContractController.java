package com.siaivo.shipments.controller;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;
import com.siaivo.shipments.model.ProductForShipment;
import com.siaivo.shipments.model.Shipment;
import com.siaivo.shipments.service.*;
import com.siaivo.shipments.support.ProductForm;
import com.siaivo.shipments.support.ProductsForShipmentForm;
import com.siaivo.shipments.support.ShipmentsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class ContractController {
    @Autowired
    private ContractService contractService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductForShipmentService productForShipmentService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CommodityService commodityService;
    @Autowired
    private ShipmentService shipmentService;

    @ModelAttribute ("shipmentsForm")
    public ShipmentsForm getShipmentsForm() {
        ShipmentsForm shipmentsForm = new ShipmentsForm();
        shipmentsForm.setShipments(new ArrayList<>());
        return shipmentsForm;
    }

    @RequestMapping(value="/salesManagement/openContracts", method = RequestMethod.GET)
    public ModelAndView openContractsForSalesManagement (){
        ModelAndView modelAndView = getModelAndViewWithOpenContracts();
        modelAndView.setViewName("/salesManagement/openContracts");
        return modelAndView;
    }

    @RequestMapping(value="/salesManagement/fulfilledContracts", method = RequestMethod.GET)
    public ModelAndView fulfilledContractsForSalesManagement (){
        ModelAndView modelAndView = getModelAndViewWithFulfilledContracts();
        modelAndView.setViewName("/salesManagement/fulfilledContracts");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/openContracts", method = RequestMethod.GET)
    public ModelAndView openContractsForSalesSupport (){
        ModelAndView modelAndView = getModelAndViewWithOpenContracts();
        modelAndView.setViewName("/salesSupport/openContracts");
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

    @RequestMapping(value= "/salesManagement/requestForNewContract", method = RequestMethod.GET)
    public ModelAndView requestForNewContractFromSalesManagement (){
        ModelAndView modelAndView = new ModelAndView();
        Contract contract = new Contract ();
        ProductForm productForm = new ProductForm();
        modelAndView.setViewName("/salesManagement/requestForNewContract");
        return getRequestForNewContractModelAndView(contract, productForm, modelAndView);
    }

    @RequestMapping(value="/salesSupport/requestForNewContract", method = RequestMethod.GET)
    public ModelAndView requestForNewContractFromSalesSupport(){
        ModelAndView modelAndView = new ModelAndView();
        Contract contract = new Contract ();
        ProductForm productForm = new ProductForm();
        modelAndView.setViewName("/salesSupport/requestForNewContract");
        return getRequestForNewContractModelAndView(contract, productForm, modelAndView);
    }

    @RequestMapping(value="/salesSupport/contract/{id}", method = RequestMethod.GET)
    public ModelAndView getContractForSalesSupport(@PathVariable(value = "id") int id){
        ModelAndView modelAndView = new ModelAndView();
        Contract contract = contractService.findContractById(id);
        modelAndView.addObject("contract", contract);
        modelAndView.addObject("allShipmentsPerContract", shipmentService.allShipmentsPerContract(contract));
        modelAndView.addObject("allProductsByContract", productService.findProductsByContract(contract));
        modelAndView.addObject("weightOfAllProductsByContract", productService.findWeightOfAllProductsByContract(contract));
        modelAndView.setViewName("/salesSupport/contract");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/contract", method = RequestMethod.POST)
    public ModelAndView changeContractStateForSalesSupport(@ModelAttribute("contract")Contract contractFromView){
        int id = contractFromView.getId();
        Contract contractFromDataBase = contractService.findContractById(id);
        contractFromDataBase.setState(contractFromView.getState());
        contractService.saveContract(contractFromDataBase);
        ModelAndView modelAndView = getModelAndViewWithAllContracts();
        modelAndView.setViewName("redirect:/salesSupport/allContracts");
        return modelAndView;
    }

    @RequestMapping(value="/salesManagement/editRequestForNewContract/{id}", method = RequestMethod.GET)
    public ModelAndView editRequestForNewContractForSalesManagement (@PathVariable(value = "id") int id){
        ModelAndView modelAndView = getEditRequestForNewContractModelAndView(id);
        modelAndView.setViewName("/salesManagement/editRequestForNewContract");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/editRequestForNewContract/{id}", method = RequestMethod.GET)
    public ModelAndView editRequestForNewContractForSalesSupport (@PathVariable(value = "id") int id){
        ModelAndView modelAndView = getEditRequestForNewContractModelAndView(id);
        modelAndView.setViewName("/salesSupport/editRequestForNewContract");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/contractPreparation/{id}", method = RequestMethod.GET)
    public ModelAndView getContractPreparationForSalesSupport (@PathVariable(value = "id") int id, @ModelAttribute("shipmentsForm") ShipmentsForm shipmentsForm){
        ModelAndView modelAndView = new ModelAndView();
        Contract contract = contractService.findContractById(id);
        modelAndView.addObject("allProductsByContract", productService.findProductsByContract(contract));
        modelAndView.addObject("weightOfAllProductsByContract", productService.findWeightOfAllProductsByContract(contract));
        modelAndView.addObject("contract", contract);
        modelAndView.addObject("shipmentsForm", shipmentsForm);
        modelAndView.setViewName("/salesSupport/contractPreparation");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/contractPreparation", method = RequestMethod.POST)
    public ModelAndView postContractPreparationForSalesSupport (@RequestParam("id") int id, @RequestParam("contractDate") String contractDate, @RequestParam("contractNumber") String contractNumber, @RequestParam("numberOfTrucks") int numberOfTrucks){
        Contract contract = contractService.findContractById(id);
        int numberOfLoadedTrucks = (int) contract.getShipments().stream().filter(shipment -> !shipment.getActualLoadingDate().equals("") || shipment.getActualLoadingDate() != null).count();
        System.out.println("loaded trucks: "+numberOfLoadedTrucks);
        contract.setContractNumber(contractNumber);
        contract.setContractDate(contractDate);
        contract.setState("підготовлений");
        contractService.saveContract(contract);
        List <Product> products = contract.getProducts();
            for (int i=0;i<numberOfTrucks-numberOfLoadedTrucks;i++){
                ProductsForShipmentForm productsForShipmentForm = new ProductsForShipmentForm(contract.getProducts().size());
                Shipment shipment = new Shipment();
                shipment.setContract(contract);
                shipment.setLogisticInstructionStatus("готується");
                shipment.setLabelsStatus("готується");
                shipment.setInvoiceNumber(contractNumber+"."+(i+1));
                shipment.setTruckNumber(i+1);
                for (int j=0; j<productsForShipmentForm.getProductsForShipment().size(); j++) {
                    productsForShipmentForm.getProductsForShipment().get(j).setShipment(shipment);
                    productsForShipmentForm.getProductsForShipment().get(j).setProduct(products.get(j));
                    productsForShipmentForm.getProductsForShipment().get(j).setQuantity((products.get(j).getQuantity()).divide(BigDecimal.valueOf(numberOfTrucks),3, RoundingMode.UP));
                }
                shipment.setProductsForShipment(productsForShipmentForm.getProductsForShipment());
                shipmentService.saveShipment(shipment);
                productsForShipmentForm.getProductsForShipment().stream().forEach(productForShipment -> productForShipmentService.saveProductForShipment(productForShipment));
           }
            ModelAndView modelAndView = getModelAndViewWithAllShipmentsPerContract(contract);
            modelAndView.setViewName("/salesSupport/allShipmentsPerContract");
            return modelAndView;
        }

    @RequestMapping(value = "/salesManagement/requestForNewContract", method = RequestMethod.POST)
    public ModelAndView requestForNewContractFromSalesManagement (@Valid Contract contract, BindingResult bindingResult, ProductForm productForm, @RequestParam("customerName") String customerName, @RequestParam("paymentTerms") String paymentTerms) {
        ModelAndView modelAndView = saveRequestForNewContractModelAndView(contract, bindingResult, productForm, customerName, paymentTerms);
        modelAndView.setViewName("redirect:/salesManagement/contractsForPreparation");
        return modelAndView;
    }

    @RequestMapping(value = "/salesSupport/requestForNewContract", method = RequestMethod.POST)
    public ModelAndView requestForNewContractFromSalesSupport (@Valid Contract contract, BindingResult bindingResult, ProductForm productForm, @RequestParam("customerName") String customerName, @RequestParam("paymentTerms") String paymentTerms) {
        ModelAndView modelAndView = saveRequestForNewContractModelAndView(contract, bindingResult, productForm, customerName, paymentTerms);
        modelAndView.setViewName("redirect:/salesSupport/contractsForPreparation");
        return modelAndView;
    }

    @RequestMapping(value = "/salesManagement/editRequestForNewContract", method = RequestMethod.POST)
    public ModelAndView editRequestForNewContractForSalesManagement (@Valid Contract contract, BindingResult bindingResult, ProductForm productForm, @RequestParam("customerName") String customerName, @RequestParam("paymentTerms") String paymentTerms) {
        ModelAndView modelAndView = saveEditRequestForNewContractModelAndView(contract, bindingResult, productForm, customerName, paymentTerms);
        modelAndView.setViewName("redirect:/salesManagement/contractsForPreparation");
        return modelAndView;
    }

    @RequestMapping(value = "/salesSupport/editRequestForNewContract", method = RequestMethod.POST)
    public ModelAndView editRequestForNewContractForSalesSupport (@Valid Contract contract, BindingResult bindingResult, ProductForm productForm, @RequestParam("customerName") String customerName, @RequestParam("paymentTerms") String paymentTerms) {
        ModelAndView modelAndView = saveEditRequestForNewContractModelAndView(contract, bindingResult, productForm, customerName, paymentTerms);
        modelAndView.setViewName("redirect:/salesSupport/contractsForPreparation");
        return modelAndView;
    }
    private ModelAndView saveRequestForNewContractModelAndView(@Valid Contract contract, BindingResult bindingResult, ProductForm productForm, @RequestParam("customerName") String customerName, @RequestParam("paymentTerms") String paymentTerms) {
        List<Product> products = productForm.getProducts();
        if (bindingResult.hasErrors()) {
            return getRequestForNewContractModelAndView(contract, productForm, new ModelAndView());
        }
        products.stream().filter(product -> (product.getCommodity())!=null).forEach(product -> product.setContract(contract));
        contract.setCustomer(customerService.findCustomerByCustomerName(customerName));
        contract.setPaymentTerms(paymentTerms);
        contractService.saveRequest(contract);
        products.stream().filter(product -> (product.getCommodity())!=null).forEach(product -> productService.saveProduct(product));
        return getModelAndViewWithContractsForPreparation ();
    }

    private ModelAndView saveEditRequestForNewContractModelAndView(@Valid Contract contract, BindingResult bindingResult, ProductForm productForm, @RequestParam("customerName") String customerName, @RequestParam("paymentTerms") String paymentTerms) {
        int id = contract.getId();
        System.out.println("id: "+id);
        List<Product> products = productForm.getProducts();
//        List <ProductForShipment> productForShipment =
        List <Shipment> allShipmentsPerContract = shipmentService.allShipmentsPerContract(contract);
        System.out.println("size all: "+allShipmentsPerContract.size());
        List<Shipment> notLoadedShipments = allShipmentsPerContract.stream().filter(shipment -> shipment.getActualLoadingDate()==null || shipment.getActualLoadingDate().equals("")).collect(Collectors.toList());
        System.out.println("size not loaded: "+notLoadedShipments.size());
        notLoadedShipments.forEach(shipment -> shipment.getProductsForShipment().forEach(productForShipment -> productForShipmentService.deleteProductForShipment(productForShipment)));
        notLoadedShipments.forEach(shipment -> shipmentService.deleteShipment(shipment));
        if (bindingResult.hasErrors()) {
            return getModelAndViewWithContractsForPreparation();
        }
        contract.setCustomer(customerService.findCustomerByCustomerName(customerName));
        contract.setPaymentTerms(paymentTerms);
        contractService.saveRequest(contract);
        products.stream().filter(product -> (product.getCommodity())!=null).forEach(product -> product.setContract(contract));
        products.stream().filter(product -> (product.getCommodity())==null).forEach(product -> productService.deleteProduct(product));
        products.stream().filter(product -> (product.getCommodity())!=null).forEach(product -> productService.saveProduct(product));
        return getModelAndViewWithContractsForPreparation();
    }


    private ModelAndView getRequestForNewContractModelAndView(@Valid Contract contract, ProductForm productForm, ModelAndView modelAndView) {
        modelAndView.addObject("allCustomersNames", getAllCustomersNames());
        modelAndView.addObject("allCommodities", commodityService.allCommodities());
        modelAndView.addObject("contract", contract);
        modelAndView.addObject("productForm", productForm);
        return modelAndView;
    }

    private ModelAndView getEditRequestForNewContractModelAndView(int id) {
        ModelAndView modelAndView = new ModelAndView();
        Contract contract = contractService.findContractById(id);
        ProductForm productForm =new ProductForm();
        productForm.setProductsForEdit(productService.findProductsByContract(contract));
        int numberOfProducts = contract.getProducts().size();
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
    private ModelAndView getModelAndViewWithContractsForPreparation(){
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

    private ModelAndView getModelAndViewWithAllShipmentsPerContract (Contract contract){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("contract", contract);
        modelAndView.addObject("allShipmentsPerContract", shipmentService.allShipmentsPerContract(contract));
        return modelAndView;
    }

}
