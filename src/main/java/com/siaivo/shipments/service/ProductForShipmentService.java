package com.siaivo.shipments.service;

import com.siaivo.shipments.model.ProductForShipment;
import com.siaivo.shipments.model.Shipment;

import java.util.List;

public interface ProductForShipmentService {

    void saveProductForShipment (ProductForShipment productForShipment);

    public ProductForShipment findById(int id);

//    List <ProductForShipment> findByContract (Contract contract);
//
    List <ProductForShipment> findByShipment (Shipment shipment);

//    BigDecimal findWeightOfAllProductsByContract (Contract contract);

//    public void deleteProductsByContract (Contract contract);

    void deleteProductForShipment (ProductForShipment productForShipment);
}
