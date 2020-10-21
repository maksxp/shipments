package com.siaivo.shipments.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Objects;

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

    @Column(name = "invoice_amount")
    private double invoiceAmount;

    @Column(name = "part_one_amount")
    private double partOneAmount;

    @Column(name = "final_part_amount")
    private double finalPartAmount;

    @Column(name = "planned_date_of_full_payment")
    private Date plannedDateOfFullPayment;

    @Column(name = "actual_date_of_full_payment")
    private Date actualDateOfFullPayment;

    @Column(name = "planned_payment_date_of_part_one")
    private Date plannedPaymentDateOfPartOne;

    @Column(name = "actual_payment_date_of_part_one")
    private Date actualPaymentDateOfPartOne;

    @Column(name = "planned_payment_date_of_final_part")
    private Date plannedPaymentDateOfFinalPart;

    @Column(name = "actual_payment_date_of_final_part")
    private Date actualPaymentDateOfFinalPart;

    @Column(name = "comment")
    private String comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public double getPartOneAmount() {
        return partOneAmount;
    }

    public void setPartOneAmount(double partOneAmount) {
        this.partOneAmount = partOneAmount;
    }

    public double getFinalPartAmount() {
        return finalPartAmount;
    }

    public void setFinalPartAmount(double finalPartAmount) {
        this.finalPartAmount = finalPartAmount;
    }

    public Date getPlannedDateOfFullPayment() {
        return plannedDateOfFullPayment;
    }

    public void setPlannedDateOfFullPayment(Date plannedDateOfFullPayment) {
        this.plannedDateOfFullPayment = plannedDateOfFullPayment;
    }

    public Date getActualDateOfFullPayment() {
        return actualDateOfFullPayment;
    }

    public void setActualDateOfFullPayment(Date actualDateOfFullPayment) {
        this.actualDateOfFullPayment = actualDateOfFullPayment;
    }

    public Date getPlannedPaymentDateOfPartOne() {
        return plannedPaymentDateOfPartOne;
    }

    public void setPlannedPaymentDateOfPartOne(Date plannedPaymentDateOfPartOne) {
        this.plannedPaymentDateOfPartOne = plannedPaymentDateOfPartOne;
    }

    public Date getActualPaymentDateOfPartOne() {
        return actualPaymentDateOfPartOne;
    }

    public void setActualPaymentDateOfPartOne(Date actualPaymentDateOfPartOne) {
        this.actualPaymentDateOfPartOne = actualPaymentDateOfPartOne;
    }

    public Date getPlannedPaymentDateOfFinalPart() {
        return plannedPaymentDateOfFinalPart;
    }

    public void setPlannedPaymentDateOfFinalPart(Date plannedPaymentDateOfFinalPart) {
        this.plannedPaymentDateOfFinalPart = plannedPaymentDateOfFinalPart;
    }

    public Date getActualPaymentDateOfFinalPart() {
        return actualPaymentDateOfFinalPart;
    }

    public void setActualPaymentDateOfFinalPart(Date actualPaymentDateOfFinalPart) {
        this.actualPaymentDateOfFinalPart = actualPaymentDateOfFinalPart;
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
        Invoice invoice = (Invoice) o;
        return id == invoice.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
