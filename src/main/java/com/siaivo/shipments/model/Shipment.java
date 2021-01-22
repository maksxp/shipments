package com.siaivo.shipments.model;

import com.siaivo.shipments.service.ProductForShipmentService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name="contract_id")
    private Contract contract;

    @Column(name = "planned_loading_date")
    private String plannedLoadingDate;

    @Column(name = "actual_loading_date")
    private String actualLoadingDate;

    @Column(name = "planned_unloading_date")
    private String plannedUnloadingDate;

    @Column(name = "actual_unloading_date")
    private String actualUnloadingDate;

    @Column(name = "logistic_instruction_status")
    private String logisticInstructionStatus;

    @Column(name = "labels_status")
    private String labelsStatus;

    @Column(name = "shipment_comment")
    private String shipmentComment;

    @Column(name = "invoice_comment")
    private String invoiceComment;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @Column(name = "truck_number")
    private int truckNumber;

    @Column(name = "invoice_first_part_sum")
    private BigDecimal invoiceFirstPartSum;

    @Column(name = "invoice_second_part_sum")
    private BigDecimal invoiceSecondPartSum;

    @Column(name = "invoice_whole_sum")
    private BigDecimal invoiceWholeSum;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shipment")
    private List<ProductForShipment> productsForShipment;


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

    public String getShipmentComment() {
        return shipmentComment;
    }

    public void setShipmentComment(String shipmentComment) {
        this.shipmentComment = shipmentComment;
    }

    public String getInvoiceComment() {
        return invoiceComment;
    }

    public void setInvoiceComment(String invoiceComment) {
        this.invoiceComment = invoiceComment;
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

    public List<ProductForShipment> getProductsForShipment() {
        return productsForShipment;
    }

    public void setProductsForShipment(List<ProductForShipment> productsForShipment) {
        this.productsForShipment = productsForShipment;
    }

    public int getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(int truckNumber) {
        this.truckNumber = truckNumber;
    }

//    public BigDecimal getInvoiceWholeSum() {
//        return invoiceWholeSum;
//    }
    public BigDecimal getInvoiceWholeSum(Shipment shipment) {
//        shipment.getProductsForShipment().stream().forEach(productForShipment -> productForShipment.getQuantity().multiply(productForShipment.getProduct().getPrice()));
        shipment.getProductsForShipment().stream().reduce()forEach(productForShipment -> productForShipment.getQuantity().multiply(productForShipment.getProduct().getPrice()));
        return invoiceWholeSum;
    }

    public void setInvoiceWholeSum(BigDecimal invoiceWholeSum) {
        this.invoiceWholeSum = invoiceWholeSum;
    }

    public List<String> getListOfGoodsPerShipment (Shipment shipment) {
        List <String> listOfGoodsPerShipment = new ArrayList<>();
        shipment.getProductsForShipment().stream().forEach(productForShipment -> listOfGoodsPerShipment.add(productForShipment.getProduct().getCommodity().getCommodityName()+" ( "+productForShipment.getQuantity()+" тонн"+" ) "+productForShipment.getProduct().getPackaging()));

        return listOfGoodsPerShipment;
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

    @Override
    public String toString() {
        return "Shipment{" +
                "contract=" + contract +
                '}';
    }
}
