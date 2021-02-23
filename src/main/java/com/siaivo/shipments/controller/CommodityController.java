package com.siaivo.shipments.controller;

import com.siaivo.shipments.model.Product;
import com.siaivo.shipments.service.ProductService;
import org.json.JSONException;
import org.json.JSONObject;
import com.siaivo.shipments.model.Commodity;
import com.siaivo.shipments.json.JsonReader;
import com.siaivo.shipments.service.CommodityService;
import com.siaivo.shipments.service.ShipmentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class CommodityController {
    @Autowired
    private CommodityService commodityService;

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private ProductService productService;

    @RequestMapping(value="/salesSupport/allCommodities", method = RequestMethod.GET)
    public ModelAndView allCommoditiesForSalesSupport() throws IOException {
        ModelAndView modelAndView = getModelAndViewWithAllCommodities();
        modelAndView.addObject("rateEUR", JsonReader.getRateEUR("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?"));
        modelAndView.addObject("rateUSD", JsonReader.getRateUSD("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?"));
        modelAndView.addObject("unpaidSumOfLoadedGoodsInEUR", getUnpaidSumOfLoadedGoodsInEUR());
        modelAndView.addObject("unpaidSumOfLoadedGoodsInUSD", getUnpaidSumOfLoadedGoodsInUSD());
        // Double rateEUR = JsonReader.getRateEUR("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?");
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

    private BigDecimal getUnpaidSumOfLoadedGoodsInEUR (){
        List<Product> allProducts = productService.allProducts();
        List <BigDecimal> unpaidSumOfEachLoadedAndUnpaidProductInEUR = new ArrayList<>();
        allProducts.forEach(product -> unpaidSumOfEachLoadedAndUnpaidProductInEUR .add(product.getUnpaidSumOfLoadedProductInEUR()));
        return unpaidSumOfEachLoadedAndUnpaidProductInEUR .stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getUnpaidSumOfLoadedGoodsInUSD (){
        List<Product> allProducts = productService.allProducts();
        List <BigDecimal> unpaidSumOfEachLoadedAndUnpaidProductInUSD = new ArrayList<>();
        allProducts.forEach(product -> unpaidSumOfEachLoadedAndUnpaidProductInUSD.add(product.getUnpaidSumOfLoadedProductInUSD()));
        return unpaidSumOfEachLoadedAndUnpaidProductInUSD.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
