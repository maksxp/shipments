package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;
import com.siaivo.shipments.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService{
    @Qualifier("productRepository")
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product findById(int id) {
        return productRepository.findById(id);
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }



//    @Override
//    public void saveProduct(List<Product> products) {
//        products.stream().forEach(product -> productRepository.save(product));
//    }

//    @Override
//    public Product findById(int id) {
//        return productRepository.findById(id);
//    }

    @Override
    public List<Product> findProductsByContract(Contract contract) {
        return productRepository.findByContract(contract);
    }

    @Override
    public List<Product> allProducts() {
        return productRepository.findAll();
    }

    @Override
    public BigDecimal findWeightOfAllProductsByContract(Contract contract) {
        List <BigDecimal> listOfEachProductWeight = new ArrayList<>();
        findProductsByContract(contract).forEach(product -> listOfEachProductWeight.add(product.getQuantity()));
        return listOfEachProductWeight.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public void deleteProductsByContract(Contract contract) {
        productRepository.deleteAll(productRepository.findByContract(contract));
    }

    @Override
    public void deleteProduct(Product product) {
        productRepository.delete(product);
    }

    @Override
    public void deleteNotLoadedProduct(Product product) {
        if (product.getLoadedQuantity().compareTo(BigDecimal.ZERO)==0){
            productRepository.delete(product);
        }
    }




}
