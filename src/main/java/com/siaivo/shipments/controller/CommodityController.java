package com.siaivo.shipments.controller;

import com.siaivo.shipments.model.Commodity;
import com.siaivo.shipments.service.CommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class CommodityController {
    @Autowired
    private CommodityService commodityService;

    @RequestMapping(value="/salesSupport/allCommodities", method = RequestMethod.GET)
    public ModelAndView allCommoditiesForSalesSupport(){
        ModelAndView modelAndView = getModelAndViewWithAllCommodities();
        modelAndView.setViewName("/salesSupport/allCommodities");
        return modelAndView;
    }
    @RequestMapping(value="/salesManagement/allCommodities", method = RequestMethod.GET)
    public ModelAndView allCommoditiesForSalesManagement(){
        ModelAndView modelAndView = getModelAndViewWithAllCommodities();
        modelAndView.setViewName("/salesManagement/allCommodities");
        return modelAndView;
    }

    @RequestMapping(value="/salesSupport/commodityRegistration", method = RequestMethod.GET)
    public ModelAndView registerNewCommodity(){
        ModelAndView modelAndView = new ModelAndView();
        Commodity commodity = new Commodity();
        modelAndView.addObject("commodity", commodity);
        modelAndView.setViewName("/salesSupport/commodityRegistration");
        return modelAndView;
    }

    @RequestMapping(value = "/salesSupport/commodityRegistration", method = RequestMethod.POST)
    public ModelAndView registerNewCommodity (@Valid Commodity commodity, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allCommodities", commodityService.allCommodities());
        Commodity commodityExists = commodityService.findCommodityByCommodityName(commodity.getCommodityName());
        if (commodityExists != null) {
            bindingResult
                    .rejectValue("commodityName", "error.commodity",
                            "Товар з такою назвою вже зареєстровано");
        }
        if (bindingResult.hasErrors()) {
           modelAndView.setViewName("/salesSupport/commodityRegistration");
        } else {
            commodityService.saveCommodity(commodity);
            modelAndView.addObject("successMessage", "Товар успішно зареєстровано");
            modelAndView.addObject("commodity", new Commodity());
            modelAndView.setViewName("/salesSupport/commodityRegistration");

        }
        return modelAndView;
    }
    private ModelAndView getModelAndViewWithAllCommodities (){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allCommodities", commodityService.allCommodities());
        return modelAndView;
    }
}
