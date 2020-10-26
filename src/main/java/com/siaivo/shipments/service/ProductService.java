package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Product;

public interface ProductService {

    void saveProduct(Product product);
    public Product findProductById(int id);
}
