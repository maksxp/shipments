package com.siaivo.shipments.model;


import com.siaivo.shipments.service.ShipmentService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "commodity")
public class Commodity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "commodity_id")
    private int id;

    @Column(name = "commodity_name")
    @NotEmpty(message = "*Please provide commodity name")
    private String commodityName;

    @Column(name = "commodity_code")
    @NotEmpty(message = "*Please provide commodity code")
    private String commodityCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "commodity")
    private List<Product> products;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String productName) {
        this.commodityName = productName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    public BigDecimal getQuantityOfAllLoadedGoods (){
        List <ProductForShipment> allLoadedProductsForShipmentFromThisCommodity;
        List <Product> allProductsFromThisCommodity = products;
        List <ProductForShipment> allProductsForShipmentFromThisCommodity = new ArrayList<>();
        allProductsFromThisCommodity.forEach(product -> allProductsForShipmentFromThisCommodity.addAll(product.getProductsForShipments()));
//        allLoadedProductsForShipmentFromThisCommodity = allProductsForShipmentFromThisCommodity.stream().filter(productForShipment -> productForShipment.getShipment().getActualLoadingDate()!=null && !productForShipment.getShipment().getActualLoadingDate().equals("")).collect(Collectors.toList());
        allLoadedProductsForShipmentFromThisCommodity = allProductsForShipmentFromThisCommodity.stream().filter(ProductForShipment::isLoaded).collect(Collectors.toList());
        List <BigDecimal> quantityOfEachLoadedProductForShipment = new ArrayList<>();
        allLoadedProductsForShipmentFromThisCommodity.forEach(loadedProductForShipment -> quantityOfEachLoadedProductForShipment.add(loadedProductForShipment.getQuantity()));
        return quantityOfEachLoadedProductForShipment.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getQuantityOfPreviousMYLoadedGoods (){
        List <ProductForShipment> allLoadedProductsForShipmentFromThisCommodity;
        List <Product> allProductsFromThisCommodity = products;
        List <ProductForShipment> allProductsForShipmentFromThisCommodity = new ArrayList<>();
        allProductsFromThisCommodity.forEach(product -> allProductsForShipmentFromThisCommodity.addAll(product.getProductsForShipments()));
//        allLoadedProductsForShipmentFromThisCommodity = allProductsForShipmentFromThisCommodity.stream().filter(productForShipment -> productForShipment.getShipment().getActualLoadingDate()!=null && !productForShipment.getShipment().getActualLoadingDate().equals("")).collect(Collectors.toList());
        allLoadedProductsForShipmentFromThisCommodity = allProductsForShipmentFromThisCommodity.stream().filter(ProductForShipment::isPreviousMYLoaded).collect(Collectors.toList());
        List <BigDecimal> quantityOfEachLoadedProductForShipment = new ArrayList<>();
        allLoadedProductsForShipmentFromThisCommodity.forEach(loadedProductForShipment -> quantityOfEachLoadedProductForShipment.add(loadedProductForShipment.getQuantity()));
        return quantityOfEachLoadedProductForShipment.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getQuantityOfThisMYLoadedGoods () {

        List <ProductForShipment> allLoadedProductsForShipmentFromThisCommodity;
        List <Product> allProductsFromThisCommodity = products;
        List <ProductForShipment> allProductsForShipmentFromThisCommodity = new ArrayList<>();
        allProductsFromThisCommodity.forEach(product -> allProductsForShipmentFromThisCommodity.addAll(product.getProductsForShipments()));
//        allLoadedProductsForShipmentFromThisCommodity = allProductsForShipmentFromThisCommodity.stream().filter(productForShipment -> productForShipment.getShipment().getActualLoadingDate()!=null && !productForShipment.getShipment().getActualLoadingDate().equals("")).collect(Collectors.toList());
        allLoadedProductsForShipmentFromThisCommodity = allProductsForShipmentFromThisCommodity.stream().filter(ProductForShipment::isThisMYLoaded).collect(Collectors.toList());
        List <BigDecimal> quantityOfEachLoadedProductForShipment = new ArrayList<>();
        allLoadedProductsForShipmentFromThisCommodity.forEach(loadedProductForShipment -> quantityOfEachLoadedProductForShipment.add(loadedProductForShipment.getQuantity()));
        return quantityOfEachLoadedProductForShipment.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getUnpaidSumOfLoadedGoodsInEUR (){
        List <Product> allProductsFromThisCommodity = products;
        List <BigDecimal> unpaidSumOfEachLoadedAndUnpaidProduct = new ArrayList<>();
        allProductsFromThisCommodity.forEach(product -> unpaidSumOfEachLoadedAndUnpaidProduct.add(product.getUnpaidSumOfLoadedProductInEUR()));
        return unpaidSumOfEachLoadedAndUnpaidProduct.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getUnpaidSumOfLoadedGoodsInUSD (){
        List <Product> allProductsFromThisCommodity = products;
        List <BigDecimal> unpaidSumOfEachLoadedAndUnpaidProduct = new ArrayList<>();
        allProductsFromThisCommodity.forEach(product -> unpaidSumOfEachLoadedAndUnpaidProduct.add(product.getUnpaidSumOfLoadedProductInUSD ()));
        return unpaidSumOfEachLoadedAndUnpaidProduct.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getUnpaidSumOfLoadedGoodsInUAH (){
        List <Product> allProductsFromThisCommodity = products;
        List <BigDecimal> unpaidSumOfEachLoadedAndUnpaidProduct = new ArrayList<>();
        allProductsFromThisCommodity.forEach(product -> unpaidSumOfEachLoadedAndUnpaidProduct.add(product.getUnpaidSumOfLoadedProductInUAH ()));
        return unpaidSumOfEachLoadedAndUnpaidProduct.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getQuantityOfAllLoadedAndUnpaidGoods (){
        List <Product> allProductsFromThisCommodity = products;
        List <BigDecimal> quantityOfEachLoadedAndUnpaidProduct = new ArrayList<>();
        allProductsFromThisCommodity.forEach(product -> quantityOfEachLoadedAndUnpaidProduct.add(product.getLoadedAndUnpaidQuantity()));
        return quantityOfEachLoadedAndUnpaidProduct.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getQuantityOfAllNotLoadedGoods (){
        List <ProductForShipment> allNotLoadedProductsForShipmentFromThisCommodity = new ArrayList<>();
        List <Product> allProductsFromThisCommodity = products;
        List <ProductForShipment> allProductsForShipmentFromThisCommodity = new ArrayList<>();
        allProductsFromThisCommodity.forEach(product -> allProductsForShipmentFromThisCommodity.addAll(product.getProductsForShipments()));
        allNotLoadedProductsForShipmentFromThisCommodity = allProductsForShipmentFromThisCommodity.stream().filter(productForShipment -> productForShipment.getShipment().getActualLoadingDate()==null || productForShipment.getShipment().getActualLoadingDate().equals("")).collect(Collectors.toList());
        List <BigDecimal> quantityOfEachNotLoadedProductForShipment = new ArrayList<>();
        allNotLoadedProductsForShipmentFromThisCommodity.forEach(notLoadedProductForShipment -> quantityOfEachNotLoadedProductForShipment.add(notLoadedProductForShipment.getQuantity()));
        return quantityOfEachNotLoadedProductForShipment.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getQuantityOfThisWeekNotLoadedGoods () {
        List <ProductForShipment> thisWeekNotLoadedProductsForShipmentFromThisCommodity = new ArrayList<>();
        List <Product> allProductsFromThisCommodity = products;
        List <ProductForShipment> allProductsForShipmentFromThisCommodity = new ArrayList<>();
        allProductsFromThisCommodity.forEach(product -> allProductsForShipmentFromThisCommodity.addAll(product.getProductsForShipments()));
        thisWeekNotLoadedProductsForShipmentFromThisCommodity = allProductsForShipmentFromThisCommodity.stream().filter(productForShipment -> {
            try {
                return getCurrentWeekNumber() == productForShipment.getShipment().getWeekOfPlannedLoadingDate();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).filter(productForShipment -> productForShipment.getShipment().getActualLoadingDate()==null || productForShipment.getShipment().getActualLoadingDate().equals("")).collect(Collectors.toList());
        List <BigDecimal> quantityOfEachNotLoadedProductForShipment = new ArrayList<>();
        thisWeekNotLoadedProductsForShipmentFromThisCommodity.forEach(notLoadedProductForShipment -> quantityOfEachNotLoadedProductForShipment.add(notLoadedProductForShipment.getQuantity()));
        return quantityOfEachNotLoadedProductForShipment.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getQuantityOfThisMonthNotLoadedGoods () {
        List <ProductForShipment> thisMonthNotLoadedProductsForShipmentFromThisCommodity = new ArrayList<>();
        List <Product> allProductsFromThisCommodity = products;
        List <ProductForShipment> allProductsForShipmentFromThisCommodity = new ArrayList<>();
        allProductsFromThisCommodity.forEach(product -> allProductsForShipmentFromThisCommodity.addAll(product.getProductsForShipments()));
        thisMonthNotLoadedProductsForShipmentFromThisCommodity = allProductsForShipmentFromThisCommodity.stream().filter(productForShipment -> {
            try {
                return getCurrentMonthNumber() == productForShipment.getShipment().getMonthOfPlannedLoadingDate();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).filter(productForShipment -> productForShipment.getShipment().getActualLoadingDate()==null || productForShipment.getShipment().getActualLoadingDate().equals("")).collect(Collectors.toList());
        List <BigDecimal> quantityOfEachNotLoadedProductForShipment = new ArrayList<>();
        thisMonthNotLoadedProductsForShipmentFromThisCommodity.forEach(notLoadedProductForShipment -> quantityOfEachNotLoadedProductForShipment.add(notLoadedProductForShipment.getQuantity()));
        return quantityOfEachNotLoadedProductForShipment.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getQuantityOfNextWeekNotLoadedGoods (){
        List <ProductForShipment> nextWeekNotLoadedProductsForShipmentFromThisCommodity = new ArrayList<>();
        List <Product> allProductsFromThisCommodity = products;
        List <ProductForShipment> allProductsForShipmentFromThisCommodity = new ArrayList<>();
        allProductsFromThisCommodity.forEach(product -> allProductsForShipmentFromThisCommodity.addAll(product.getProductsForShipments()));
        nextWeekNotLoadedProductsForShipmentFromThisCommodity = allProductsForShipmentFromThisCommodity.stream().filter(productForShipment -> {
            try {
                return getNextWeekNumber() == productForShipment.getShipment().getWeekOfPlannedLoadingDate();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).filter(productForShipment -> productForShipment.getShipment().getActualLoadingDate()==null || productForShipment.getShipment().getActualLoadingDate().equals("")).collect(Collectors.toList());
        List <BigDecimal> quantityOfEachNotLoadedProductForShipment = new ArrayList<>();
        nextWeekNotLoadedProductsForShipmentFromThisCommodity.forEach(notLoadedProductForShipment -> quantityOfEachNotLoadedProductForShipment.add(notLoadedProductForShipment.getQuantity()));
        return quantityOfEachNotLoadedProductForShipment.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getQuantityOfNextMonthNotLoadedGoods (){
        List <ProductForShipment> nextMonthNotLoadedProductsForShipmentFromThisCommodity = new ArrayList<>();
        List <Product> allProductsFromThisCommodity = products;
        List <ProductForShipment> allProductsForShipmentFromThisCommodity = new ArrayList<>();
        allProductsFromThisCommodity.forEach(product -> allProductsForShipmentFromThisCommodity.addAll(product.getProductsForShipments()));
        nextMonthNotLoadedProductsForShipmentFromThisCommodity = allProductsForShipmentFromThisCommodity.stream().filter(productForShipment -> {
            try {
                return getNextMonthNumber() == productForShipment.getShipment().getMonthOfPlannedLoadingDate();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        }).filter(productForShipment -> productForShipment.getShipment().getActualLoadingDate()==null || productForShipment.getShipment().getActualLoadingDate().equals("")).collect(Collectors.toList());
        List <BigDecimal> quantityOfEachNotLoadedProductForShipment = new ArrayList<>();
        nextMonthNotLoadedProductsForShipmentFromThisCommodity.forEach(notLoadedProductForShipment -> quantityOfEachNotLoadedProductForShipment.add(notLoadedProductForShipment.getQuantity()));
        return quantityOfEachNotLoadedProductForShipment.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

        private int getCurrentWeekNumber () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        Date today = new Date();
        calendar.setTime(today);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    private int getCurrentMonthNumber() {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        Date today = new Date();
        calendar.setTime(today);
        return calendar.get(Calendar.MONTH);
    }

    private int getNextWeekNumber () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        Date today = new Date();
        calendar.setTime(today);
        calendar.add(Calendar.DATE, 7);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    private int getNextMonthNumber () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        Date today = new Date();
        calendar.setTime(today);
        calendar.add(Calendar.MONTH, 1);
        return calendar.get(Calendar.MONTH);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commodity commodity = (Commodity) o;
        return commodityName.equals(commodity.commodityName) && commodityCode.equals(commodity.commodityCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commodityName, commodityCode);
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "commodityName=" + commodityName +
                '}';
    }
}
