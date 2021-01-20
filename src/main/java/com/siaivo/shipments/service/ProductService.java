package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    void saveProduct(Product product);

    List <Product> findProductsByContract (Contract contract);

    BigDecimal findWeightOfAllProductsByContract (Contract contract);

    void deleteProductsByContract(Contract contract);

    void deleteProduct (Product product);
}
