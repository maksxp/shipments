package com.siaivo.shipments.controller;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class ContractController {
    @Autowired
    private ContractService contractService;

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
        modelAndView.addObject("contract", contract);
        modelAndView.setViewName("/salesManagement/contractRegistration");
        return modelAndView;
    }

    @RequestMapping(value = "/salesManagement/contractRegistration", method = RequestMethod.POST)
    public ModelAndView registerNewCustomer (@Valid Contract contract, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allContracts", contractService.allContracts());
        Contract contractExists = contractService.findContractByContractNumberAndContractDate(contract.getContractNumber(),contract.getContractDate());
        if (contractExists != null) {
            bindingResult
                    .rejectValue("contract", "error.contract",
                            "Контракт з таким номером та датою вже зареєстровано");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("/salesManagement/contractRegistration");
        } else {
            contractService.saveContract(contract);
            modelAndView.addObject("successMessage", "Контракт успішно зареєстровано");
            modelAndView.addObject("contract", new Contract());
            modelAndView.setViewName("/salesManagement/contractRegistration");

        }
        return modelAndView;
    }
}
