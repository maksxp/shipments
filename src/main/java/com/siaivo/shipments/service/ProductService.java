package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Product;

import java.util.List;

public interface ProductService {

    void saveProduct(Product product);

//    void saveProduct(List<Product> products);

    public Product findProductById(int id);
}
