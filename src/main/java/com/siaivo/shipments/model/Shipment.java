package com.siaivo.shipments.model;

import com.siaivo.shipments.support.LabelsStatus;
import com.siaivo.shipments.support.LogisticInstructionStatus;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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

    @Column(name = "logistic_instruction_status")
    @NotEmpty(message = "*Please provide logistic instruction status")
    private LogisticInstructionStatus logisticInstructionStatus;

    @Column(name = "labels_status")
    @NotEmpty(message = "*Please provide labels status")
    private LabelsStatus labelsStatus;

    @Column(name = "comment")
    private String comment;

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

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Date getPlannedLoadingDate() {
        return plannedLoadingDate;
    }

    public void setPlannedLoadingDate(Date plannedLoadingDate) {
        this.plannedLoadingDate = plannedLoadingDate;
    }

    public Date getActualLoadingDate() {
        return actualLoadingDate;
    }

    public void setActualLoadingDate(Date actualLoadingDate) {
        this.actualLoadingDate = actualLoadingDate;
    }

    public Date getPlannedUnloadingDate() {
        return plannedUnloadingDate;
    }

    public void setPlannedUnloadingDate(Date plannedUnloadingDate) {
        this.plannedUnloadingDate = plannedUnloadingDate;
    }

    public Date getActualUnloadingDate() {
        return actualUnloadingDate;
    }

    public void setActualUnloadingDate(Date actualUnloadingDate) {
        this.actualUnloadingDate = actualUnloadingDate;
    }

    public LogisticInstructionStatus getLogisticInstructionStatus() {
        return logisticInstructionStatus;
    }

    public void setLogisticInstructionStatus(LogisticInstructionStatus logisticInstructionStatus) {
        this.logisticInstructionStatus = logisticInstructionStatus;
    }

    public LabelsStatus getLabelsStatus() {
        return labelsStatus;
    }

    public void setLabelsStatus(LabelsStatus labelsStatus) {
        this.labelsStatus = labelsStatus;
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
        Shipment shipment = (Shipment) o;
        return id == shipment.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
