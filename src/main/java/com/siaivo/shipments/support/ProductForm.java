package com.siaivo.shipments.support;

import com.siaivo.shipments.model.Product;
import java.util.List;

public class ProductForm {
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public ProductForm(List<Product> products) {
        super();
        this.products = products;
    }

    public ProductForm() {
        super();
    }

    @Override
    public String toString() {
        return "ProductForm{" +
                "products=" + products +
                '}';
    }
}
