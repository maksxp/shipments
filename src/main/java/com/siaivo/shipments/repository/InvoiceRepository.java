package com.siaivo.shipments.repository;

import com.siaivo.shipments.model.Invoice;
import org.springframework.stereotype.Repository;

@Repository("invoiceRepository")
public interface InvoiceRepository {
    Invoice findById(int id);
    Invoice findByInvoiceNumber(String invoiceNumber);
    Invoice findByIsInvoicePaid(boolean isInvoicePaid);
}
