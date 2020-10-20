package com.siaivo.shipments.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private int id;

    @ManyToOne
    @JoinColumn(name="commodity_id")
    private Commodity commodity;

    @Column(name = "product_price")
    @NotEmpty(message = "*Please provide a price")
    private double price;

    @Column(name = "currency")
    private String currency;

    @Column(name = "product_packaging")
    private String packaging;

    @Column(name = "isProductOnPallets")
    private Boolean isProductOnPallets;

    @Column(name = "product_quantity")
    @NotEmpty(message = "*Please provide a quantity")
    private double quantity;



}
