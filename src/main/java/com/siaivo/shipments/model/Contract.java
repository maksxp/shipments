package com.siaivo.shipments.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "contract")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "contract_id")
    private int id;

    @Column(name = "contract_number")
    @NotEmpty(message = "*Please provide user name")
    private String contractNumber;

    @Column(name = "contract_date")
    @NotEmpty(message = "*Please provide contract date")
    private Date contractDate;

    @Column(name = "comment")
    private String comment;

    @OneToMany
    @JoinColumn(name="product_id")
    private Set<Product> product;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @Column(name = "firstMonthOfShipments")
    @NotEmpty(message = "*Please provide first month of shipments")
    private Calendar firstMonthOfShipments;

    @Column(name = "lastMonthOfShipments")
    @NotEmpty(message = "*Please provide last month of shipments")
    private Calendar lastMonthOfShipments;

    @Column(name = "isContractActive")
    private Boolean isContractActive;

    @OneToMany
    @JoinColumn(name="shipment_id")
    private List<Shipment> shipment;

}
