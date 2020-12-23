package com.siaivo.shipments.support;

import com.siaivo.shipments.model.Shipment;

import java.util.List;

public class ShipmentsForm {
    private List <Shipment> shipmentsForm;

    public List<Shipment> ShipmentsForm() {
        return shipmentsForm;
    }

    public void setShipmentsForm(List<Shipment> shipmentsForm) {
        this.shipmentsForm = shipmentsForm;
    }

    @Override
    public String toString() {
        return "ShipmentsForm{" +
                "shipmentsForm=" + shipmentsForm +
                '}';
    }
}
