package com.siaivo.shipments.repository;

import com.siaivo.shipments.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Integer>{
    Product findByProductName(String productName);
    Product findById(int id);

}
