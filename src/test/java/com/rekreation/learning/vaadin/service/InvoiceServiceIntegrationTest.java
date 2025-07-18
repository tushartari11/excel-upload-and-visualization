package com.rekreation.learning.vaadin.service;

import com.rekreation.learning.vaadin.backend.InvoiceCombinedDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class InvoiceServiceIntegrationTest {

    @Autowired
    private InvoiceService invoiceService;

    @Test
    void testFindAllReturnsCombinedInvoices() {
        List<InvoiceCombinedDTO> invoices = invoiceService.findAll();

        // Basic assertions (adjust as per your test data)
        assertThat(invoices).isNotNull();
        assertThat(invoices.size()).isGreaterThanOrEqualTo(0);

        if (!invoices.isEmpty()) {
            InvoiceCombinedDTO dto = invoices.get(0);
            assertThat(dto.getCustomerName()).isNotNull();
            assertThat(dto.getMachineNumber()).isNotNull();
            // Add more assertions as needed
        }
    }
}