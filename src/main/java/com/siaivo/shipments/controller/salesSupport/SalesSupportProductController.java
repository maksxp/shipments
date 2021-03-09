package com.siaivo.shipments.controller.salesSupport;

import com.siaivo.shipments.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SalesSupportProductController {

    @Autowired
    private ProductService productService;
}
