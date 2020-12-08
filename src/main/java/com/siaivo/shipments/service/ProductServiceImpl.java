package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Product;
import com.siaivo.shipments.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

//    @Override
//    public void saveProduct(List<Product> products) {
//        products.stream().forEach(product -> productRepository.save(product));
//    }

    @Override
    public Product findProductById(int id) {
        return productRepository.findById(id);
    }

}
