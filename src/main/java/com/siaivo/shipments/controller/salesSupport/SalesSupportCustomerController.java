package com.siaivo.shipments.controller.salesSupport;

import com.siaivo.shipments.json.JsonReader;
import com.siaivo.shipments.model.Customer;
import com.siaivo.shipments.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class SalesSupportCustomerController {
    @Autowired
    private CustomerService customerService;

    @RequestMapping(value="/salesSupport/allCustomers", method = RequestMethod.GET)
    public ModelAndView allCustomers(){
        ModelAndView modelAndView = getModelAndViewWithAllCustomers();
        modelAndView.setViewName("/salesSupport/allCustomers");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/customerRegistration", method = RequestMethod.GET)
    public ModelAndView registerNewCustomer(){
        ModelAndView modelAndView = createModelAndView ();
        Customer customer = new Customer();
        modelAndView.addObject("customer", customer);
        modelAndView.setViewName("/salesSupport/customerRegistration");
        return modelAndView;
    }

    @RequestMapping(value = "/salesSupport/customerRegistration", method = RequestMethod.POST)
    public ModelAndView registerNewCustomer (@Valid Customer customer, BindingResult bindingResult, @RequestParam("country") String country) {
        ModelAndView modelAndView = createModelAndView ();
        modelAndView.addObject("allCustomers", customerService.allCustomers());
        Customer customerExists = customerService.findCustomerByCustomerName(customer.getCustomerName());
        if (customerExists != null) {
            bindingResult
                    .rejectValue("customerName", "error.customer",
                            "Покупець з такою назвою вже зареєстрований");
        }
//        if (bindingResult.hasErrors()) {
//           modelAndView.setViewName("/salesSupport/customerRegistration");
//        }
         else {
            customer.setCustomerCountry(country);
            customerService.saveCustomer(customer);
            modelAndView.addObject("successMessage", "Покупця успішно зареєстровано");
            modelAndView.addObject("customer", new Customer());
            modelAndView.setViewName("/salesSupport/customerRegistration");
        }
        return modelAndView;
    }
    @RequestMapping(value = "/salesSupport/editCustomer/{id}", method = RequestMethod.GET)
    public ModelAndView editCustomer(@PathVariable(value = "id") int id){
        ModelAndView modelAndView = createModelAndView ();
        Customer customer =  customerService.findCustomerById(id);
        modelAndView.addObject("customer", customer);
        modelAndView.setViewName("/salesSupport/editCustomer");
        return modelAndView;
    }

    @RequestMapping(value = "/salesSupport/editCustomer", method = RequestMethod.POST)
        public ModelAndView editCustomer(@ModelAttribute("customer") Customer customer, @RequestParam("country") String country) {
        ModelAndView modelAndView = createModelAndView ();
        String customerName =  customer.getCustomerName();
        String comment =  customer.getComment();
        String customerType =  customer.getCustomerType();
        String customerCountry = customer.getCustomerCountry();
        int id = customer.getId();
        customer = customerService.findCustomerById(id);
        customer.setCustomerCountry(country);
        //customerType changing
        customerService.editCustomerType(customerType, id);
        //customerComment changing
        customerService.editCustomerComment(comment, id);
        //customerName changing
        if (customerName.length()<1 ) {
            modelAndView.addObject("successMessage", "Назва не може бути пустою");
            return modelAndView;
        } else {
            customerService.editCustomerName(customerName, id);
        }
        //customerCountry changing
//        if (customerCountry.length()<1 ){
//            modelAndView.addObject("successMessage", "Введіть назву країни");
//            return modelAndView;
//        } else {
//            customerService.editCustomerCountry(customerCountry, id);
//        }
        customerService.saveCustomer(customer);
        modelAndView.addObject("successMessage", "Зміни успішно внесено");
        modelAndView.setViewName("redirect:/salesSupport/allCustomers");
        return modelAndView;
    }
    private ModelAndView getModelAndViewWithAllCustomers (){
        ModelAndView modelAndView = createModelAndView ();
        modelAndView.addObject("allCustomers", customerService.allCustomers());
        return modelAndView;
    }

    private ModelAndView createModelAndView (){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("rateEUR", JsonReader.getRateEUR("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?"));
        modelAndView.addObject("rateUSD", JsonReader.getRateUSD("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?"));
        return modelAndView;
    }
}
