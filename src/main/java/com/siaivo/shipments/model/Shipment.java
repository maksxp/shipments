package com.siaivo.shipments.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "shipment_id")
    private int id;

    @ManyToOne
    @JoinColumn(name="contract_id")
    private Contract contract;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Invoice invoice;

    @Column(name = "planned_loading_date")
    @NotEmpty(message = "*Please provide planned loading date")
    private Date plannedLoadingDate;

    @Column(name = "actual_loading_date")
    @NotEmpty(message = "*Please provide actual loading date")
    private Date actualLoadingDate;

    @Column(name = "planned_unloading_date")
    @NotEmpty(message = "*Please provide planned unloading date")
    private Date plannedUnloadingDate;

    @Column(name = "actual_unloading_date")
    @NotEmpty(message = "*Please provide actual unloading date")
    private Date actualUnloadingDate;

    @Column(name = "comment")
    private String comment;

}
