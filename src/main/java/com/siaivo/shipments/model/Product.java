package com.siaivo.shipments.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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

    @Column(name = "product_price")
    @NotEmpty(message = "*Please provide a price")
    private double price;

    @Column(name = "currency")
    @NotEmpty(message = "*Please provide currency")
    private String currency;

    @Column(name = "product_packaging")
    @NotEmpty(message = "*Please provide packaging")
    private String packaging;

    @Column(name = "is_product_on_pallets")
    private Boolean isProductOnPallets;

    @Column(name = "product_quantity")
    @NotEmpty(message = "*Please provide a quantity")
    private double quantity;

    @Column(name = "product_batch")
    @NotEmpty(message = "*Please provide batch")
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public Boolean getProductOnPallets() {
        return isProductOnPallets;
    }

    public void setProductOnPallets(Boolean productOnPallets) {
        isProductOnPallets = productOnPallets;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getBatch() { return batch; }

    public void setBatch(String batch) { this.batch = batch; }

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
}
