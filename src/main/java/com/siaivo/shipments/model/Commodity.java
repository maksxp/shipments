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

    public BigDecimal getQuantityOfAllLoadedGoods (Commodity commodity){
        List <ProductForShipment> allLoadedProductsForShipmentFromThisCommodity;
        List <Product> allProductsFromThisCommodity = commodity.getProducts();
        List <ProductForShipment> allProductsForShipmentFromThisCommodity = new ArrayList<>();
        allProductsFromThisCommodity.forEach(product -> allProductsForShipmentFromThisCommodity.addAll(product.getProductsForShipments()));
        allLoadedProductsForShipmentFromThisCommodity = allProductsForShipmentFromThisCommodity.stream().filter(productForShipment -> productForShipment.getShipment().getActualLoadingDate()!=null && !productForShipment.getShipment().getActualLoadingDate().equals("")).collect(Collectors.toList());
        List <BigDecimal> quantityOfEachLoadedProductForShipment = new ArrayList<>();
        allLoadedProductsForShipmentFromThisCommodity.forEach(loadedProductForShipment -> quantityOfEachLoadedProductForShipment.add(loadedProductForShipment.getQuantity()));
        return quantityOfEachLoadedProductForShipment.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getQuantityOfAllNotLoadedGoods (Commodity commodity){
        List <ProductForShipment> allNotLoadedProductsForShipmentFromThisCommodity = new ArrayList<>();
        List <Product> allProductsFromThisCommodity = commodity.getProducts();
        List <ProductForShipment> allProductsForShipmentFromThisCommodity = new ArrayList<>();
        allProductsFromThisCommodity.forEach(product -> allProductsForShipmentFromThisCommodity.addAll(product.getProductsForShipments()));
        allNotLoadedProductsForShipmentFromThisCommodity = allProductsForShipmentFromThisCommodity.stream().filter(productForShipment -> productForShipment.getShipment().getActualLoadingDate()==null || productForShipment.getShipment().getActualLoadingDate().equals("")).collect(Collectors.toList());
        List <BigDecimal> quantityOfEachNotLoadedProductForShipment = new ArrayList<>();
        allNotLoadedProductsForShipmentFromThisCommodity.forEach(notLoadedProductForShipment -> quantityOfEachNotLoadedProductForShipment.add(notLoadedProductForShipment.getQuantity()));
        return quantityOfEachNotLoadedProductForShipment.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getQuantityOfThisWeekNotLoadedGoods (Commodity commodity) {
        List <ProductForShipment> thisWeekNotLoadedProductsForShipmentFromThisCommodity = new ArrayList<>();
        List <Product> allProductsFromThisCommodity = commodity.getProducts();
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

    public BigDecimal getQuantityOfNextWeekNotLoadedGoods (Commodity commodity){
        return BigDecimal.ZERO;
    }

        private int getCurrentWeekNumber () {
        Calendar calendar = new GregorianCalendar(Locale.FRANCE);
        Date today = new Date();
        calendar.setTime(today);
        return calendar.get(Calendar.WEEK_OF_YEAR);
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
