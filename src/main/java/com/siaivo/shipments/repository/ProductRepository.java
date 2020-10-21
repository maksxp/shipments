package com.siaivo.shipments.repository;

import com.siaivo.shipments.model.Commodity;
import com.siaivo.shipments.model.Product;
import org.springframework.stereotype.Repository;

@Repository("productRepository")
public interface ProductRepository {

    Product findById(int id);
    Product findByCommodity(Commodity commodity);
    Product findByPrice(double price);
    Product findByQuantity(double quantity);
    Product findByPackaging(String packaging);
}
