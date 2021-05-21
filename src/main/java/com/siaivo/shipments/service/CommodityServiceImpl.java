package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Commodity;
import com.siaivo.shipments.repository.CommodityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("commodityService")
public class CommodityServiceImpl implements CommodityService{

    @Autowired
    @Qualifier ("commodityRepository")
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
    public Commodity findCommodityByCommodityCode(String commodityCode) {
        return commodityRepository.findByCommodityCode(commodityCode);
    }

    @Override
    public Commodity findCommodityById(int id) {
        return commodityRepository.findById(id);
    }

}
