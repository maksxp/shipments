package com.siaivo.shipments.controller.salesManagement;

import com.siaivo.shipments.json.JsonReader;
import com.siaivo.shipments.model.Commodity;
import com.siaivo.shipments.model.Product;
import com.siaivo.shipments.service.CommodityService;
import com.siaivo.shipments.service.ProductService;
import com.siaivo.shipments.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SalesManagementCommodityController {

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private ProductService productService;

    @RequestMapping(value="/salesManagement/allCommodities", method = RequestMethod.GET)
    public ModelAndView allCommodities() throws IOException {
        final ModelAndView modelAndView = getModelAndViewWithAllCommodities();
//        modelAndView.addObject("rateEUR", JsonReader.getRateEUR("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?"));
//        modelAndView.addObject("rateUSD", JsonReader.getRateUSD("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?"));
        modelAndView.addObject("unpaidSumOfLoadedGoodsInEUR", getUnpaidSumOfLoadedGoodsInEUR());
        modelAndView.addObject("unpaidSumOfLoadedGoodsInUSD", getUnpaidSumOfLoadedGoodsInUSD());
        modelAndView.addObject("unpaidSumOfLoadedGoodsInUAH", getUnpaidSumOfLoadedGoodsInUAH());
        // Double rateEUR = JsonReader.getRateEUR("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?");
        modelAndView.setViewName("/salesManagement/allCommodities");
        return modelAndView;
    }

//    @RequestMapping(value="/salesManagement/commodityRegistration", method = RequestMethod.GET)
//    public ModelAndView registerNewCommodity(){
//
//        ModelAndView modelAndView = createModelAndView();
//        Commodity commodity = new Commodity();
//        modelAndView.addObject("commodity", commodity);
////        modelAndView.addObject("rateEUR", JsonReader.getRateEUR("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?"));
////        modelAndView.addObject("rateUSD", JsonReader.getRateUSD("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?"));
//        modelAndView.setViewName("/salesManagement/commodityRegistration");
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "salesManagement/commodityRegistration", method = RequestMethod.POST)
//    public ModelAndView registerNewCommodity (@Valid Commodity commodity, BindingResult bindingResult) {
//        ModelAndView modelAndView = createModelAndView();
//        modelAndView.addObject("allCommodities", commodityService.allCommodities());
////        modelAndView.addObject("rateEUR", JsonReader.getRateEUR("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?"));
////        modelAndView.addObject("rateUSD", JsonReader.getRateUSD("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?"));
//        Commodity commodityExists = commodityService.findCommodityByCommodityName(commodity.getCommodityName());
//        if (commodityExists != null) {
//            bindingResult
//                    .rejectValue("commodityName", "error.commodity",
//                            "Товар з такою назвою вже зареєстровано");
//        }
//        if (bindingResult.hasErrors()) {
//            modelAndView.setViewName("/salesSupport/commodityRegistration");
//        } else {
//            commodityService.saveCommodity(commodity);
//            modelAndView.addObject("successMessage", "Товар успішно зареєстровано");
//            modelAndView.addObject("commodity", new Commodity());
//            modelAndView.setViewName("/salesManagement/commodityRegistration");
//
//        }
//        return modelAndView;
//    }
    private ModelAndView getModelAndViewWithAllCommodities (){
        ModelAndView modelAndView = createModelAndView();
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

    private BigDecimal getUnpaidSumOfLoadedGoodsInUAH (){
        List<Product> allProducts = productService.allProducts();
        List <BigDecimal> unpaidSumOfEachLoadedAndUnpaidProductInUAH = new ArrayList<>();
        allProducts.forEach(product -> unpaidSumOfEachLoadedAndUnpaidProductInUAH.add(product.getUnpaidSumOfLoadedProductInUAH()));
        return unpaidSumOfEachLoadedAndUnpaidProductInUAH.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private ModelAndView createModelAndView (){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("rateEUR", JsonReader.getRateEUR("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?"));
        modelAndView.addObject("rateUSD", JsonReader.getRateUSD("https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json?"));
        return modelAndView;
    }
}
