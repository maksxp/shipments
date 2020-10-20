package com.siaivo.shipments.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Table(name = "invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "invoice_id")
    private int id;

    @Column(name = "invoice_number")
    @NotEmpty(message = "*Please provide invoice number")
    private String invoiceNumber;

    @Column(name = "planned_payment_date#1")
    @NotEmpty(message = "*Please provide planned loading date")
    private Date plannedPaymentDate;

    @Column(name = "actual_payment_date#1")
    @NotEmpty(message = "*Please provide actual loading date")
    private Date actualLoadingDate;

    @Column(name = "planned_unloading_date")
    @NotEmpty(message = "*Please provide planned unloading date")
    private Date plannedUnloadingDate;

    @Column(name = "actual_unloading_date")
    @NotEmpty(message = "*Please provide actual unloading date")
    private Date actualUnloadingDate;
}
