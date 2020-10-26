package com.siaivo.shipments.service;

import com.siaivo.shipments.model.Commodity;
import java.util.List;

public interface CommodityService {

    void saveCommodity(Commodity commodity);
    List<Commodity> allCommodities();
    Commodity findCommodityByCommodityName (String commodityName);
    Commodity findCommodityById (int id);

}
