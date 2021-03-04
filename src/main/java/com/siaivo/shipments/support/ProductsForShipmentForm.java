package com.siaivo.shipments.support;

import com.siaivo.shipments.model.ProductForShipment;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProductsForShipmentForm {

    List <ProductForShipment> productsForShipment;

    public List<ProductForShipment> getProductsForShipment() {
        return productsForShipment;
    }

    public void setProductsForShipment(List<ProductForShipment> productsForShipment) {
        this.productsForShipment = productsForShipment;
    }

    public ProductsForShipmentForm (int numberOfProductsForShipments) {
        productsForShipment = IntStream.range(0, numberOfProductsForShipments).mapToObj(productForShipment -> new ProductForShipment()).collect(Collectors.toList());
    }
}
