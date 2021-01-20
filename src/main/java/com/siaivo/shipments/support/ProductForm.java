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

    public void setProductsForEdit(List<Product> products) {
        this.products = products;
        while (products.size()<5) {
            products.add(products.size(), new Product());
            products.get(products.size()-1).setId(0);
        }
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
