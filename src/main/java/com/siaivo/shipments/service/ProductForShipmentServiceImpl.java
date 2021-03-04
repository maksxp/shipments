package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.Product;
import com.siaivo.shipments.model.ProductForShipment;
import com.siaivo.shipments.model.Shipment;
import com.siaivo.shipments.repository.ProductForShipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productForShipmentService")
public class ProductForShipmentServiceImpl implements ProductForShipmentService {

    @Qualifier("productForShipmentRepository")
    @Autowired
    private ProductForShipmentRepository productForShipmentRepository;

    @Override
    public void saveProductForShipment(ProductForShipment productForShipment) {
        productForShipmentRepository.save(productForShipment);
    }

    @Override
    public ProductForShipment findById(int id) {
        return productForShipmentRepository.findById(id);
    }

//    @Override
//    public List<ProductForShipment> findByContract(Contract contract) {
//        return productForShipmentRepository.findByContract(contract);
//    }
//
    @Override
    public List<ProductForShipment> findByShipment(Shipment shipment) {
        return productForShipmentRepository.findByShipment(shipment);
    }

    @Override
    public void deleteProductForShipment(ProductForShipment productForShipment) {
        productForShipmentRepository.delete(productForShipment);
    }
}
