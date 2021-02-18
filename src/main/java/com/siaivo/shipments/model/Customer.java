package com.siaivo.shipments.model;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id")
    private int id;

    @Column(name = "customer_name")
    @NotEmpty(message = "*Please provide customer name")
    private String customerName;

    @Column(name = "customer_country")
    @NotEmpty(message = "*Please provide customer country")
    private String customerCountry;

    @Column(name = "customer_type")
    private String customerType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "customer")
    private List<Contract> contracts;

    @Column(name = "comment")
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public List<Contract> getContracts() {
        return contracts;
    }

    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigDecimal getArrears () {
        List <BigDecimal> arrearsSums = new ArrayList<>();
        List <Contract> allContracts = getContracts();
        List <Shipment> allShipments = new ArrayList<>();
        allContracts.forEach(contract -> allShipments.addAll(contract.getShipments()));
        allShipments.forEach(shipment -> {
            try {
                arrearsSums.add(shipment.getArrears());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return arrearsSums.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getUnpaidSumOfOpenContracts () {
        List <BigDecimal> unpaidSums = new ArrayList<>();
        List <Contract> allContracts = getContracts();
        List <Shipment> allShipments = new ArrayList<>();
        allContracts.forEach(contract -> allShipments.addAll(contract.getShipments()));
        allShipments.forEach(shipment -> {
            try {
                unpaidSums.add(shipment.getUnpaidSum());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return unpaidSums.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}