package com.siaivo.shipments.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

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

    @Column(name = "planned_loading_date")
    @NotEmpty(message = "*Please provide planned loading date")
    private String plannedLoadingDate;

    @Column(name = "actual_loading_date")
    @NotEmpty(message = "*Please provide actual loading date")
    private String actualLoadingDate;

    @Column(name = "planned_unloading_date")
    @NotEmpty(message = "*Please provide planned unloading date")
    private String plannedUnloadingDate;

    @Column(name = "actual_unloading_date")
    @NotEmpty(message = "*Please provide actual unloading date")
    private String actualUnloadingDate;

    @Column(name = "logistic_instruction_status")
    @NotEmpty(message = "*Please provide logistic instruction status")
    private String logisticInstructionStatus;

    @Column(name = "labels_status")
    @NotEmpty(message = "*Please provide labels status")
    private String labelsStatus;

    @Column(name = "comment")
    private String comment;

    @Column(name = "invoice_number")
    @NotEmpty(message = "*Please provide invoice number")
    private String invoiceNumber;

    @Column(name = "invoice_first_part_sum")
    private BigDecimal invoiceFirstPartSum;

    @Column(name = "invoice_second_part_sum")
    private BigDecimal invoiceSecondPartSum;

    @Column(name = "planned_payment_date_of_first_part_sum")
    private String plannedPaymentDateOfFirstPartSum;

    @Column(name = "actual_payment_date_of_first_part_sum")
    private String actualPaymentDateOfFirstPartSum;

    @Column(name = "planned_payment_date_of_second_part_sum")
    private String plannedPaymentDateOfSecondPartSum;

    @Column(name = "actual_payment_date_of_second_part_sum")
    private String actualPaymentDateOfSecondPartSum;

    @Column(name = "planned_payment_date_of_whole_sum")
    private String plannedPaymentDateOfWholeSum;

    @Column(name = "actual_payment_date_of_whole_sum")
    private String actualPaymentDateOfWholeSum;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public String getPlannedLoadingDate() {
        return plannedLoadingDate;
    }

    public void setPlannedLoadingDate(String plannedLoadingDate) {
        this.plannedLoadingDate = plannedLoadingDate;
    }

    public String getActualLoadingDate() {
        return actualLoadingDate;
    }

    public void setActualLoadingDate(String actualLoadingDate) {
        this.actualLoadingDate = actualLoadingDate;
    }

    public String getPlannedUnloadingDate() {
        return plannedUnloadingDate;
    }

    public void setPlannedUnloadingDate(String plannedUnloadingDate) { this.plannedUnloadingDate = plannedUnloadingDate;
    }

    public String getActualUnloadingDate() {
        return actualUnloadingDate;
    }

    public void setActualUnloadingDate(String actualUnloadingDate) {
        this.actualUnloadingDate = actualUnloadingDate;
    }

    public String getLogisticInstructionStatus() {
        return logisticInstructionStatus;
    }

    public void setLogisticInstructionStatus(String logisticInstructionStatus) {
        this.logisticInstructionStatus = logisticInstructionStatus;
    }

    public String getLabelsStatus() {
        return labelsStatus;
    }

    public void setLabelsStatus(String labelsStatus) {
        this.labelsStatus = labelsStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigDecimal getInvoiceFirstPartSum() {
        return invoiceFirstPartSum;
    }

    public void setInvoiceFirstPartSum(BigDecimal invoiceFirstPartSum) {
        this.invoiceFirstPartSum = invoiceFirstPartSum;
    }

    public BigDecimal getInvoiceSecondPartSum() {
        return invoiceSecondPartSum;
    }

    public void setInvoiceSecondPartSum(BigDecimal invoiceSecondPartSum) {
        this.invoiceSecondPartSum = invoiceSecondPartSum;
    }

    public String getPlannedPaymentDateOfFirstPartSum() {
        return plannedPaymentDateOfFirstPartSum;
    }

    public void setPlannedPaymentDateOfFirstPartSum(String plannedPaymentDateOfFirstPartSum) {
        this.plannedPaymentDateOfFirstPartSum = plannedPaymentDateOfFirstPartSum;
    }

    public String getActualPaymentDateOfFirstPartSum() {
        return actualPaymentDateOfFirstPartSum;
    }

    public void setActualPaymentDateOfFirstPartSum(String actualPaymentDateOfFirstPartSum) {
        this.actualPaymentDateOfFirstPartSum = actualPaymentDateOfFirstPartSum;
    }

    public String getPlannedPaymentDateOfSecondPartSum() {
        return plannedPaymentDateOfSecondPartSum;
    }

    public void setPlannedPaymentDateOfSecondPartSum(String plannedPaymentDateOfSecondPartSum) {
        this.plannedPaymentDateOfSecondPartSum = plannedPaymentDateOfSecondPartSum;
    }

    public String getActualPaymentDateOfSecondPartSum() {
        return actualPaymentDateOfSecondPartSum;
    }

    public void setActualPaymentDateOfSecondPartSum(String actualPaymentDateOfSecondPartSum) {
        this.actualPaymentDateOfSecondPartSum = actualPaymentDateOfSecondPartSum;
    }

    public String getPlannedPaymentDateOfWholeSum() {
        return plannedPaymentDateOfWholeSum;
    }

    public void setPlannedPaymentDateOfWholeSum(String plannedPaymentDateOfWholeSum) {
        this.plannedPaymentDateOfWholeSum = plannedPaymentDateOfWholeSum;
    }

    public String getActualPaymentDateOfWholeSum() {
        return actualPaymentDateOfWholeSum;
    }

    public void setActualPaymentDateOfWholeSum(String actualPaymentDateOfWholeSum) {
        this.actualPaymentDateOfWholeSum = actualPaymentDateOfWholeSum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shipment shipment = (Shipment) o;
        return id == shipment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
