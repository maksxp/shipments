package com.siaivo.shipments.model;

import javax.persistence.*;
import java.math.BigDecimal;

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
}
