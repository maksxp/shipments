package com.siaivo.shipments.repository;

import com.siaivo.shipments.model.Contract;
import com.siaivo.shipments.model.ProductForShipment;
import com.siaivo.shipments.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("productForShipmentRepository")
public interface ProductForShipmentRepository extends JpaRepository<ProductForShipment, Integer> {

    ProductForShipment findById(int id);
//    List <ProductForShipment> findByContract(Contract contract);
    List <ProductForShipment> findByShipment(Shipment shipment);
}
