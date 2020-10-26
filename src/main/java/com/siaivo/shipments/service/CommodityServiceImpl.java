package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Commodity;
import com.siaivo.shipments.repository.CommodityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("commodityService")
public class CommodityServiceImpl implements CommodityService{
    @Autowired
    private CommodityRepository commodityRepository;

    @Override
    public void saveCommodity(Commodity commodity) {
        commodityRepository.save(commodity);
    }

    @Override
    public List<Commodity> allCommodities(){
        return commodityRepository.findAll();
    }

    @Override
    public Commodity findCommodityByCommodityName(String commodityName) {
        return commodityRepository.findByCommodityName(commodityName);
    }

    @Override
    public Commodity findCommodityById(int id) {
        return commodityRepository.findById(id);
    }

}
