package com.siaivo.shipments.model;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        allNotLoadedProductsForShipmentFromThisCommodity.forEach(loadedProductForShipment -> quantityOfEachNotLoadedProductForShipment.add(loadedProductForShipment.getQuantity()));
        return quantityOfEachNotLoadedProductForShipment.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
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
