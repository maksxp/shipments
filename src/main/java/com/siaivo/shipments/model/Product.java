package com.siaivo.shipments.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private int id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="commodity_id")
    private Commodity commodity;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="contract_id")
    private Contract contract;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<ProductForShipment> productsForShipments;

    @Column(name = "product_price")
    private BigDecimal price;

    @Column(name = "currency")
    private String currency;

    @Column(name = "product_packaging")
    private String packaging;

    @Column(name = "product_quantity")
    private BigDecimal quantity;

    @Column(name = "product_batch")
    private String batch;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setQuantityAfterChangeRequest (BigDecimal quantity) {
        BigDecimal loadedQuantity = getLoadedQuantity();
        if (loadedQuantity.compareTo(quantity)<0) {
        this.quantity = quantity;}
        else {
            this.quantity = loadedQuantity;
        }
    }

    public String getBatch() { return batch; }

    public void setBatch(String batch) { this.batch = batch; }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public List<ProductForShipment> getProductsForShipments() {
        return productsForShipments;
    }

    public void setProductsForShipments(List<ProductForShipment> productsForShipments) {
        this.productsForShipments = productsForShipments;
    }

    public BigDecimal getLoadedQuantity (){
        List <BigDecimal> quantityOfLoadedProductInEachShipment = new ArrayList<>();
        getProductsForShipments()
                .stream()
                .filter(productForShipment -> productForShipment.getShipment().getActualLoadingDate()!=null && !productForShipment.getShipment().getActualLoadingDate().equals(""))
                .collect(Collectors.toList())
                .forEach(productForShipment -> quantityOfLoadedProductInEachShipment.add(productForShipment.getQuantity()));
        return quantityOfLoadedProductInEachShipment
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

//    public BigDecimal getLoadedAndUnpaidQuantity (){
//        List <BigDecimal> quantityOfLoadedAndUnpaidProductInEachShipment = new ArrayList<>();
//        getProductsForShipments()
//                .stream()
//                .filter(productForShipment -> productForShipment.getShipment().getActualLoadingDate()!=null && !productForShipment.getShipment().getActualLoadingDate().equals(""))
//                .filter(productForShipment -> productForShipment.getShipment().getActualPaymentDateOfWholeSum()==null || productForShipment.getShipment().getActualPaymentDateOfWholeSum().equals(""))
//                .collect(Collectors.toList())
//                .forEach(productForShipment -> quantityOfLoadedAndUnpaidProductInEachShipment.add(productForShipment.getQuantity()));
//        return quantityOfLoadedAndUnpaidProductInEachShipment
//                .stream()
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }

    public BigDecimal getLoadedAndUnpaidQuantity (){
        List <BigDecimal> quantityOfLoadedAndUnpaidProductsForShipment = new ArrayList<>();
        getProductsForShipments()
                .forEach(productForShipment -> quantityOfLoadedAndUnpaidProductsForShipment.add(productForShipment.getLoadedAndUnpaidQuantity()));

        return quantityOfLoadedAndUnpaidProductsForShipment
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getUnpaidSumOfLoadedProductInEUR (){
        BigDecimal unpaidSumOfLoadedProductInEUR = BigDecimal.ZERO;
        if (currency.equals("EUR")){
            List <BigDecimal> unpaidSumOfLoadedAndUnpaidProductInEachShipment = new ArrayList<>();
            getProductsForShipments()
                    .stream()
                    .filter(productForShipment -> productForShipment.getShipment().getActualLoadingDate()!=null && !productForShipment.getShipment().getActualLoadingDate().equals(""))
                    .filter(productForShipment -> productForShipment.getShipment().getActualPaymentDateOfWholeSum()==null || productForShipment.getShipment().getActualPaymentDateOfWholeSum().equals(""))
                    .collect(Collectors.toList())
                    .forEach(productForShipment -> unpaidSumOfLoadedAndUnpaidProductInEachShipment.add(productForShipment.getQuantity().multiply(price)));

            unpaidSumOfLoadedProductInEUR = unpaidSumOfLoadedAndUnpaidProductInEachShipment
                    .stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return unpaidSumOfLoadedProductInEUR;
    }

    public BigDecimal getUnpaidSumOfLoadedProductInUSD (){
        BigDecimal unpaidSumOfLoadedProductInUSD = BigDecimal.ZERO;
        if (this.currency.equals("USD")){
            List <BigDecimal> unpaidSumOfLoadedAndUnpaidProductInEachShipment = new ArrayList<>();
            getProductsForShipments()
                    .stream()
                    .filter(productForShipment -> productForShipment.getShipment().getActualLoadingDate()!=null && !productForShipment.getShipment().getActualLoadingDate().equals(""))
                    .filter(productForShipment -> productForShipment.getShipment().getActualPaymentDateOfWholeSum()==null || productForShipment.getShipment().getActualPaymentDateOfWholeSum().equals(""))
                    .collect(Collectors.toList())
                    .forEach(productForShipment -> unpaidSumOfLoadedAndUnpaidProductInEachShipment.add(productForShipment.getQuantity().multiply(price)));

            unpaidSumOfLoadedProductInUSD = unpaidSumOfLoadedAndUnpaidProductInEachShipment
                    .stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return unpaidSumOfLoadedProductInUSD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return commodity.equals(product.commodity) && price.equals(product.price) && currency.equals(product.currency) && packaging.equals(product.packaging)  && batch.equals(product.batch) && contract.equals(product.contract);
    }

    @Override
    public String toString() {
        return "Product{" +
                "commodity=" + commodity +
                ", contract=" + contract +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", quantity='" + quantity + '\'' +
                ", packaging='" + packaging + '\'' +
                ", batch='" + batch + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(commodity, price, currency, packaging, batch, contract);
    }
}
