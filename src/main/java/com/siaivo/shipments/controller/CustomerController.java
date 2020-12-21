package com.siaivo.shipments.controller;

import com.siaivo.shipments.model.Customer;
import com.siaivo.shipments.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @RequestMapping(value="/salesSupport/allCustomers", method = RequestMethod.GET)
    public ModelAndView allCustomers(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allCustomers", customerService.allCustomers());
        modelAndView.setViewName("/salesSupport/allCustomers");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/customerRegistration", method = RequestMethod.GET)
    public ModelAndView registerNewCustomer(){
        ModelAndView modelAndView = new ModelAndView();
        Customer customer = new Customer();
        modelAndView.addObject("customer", customer);
        modelAndView.setViewName("/salesSupport/customerRegistration");
        return modelAndView;
    }

    @RequestMapping(value = "/salesSupport/customerRegistration", method = RequestMethod.POST)
    public ModelAndView registerNewCustomer (@Valid Customer customer, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allCustomers", customerService.allCustomers());
        Customer customerExists = customerService.findCustomerByCustomerName(customer.getCustomerName());
        if (customerExists != null) {
            bindingResult
                    .rejectValue("customerName", "error.customer",
                            "Покупець з такою назвою вже зареєстрований");
        }
        if (bindingResult.hasErrors()) {
           modelAndView.setViewName("/salesSupport/customerRegistration");
        } else {
            customerService.saveCustomer(customer);
            modelAndView.addObject("successMessage", "Покупця успішно зареєстровано");
            modelAndView.addObject("customer", new Customer());
            modelAndView.setViewName("/salesSupport/customerRegistration");

        }
        return modelAndView;
    }
    @RequestMapping(value = "/salesSupport/editCustomer/{id}", method = RequestMethod.GET)
    public ModelAndView editCustomer(@PathVariable(value = "id") int id){
        ModelAndView modelAndView = new ModelAndView();
        Customer customer =  customerService.findCustomerById(id);
        modelAndView.addObject("customer", customer);
        modelAndView.setViewName("/salesSupport/editCustomer");
        return modelAndView;
    }

    @RequestMapping(value = "/salesSupport/editCustomer", method = RequestMethod.POST)
        public ModelAndView editCustomer(@ModelAttribute("customer") Customer customer) {
        ModelAndView modelAndView = new ModelAndView();
        String customerName =  customer.getCustomerName();
        String comment =  customer.getComment();
        String customerType =  customer.getCustomerType();
        String customerCountry = customer.getCustomerCountry();
        int id = customer.getId();
        customer = customerService.findCustomerById(id);
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
        if (customerCountry.length()<1 ){
            modelAndView.addObject("successMessage", "Введіть назву країни");
            return modelAndView;
        } else {
            customerService.editCustomerCountry(customerCountry, id);
        }
        customerService.saveCustomer(customer);
        modelAndView.addObject("successMessage", "Зміни успішно внесено");
        return modelAndView;
    }
}
