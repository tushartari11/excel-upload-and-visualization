package com.rekreation.learning.vaadin.excelhelper;


import com.rekreation.learning.vaadin.backend.Address;
import com.rekreation.learning.vaadin.backend.Client;
import com.rekreation.learning.vaadin.backend.Machine;
import com.rekreation.learning.vaadin.backend.Region;
import com.rekreation.learning.vaadin.repository.AddressRepository;
import com.rekreation.learning.vaadin.repository.ClientRepository;
import com.rekreation.learning.vaadin.repository.MachineRepository;
import com.rekreation.learning.vaadin.repository.RegionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class EntityIntegrationTest {

    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private MachineRepository machineRepository;

    @Test
    void saveOneInstanceOfEachEntity() {
        // 1. Save Region
        Region region = new Region();
        region.setCity("TestCity");
        region.setDistrict("TestDistrict");
        region.setState("TestState");
        region.setCountry("TestCountry");
        region = regionRepository.save(region);

        // 2. Save Client
        Client client = new Client();
        client.setCustomerName("Test Client");
        client.setYear(LocalDate.of(2024, 1, 1));
        client = clientRepository.save(client);

        // 3. Save Address
        Address address = new Address();
        address.setAddress("123 Test St");
        address.setRegion(region);
        address.setClient(client);
        address = addressRepository.save(address);

        // 4. Save Machine
        Machine machine = new Machine();
        machine.setMachineNumber(1001L);
        machine.setYear(LocalDate.of(2024, 1, 1));
        machine.setBillDate(LocalDate.of(2024, 2, 1));
        machine.setMachineModel("ModelX");
        machine.setClient(client);
        machine = machineRepository.save(machine);

        // Assertions
        assertThat(region.getRegionId()).isNotNull();
        assertThat(client.getClientId()).isNotNull();
        assertThat(address.getAddressId()).isNotNull();
        assertThat(machine.getMachineId()).isNotNull();
    }
}
