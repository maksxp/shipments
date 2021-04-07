package com.siaivo.shipments.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class ProductForShipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    Product product;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="shipment_id")
    Shipment shipment;

    public ProductForShipment (){

    }

    public ProductForShipment (Product product) {
        setProduct(product);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public String getPaymentTerms () {
        Contract contract = shipment.getContract();
        return contract.getPaymentTerms();
    }

    public BigDecimal getUnpaidSumInEUR () {
        BigDecimal unpaidSumInEUR = BigDecimal.ZERO;
        if ("EUR".equals(product.getCurrency())){
            unpaidSumInEUR = getUnpaidQuantity().multiply(product.getPrice());
        }
        return unpaidSumInEUR;
    }

    public BigDecimal getUnpaidSumInUSD () {
        BigDecimal unpaidSumInUSD = BigDecimal.ZERO;
        if ("USD".equals(product.getCurrency())){
            unpaidSumInUSD = getUnpaidQuantity().multiply(product.getPrice());
        }
        return unpaidSumInUSD;
    }

    public BigDecimal getUnpaidSumInUAH () {
        BigDecimal unpaidSumInUAH = BigDecimal.ZERO;
        if ("UAH".equals(product.getCurrency())){
            unpaidSumInUAH = getUnpaidQuantity().multiply(product.getPrice());
        }
        return unpaidSumInUAH;
    }

    public BigDecimal getUnpaidQuantity () {
        BigDecimal unpaidQuantity = BigDecimal.ZERO;
        if ("оплата частинами".equals(getPaymentTerms())) {
            if (isFirstAndSecondPartSumUnpaid()) {
                unpaidQuantity = getQuantity();
                return unpaidQuantity;
            } else if (!isFirstPartSumPaid()) {
                unpaidQuantity = quantity.multiply(shipment.getInvoiceFirstPartSum().divide(shipment.getInvoiceWholeSum(), 3, RoundingMode.HALF_UP));
                return unpaidQuantity;
            } else if (!isSecondPartSumPaid()) {
                unpaidQuantity = quantity.multiply(shipment.getInvoiceSecondPartSum().divide(shipment.getInvoiceWholeSum(), 3, RoundingMode.HALF_UP));
                return unpaidQuantity;
            }
        }
        if (!isWholeSumPaid()) {
            unpaidQuantity = getQuantity();

        }
        return unpaidQuantity;
    }

    public BigDecimal getLoadedAndUnpaidQuantity () {
        BigDecimal loadedAndUnpaidQuantity = BigDecimal.ZERO;
        if ("оплата частинами".equals(getPaymentTerms())) {
            if (isLoaded() && isFirstAndSecondPartSumUnpaid()) {
                loadedAndUnpaidQuantity = getQuantity();
                return loadedAndUnpaidQuantity;
            } else if (isLoaded() && !isFirstPartSumPaid()) {
                loadedAndUnpaidQuantity = quantity.multiply(shipment.getInvoiceFirstPartSum().divide(shipment.getInvoiceWholeSum(), 3, RoundingMode.HALF_UP));
                return loadedAndUnpaidQuantity;
            } else if (isLoaded() && !isSecondPartSumPaid()) {
                loadedAndUnpaidQuantity = quantity.multiply(shipment.getInvoiceSecondPartSum().divide(shipment.getInvoiceWholeSum(), 3, RoundingMode.HALF_UP));
                return loadedAndUnpaidQuantity;
            }
        }
        if (isLoaded() && !isWholeSumPaid()) {
            loadedAndUnpaidQuantity = getQuantity();
        }
        return loadedAndUnpaidQuantity;
    }

    public boolean isLoaded (){
        boolean isLoaded = true;
        if (null == shipment.getActualLoadingDate()){
            isLoaded = false;
        } else if ("".equals(shipment.getActualLoadingDate())){
            isLoaded = false;
        }
        return isLoaded;
    }

    private boolean isWholeSumPaid (){
        boolean isWholeSumPaid = true;
        if (null == shipment.getActualPaymentDateOfWholeSum()){
            isWholeSumPaid = false;
        } else if ("".equals(shipment.getActualPaymentDateOfWholeSum())){
            isWholeSumPaid = false;
        }
        return isWholeSumPaid;
    }

    private boolean isFirstPartSumPaid (){
        boolean isFirstPartSumPaid = true;
        if (null == shipment.getActualPaymentDateOfFirstPartSum()){
            isFirstPartSumPaid = false;
        } else if ("".equals(shipment.getActualPaymentDateOfFirstPartSum())){
            isFirstPartSumPaid = false;
        }
        return isFirstPartSumPaid;
    }

    private boolean isSecondPartSumPaid (){
        boolean isSecondPartSumPaid = true;
        if (null == shipment.getActualPaymentDateOfSecondPartSum()){
            isSecondPartSumPaid = false;
        } else if ("".equals(shipment.getActualPaymentDateOfSecondPartSum())){
            isSecondPartSumPaid = false;
        }
        return isSecondPartSumPaid;
    }

    private boolean isFirstAndSecondPartSumUnpaid () {
       return !isFirstPartSumPaid()&&!isSecondPartSumPaid();
    }


    public boolean isAnySumPaid () {
        boolean isAnySumPaid=false;
        if (isFirstPartSumPaid()||isSecondPartSumPaid()||isWholeSumPaid()){
            isAnySumPaid=true;
        }
        return isAnySumPaid;
    }

}
