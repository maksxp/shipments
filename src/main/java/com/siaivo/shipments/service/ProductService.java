package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    Product findById(int id) ;

    void saveProduct(Product product);

    List <Product> findProductsByContract (Contract contract);

    List<Product> allProducts();

    BigDecimal findWeightOfAllProductsByContract (Contract contract);

    void deleteProductsByContract(Contract contract);

    void deleteProductAndShipments(Product product);

    void deleteProductAndProductsForShipments(Product product);

    void deleteNotLoadedProduct (Product product);
}
