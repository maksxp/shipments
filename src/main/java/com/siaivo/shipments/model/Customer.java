package com.siaivo.shipments.model;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id")
    private int id;

    @Column(name = "customer_name")
    @NotEmpty(message = "*Please provide customer name")
    private int customerName;

    @Column(name = "customer_country")
    @NotEmpty(message = "*Please provide customer country")
    private int customerCountry;

    @Column(name = "customer_type")
    private String customerType;

    @Column(name = "comment")
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerName() {
        return customerName;
    }

    public void setCustomerName(int customerName) {
        this.customerName = customerName;
    }

    public int getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(int customerCountry) {
        this.customerCountry = customerCountry;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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