package com.siaivo.shipments.model;


import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "commodity")
public class Commodity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "commodity_id")
    private int id;

    @Column(name = "commodity_name")
    @NotEmpty(message = "*Please provide commodity name")
    private String commodityName;

    @Column(name = "commodity_code")
    @NotEmpty(message = "*Please provide commodity code")
    private String commodityCode;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "commodity")
    private List<Product> products;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String productName) {
        this.commodityName = productName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getCommodityCode() {
        return commodityCode;
    }

    public void setCommodityCode(String commodityCode) {
        this.commodityCode = commodityCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Commodity commodity = (Commodity) o;
        return id == commodity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "commodityName=" + commodityName +
                '}';
    }
}
