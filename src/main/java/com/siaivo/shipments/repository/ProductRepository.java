package com.siaivo.shipments.repository;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("productRepository")
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Product findById(int id);
    List <Product> findProductsByContract(Contract contract);
}
