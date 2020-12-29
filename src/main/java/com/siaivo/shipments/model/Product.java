package com.siaivo.shipments.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private int id;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="commodity_id")
    private Commodity commodity;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="contract_id")
    private Contract contract;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="shipment_id")
    private Shipment shipment;

    @Column(name = "product_price")
    private BigDecimal price;

    @Column(name = "currency")
    private String currency;

    @Column(name = "product_packaging")
    private String packaging;

    @Column(name = "is_product_on_pallets")
    private Boolean isProductOnPallets;

    @Column(name = "product_quantity")
    private BigDecimal quantity;

    @Column(name = "product_batch")
    private String batch;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public void setCommodity(Commodity commodity) {
        this.commodity = commodity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getBatch() { return batch; }

    public void setBatch(String batch) { this.batch = batch; }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Boolean getProductOnPallets() {
        return isProductOnPallets;
    }

    public void setProductOnPallets(Boolean productOnPallets) {
        isProductOnPallets = productOnPallets;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id == product.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", commodity=" + commodity +
                ", contract=" + contract +
                ", packaging='" + packaging + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
