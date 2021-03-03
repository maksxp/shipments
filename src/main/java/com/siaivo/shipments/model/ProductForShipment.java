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

    public BigDecimal getLoadedAndUnpaidQuantity () {
        BigDecimal loadedAndUnpaidQuantity = BigDecimal.ZERO;
        switch (shipment.getContract().getPaymentTerms()) {
            case ("оплата частинами"):
                if (isLoaded()&&isFirstAndSecondPartSumUnpaid()){
                    loadedAndUnpaidQuantity = getQuantity();
                    break;
                } else if (isLoaded()&&!isFirstPartSumPaid()){
//                    loadedAndUnpaidQuantity = (shipment.getInvoiceSecondPartSum().divide(shipment.getInvoiceWholeSum(),2, RoundingMode.HALF_UP)).divide(product.getPrice(),2, RoundingMode.HALF_UP);
                    loadedAndUnpaidQuantity = (shipment.getInvoiceWholeSum().subtract(shipment.getInvoiceSecondPartSum())).divide(product.getPrice(),2, RoundingMode.HALF_UP);
                    loadedAndUnpaidQuantity = quantity.multiply(shipment.getInvoiceSecondPartSum().divide(shipment.getInvoiceWholeSum()));
                    break;
                } else if (isLoaded()&&!isSecondPartSumPaid()){
//                    loadedAndUnpaidQuantity = (shipment.getInvoiceFirstPartSum().divide(shipment.getInvoiceWholeSum(), 2, RoundingMode.HALF_UP)).divide(product.getPrice(), 2, RoundingMode.HALF_UP);
//                    loadedAndUnpaidQuantity = (shipment.getInvoiceSecondPartSum()).divide(product.getPrice(),2, RoundingMode.HALF_UP);
                    loadedAndUnpaidQuantity = quantity.multiply(shipment.getInvoiceFirstPartSum().divide(shipment.getInvoiceWholeSum()));
                    break;
                }
            default:
                if (isLoaded()&&!isWholeSumPaid()){
                    loadedAndUnpaidQuantity = getQuantity();
                    break;
                }
        }
        return loadedAndUnpaidQuantity;
    }

    private Boolean isLoaded (){
        return shipment.getActualLoadingDate() != null && !shipment.getActualLoadingDate().equals("");
    }

    private Boolean isWholeSumPaid (){
        return shipment.getActualPaymentDateOfWholeSum()!=null && !shipment.getActualPaymentDateOfWholeSum().equals("");
    }

    private Boolean isFirstPartSumPaid (){
        return shipment.getActualPaymentDateOfFirstPartSum()!=null && !shipment.getActualPaymentDateOfFirstPartSum().equals("");
    }

    private Boolean isSecondPartSumPaid (){
        return shipment.getActualPaymentDateOfSecondPartSum()!=null && !shipment.getActualPaymentDateOfSecondPartSum().equals("");
    }

    private Boolean isFirstAndSecondPartSumUnpaid () {
       return !isFirstPartSumPaid()&&!isSecondPartSumPaid();
    }

}
