package com.siaivo.shipments.model;

import com.siaivo.shipments.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "contract_id")
    private int id;

    @Column(name = "contract_number")
    private String contractNumber;

    @Column(name = "contract_date")
    private String contractDate;

    @Column(name = "payment_terms")
    private String paymentTerms;

    @Column(name = "delivery_terms")
    private String deliveryTerms;

    @Column(name = "comment")
    private String comment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    private List<Product> products;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id")
    private Customer customer;

    @Column(name = "start_of_shipments")
    private String startOfShipments;

    @Column(name = "end_of_shipments")
    private String endOfShipments;

    @Column(name = "state")
    private String state;

    @OneToMany
    @JoinColumn(name="shipment_id")
    private List<Shipment> shipment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public String getPaymentTerms() {
        return paymentTerms;
    }

    public void setPaymentTerms(String paymentTerms) {
        this.paymentTerms = paymentTerms;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts (List<Product> products) {
        this.products = products;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getStartOfShipments() {
        return startOfShipments;
    }

    public void setStartOfShipments(String startOfShipments) {
        this.startOfShipments = startOfShipments;
    }

    public String getEndOfShipments() {
        return endOfShipments;
    }

    public void setEndOfShipments(String endOfShipments) {
        this.endOfShipments = endOfShipments;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Shipment> getShipments() {
        return shipment;
    }

    public void setShipments(List<Shipment> shipment) {
        this.shipment = shipment;
    }

    public List<Shipment> getShipment() {
        return shipment;
    }

    public String getStringOfAllCommodityNames (Contract contract) {
        StringBuilder str = new StringBuilder();
        contract.getProducts().stream().forEach(product -> str.append(product.getCommodity().getCommodityName()+"/"));
        String stringOfCommodityNames = str.toString().trim().replaceAll(".$", "");
        return stringOfCommodityNames.replaceAll("/","+");
    }
    public String getStringOfUniqueCommodityNames (Contract contract) {
        StringBuilder str = new StringBuilder();
        Set <String> setOfUniqueCommodityNamesInContract = new HashSet<>();
        contract.getProducts().stream().forEach(product -> setOfUniqueCommodityNamesInContract.add(product.getCommodity().getCommodityName()));
        setOfUniqueCommodityNamesInContract.stream().forEach(name -> str.append(name+"/"));
        String stringOfCommodityNames = str.toString().trim().replaceAll(".$", "");
        return stringOfCommodityNames.replaceAll("/","+");
    }

    public BigDecimal getSumOfContract (Contract contract){
        List <BigDecimal> costOfEachProduct = new ArrayList<>();
        contract.getProducts().stream().forEach(product -> costOfEachProduct.add((product.getQuantity()).multiply(product.getPrice())));
        return costOfEachProduct.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String getDeliveryTerms() {
        return deliveryTerms;
    }

    public void setDeliveryTerms(String deliveryTerms) {
        this.deliveryTerms = deliveryTerms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contract contract = (Contract) o;
        return id == contract.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
