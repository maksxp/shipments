package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    void saveProduct(Product product);

//    void saveProduct(List<Product> products);

    public Product findProductById(int id);

    List <Product> findProductsByContract (Contract contract);

    BigDecimal findWeightOfAllProductsByContract (Contract contract);

    public void deleteProductsByContract (Contract contract);
}
