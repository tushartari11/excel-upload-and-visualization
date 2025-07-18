package com.rekreation.learning.vaadin.service;

import com.rekreation.learning.vaadin.backend.*;
import com.rekreation.learning.vaadin.repository.AddressRepository;
import com.rekreation.learning.vaadin.repository.ClientRepository;
import com.rekreation.learning.vaadin.repository.MachineRepository;
import com.rekreation.learning.vaadin.repository.RegionRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.crudui.crud.CrudListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class InvoiceService implements CrudListener<InvoiceCombinedDTO> {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceService.class);

    private final AddressRepository addressRepository;
    private final ClientRepository clientRepository;
    private final MachineRepository machineRepository;
    private final RegionRepository regionRepository;

    public InvoiceService(
            AddressRepository addressRepository,
            ClientRepository clientRepository,
            MachineRepository machineRepository,
            RegionRepository regionRepository) {
        this.addressRepository = addressRepository;
        this.clientRepository = clientRepository;
        this.machineRepository = machineRepository;
        this.regionRepository = regionRepository;
    }

    public List<InvoiceCombinedDTO> getAllCombinedInvoices() {
        // Fetch all and map
        logger.info("Fetching all combined invoices from repositories");
        List<InvoiceCombinedDTO> combinedList = new ArrayList<>();
//        addressRepository.findAll().forEach(address -> combinedList.add(getInvoiceCombinedDTO(address)));
//        clientRepository.findAll().forEach(client -> combinedList.add(getInvoiceCombinedDTO(client)));
//        machineRepository.findAll().forEach(machine -> combinedList.add(getInvoiceCombinedDTO(machine)));
//        regionRepository.findAll().forEach(region -> combinedList.add(getInvoiceCombinedDTO(region)));
         List<Machine> machines = machineRepository.getAllInvoicesByMachines();
         machines.forEach(machine -> {
                InvoiceCombinedDTO dto = getInvoiceCombinedDTO(machine);
                combinedList.add(dto);
         });

        return combinedList;
    }

//    @Cacheable("combinedInvoices")
//    public Page<InvoiceCombinedDTO> getAllCombinedInvoices(int page, int size, String sortBy) {
//        List<InvoiceCombinedDTO> combinedList = getAllCombinedInvoicesUnpaged(sortBy);
//        int start = Math.min(page * size, combinedList.size());
//        int end = Math.min(start + size, combinedList.size());
//        List<InvoiceCombinedDTO> paged = combinedList.subList(start, end);
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
//        return new PageImpl<>(paged, pageable, combinedList.size());
//    }

    private InvoiceCombinedDTO getInvoiceCombinedDTO(Client client) {
        InvoiceCombinedDTO dto = new InvoiceCombinedDTO();
        dto.setClientId(client.getClientId());
        dto.setYear(client.getYear());
        dto.setCustomerName(client.getCustomerName());
        return dto;
    }

    private InvoiceCombinedDTO getInvoiceCombinedDTO(Address address) {
        InvoiceCombinedDTO dto = new InvoiceCombinedDTO();
        dto.setAddressId(address.getAddressId());
        dto.setAddress(address.getAddress());
        if (address.getRegion() != null) {
            dto.setRegion(address.getRegion().getCity());
            dto.setRegionId(address.getRegion().getRegionId());
            dto.setCity(address.getRegion().getCity());
            dto.setDistrict(address.getRegion().getDistrict());
            dto.setState(address.getRegion().getState());
            dto.setCountry(address.getRegion().getCountry());
        }
        if (address.getClient() != null) {
            dto.setClientId(address.getClient().getClientId());
            dto.setCustomerName(address.getClient().getCustomerName());
        }
        return dto;
    }

    private InvoiceCombinedDTO getInvoiceCombinedDTO(Machine machine) {
        InvoiceCombinedDTO dto = new InvoiceCombinedDTO();
        dto.setMachineId(machine.getMachineId());
        dto.setMachineNumber(machine.getMachineNumber());
        dto.setBillDate(machine.getBillDate());
        if (machine.getClient() != null) {
            dto.setClientId(machine.getClient().getClientId());
            dto.setCustomerName(machine.getClient().getCustomerName());
        }
        return dto;
    }

    private InvoiceCombinedDTO getInvoiceCombinedDTO(Region region) {
        InvoiceCombinedDTO dto = new InvoiceCombinedDTO();
        dto.setRegionId(region.getRegionId());
        dto.setCity(region.getCity());
        dto.setDistrict(region.getDistrict());
        dto.setState(region.getState());
        dto.setCountry(region.getCountry());
        return dto;
    }

//    @Override
//    public List<InvoiceCombinedDTO> findAll() {
//        return getAllCombinedInvoices();
//    }

    /*
    "select c.customer_name, m.machine_number, m.machine_model, m.year, m.bill_date, a.address, r.region, r.district, r.city, r.state, r.country " +
            "from machine m " +
            "join client c on m.client_id = c.client_id " +
            "join address a on c.client_id = a.client_id " +
            "join region r on a.region_id = r.region_id"
     */
    @Override
    public List<InvoiceCombinedDTO> findAll(){
        List<Object[]> rows = machineRepository.fetchInvoiceCombinedRaw();
        List<InvoiceCombinedDTO> result = new ArrayList<>();
        try{
            if (rows == null || rows.isEmpty()) {
                logger.warn("No data found in the database for combined invoices.");
                return result;
            }
            for (Object[] row : rows) {
                InvoiceCombinedDTO dto =  new InvoiceCombinedDTO();
                dto.setCustomerName(getStringOrEmpty(row[0]));
                dto.setMachineNumber(getLongValueFromObject(row[1]));
                dto.setMachineModel(getStringOrEmpty(row[2]) );
                dto.setYear(LocalDate.parse(getStringOrEmpty(row[3])));
                dto.setBillDate(LocalDate.parse(getStringOrEmpty(row[4])));
                dto.setAddress(getStringOrEmpty(row[5]));
                dto.setDistrict(getStringOrEmpty(row[6]));
                dto.setCity(getStringOrEmpty(row[7]));
                dto.setState(getStringOrEmpty(row[8]));
                dto.setCountry(getStringOrEmpty(row[9]));
                result.add( dto);
            }
        } catch (Exception e) {
            logger.error("Error fetching combined invoices: ", e);
            return result;
        }

        return result;
    }

    @Override
    public InvoiceCombinedDTO add(InvoiceCombinedDTO domainObjectToAdd) {
        return null;
    }

    @Override
    public InvoiceCombinedDTO update(InvoiceCombinedDTO domainObjectToUpdate) {
        return null;
    }

    @Override
    public void delete(InvoiceCombinedDTO domainObjectToDelete) {

    }

    private Long getLongValueFromObject(Object value) {
       if (value == null){
           return 0L;
       }else{
           return Long.parseLong(String.valueOf(value));
       }
    }

    public static String getStringOrEmpty(Object value) {
        return value == null ? "" : value.toString();
    }

}
