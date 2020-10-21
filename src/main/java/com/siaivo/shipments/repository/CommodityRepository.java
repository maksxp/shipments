package com.siaivo.shipments.repository;

import com.siaivo.shipments.model.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("commodityRepository")
public interface CommodityRepository extends JpaRepository<Commodity, Integer>{
    Commodity findByCommodityName(String commodityName);
    Commodity findById(int id);
}