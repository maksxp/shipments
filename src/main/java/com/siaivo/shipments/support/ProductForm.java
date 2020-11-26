package com.siaivo.shipments.support;

import com.siaivo.shipments.model.Product;

import java.util.ArrayList;

public class ProductForm {
    private ArrayList<Product> productList;

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }
}
