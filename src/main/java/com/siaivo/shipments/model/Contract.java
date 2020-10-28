package com.siaivo.shipments.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
    private Date contractDate;

    @Column(name = "payment_terms")
    @NotEmpty(message = "*Please provide payment terms")
    private String paymentTerms;

    @Column(name = "comment")
    private String comment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "contract")
    private List<Product> products;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="customer_id")
    private Customer customer;

    @Column(name = "first_month_of_shipments")
    @NotEmpty(message = "*Please provide first month of shipments")
    private Calendar firstMonthOfShipments;

    @Column(name = "last_month_of_shipments")
    @NotEmpty(message = "*Please provide last month of shipments")
    private Calendar lastMonthOfShipments;

    @Column(name = "is_contract_active")
    private Boolean isContractActive;

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

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
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

    public Calendar getFirstMonthOfShipments() {
        return firstMonthOfShipments;
    }

    public void setFirstMonthOfShipments(Calendar firstMonthOfShipments) {
        this.firstMonthOfShipments = firstMonthOfShipments;
    }

    public Calendar getLastMonthOfShipments() {
        return lastMonthOfShipments;
    }

    public void setLastMonthOfShipments(Calendar lastMonthOfShipments) {
        this.lastMonthOfShipments = lastMonthOfShipments;
    }

    public Boolean getContractActive() {
        return isContractActive;
    }

    public void setContractActive(Boolean contractActive) {
        isContractActive = contractActive;
    }

    public List<Shipment> getShipment() {
        return shipment;
    }

    public void setShipment(List<Shipment> shipment) {
        this.shipment = shipment;
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
