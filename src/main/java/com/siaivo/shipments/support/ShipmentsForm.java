package com.siaivo.shipments.support;

import com.siaivo.shipments.model.ProductForShipment;
import com.siaivo.shipments.model.Shipment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ShipmentsForm {
    private List <Shipment> shipments;

    public List<Shipment> getShipments() {
        return shipments;
    }

    public void setShipments(List<Shipment> shipments) {
        this.shipments = shipments;
    }

    public ShipmentsForm (){
        shipments = new ArrayList<>();
    }

    public ShipmentsForm (int numberOfTrucks) {
        this.shipments = IntStream.range(0, numberOfTrucks).mapToObj(truck -> new Shipment()).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "ShipmentsForm{" +
                "shipmentsForm=" + shipments +
                '}';
    }
}
