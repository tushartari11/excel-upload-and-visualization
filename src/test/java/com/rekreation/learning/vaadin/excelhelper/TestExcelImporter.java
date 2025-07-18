package com.rekreation.learning.vaadin.excelhelper;


import com.rekreation.learning.vaadin.repository.AddressRepository;
import com.rekreation.learning.vaadin.repository.ClientRepository;
import com.rekreation.learning.vaadin.repository.MachineRepository;
import com.rekreation.learning.vaadin.repository.RegionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.MockitoAnnotations.openMocks;


public class TestExcelImporter {

    private ExcelImporter excelImporter;

    @Mock
    private MachineRepository machineRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private RegionRepository regionRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
        machineRepository = org.mockito.Mockito.mock(MachineRepository.class);
        clientRepository = org.mockito.Mockito.mock(ClientRepository.class);
        addressRepository = org.mockito.Mockito.mock(AddressRepository.class);
        regionRepository = org.mockito.Mockito.mock(RegionRepository.class);

        excelImporter = new ExcelImporter(machineRepository, clientRepository, addressRepository, regionRepository);
    }

    @Test
    void testImportAllSheets_NormalCase() throws Exception {
        InputStream is = new ClassPathResource("Invoice.xlsx").getInputStream();
        assertDoesNotThrow(() -> excelImporter.importAllSheets(is));
        assertDoesNotThrow(() -> excelImporter.importSampleData());
    }

}