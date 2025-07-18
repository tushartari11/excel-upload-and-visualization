package com.rekreation.learning.vaadin.excelhelper;

import com.rekreation.learning.vaadin.backend.Address;
import com.rekreation.learning.vaadin.backend.Client;
import com.rekreation.learning.vaadin.backend.Machine;
import com.rekreation.learning.vaadin.backend.Region;
import com.rekreation.learning.vaadin.dto.AddressDTO;
import com.rekreation.learning.vaadin.dto.ClientDTO;
import com.rekreation.learning.vaadin.dto.MachineDTO;
import com.rekreation.learning.vaadin.dto.RegionDTO;
import com.rekreation.learning.vaadin.repository.AddressRepository;
import com.rekreation.learning.vaadin.repository.ClientRepository;
import com.rekreation.learning.vaadin.repository.MachineRepository;
import com.rekreation.learning.vaadin.repository.RegionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ExcelImporter {

    private final MachineRepository machineRepository;
    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final RegionRepository regionRepository;

    private List<MachineDTO> machineList;
    private List<ClientDTO> clientList;
    private List<AddressDTO> addressList;
    private List<RegionDTO> regionList;

    public ExcelImporter(MachineRepository machineRepository, ClientRepository clientRepository,
                         AddressRepository addressRepository, RegionRepository regionRepository) {
        this.machineRepository = machineRepository;
        this.clientRepository = clientRepository;
        this.addressRepository = addressRepository;
        this.regionRepository = regionRepository;
    }

    public void importAllSheets(InputStream is) throws Exception {
        Workbook workbook = new XSSFWorkbook(is);

        for (Sheet sheet : workbook) {
            String tableName = sheet.getSheetName();
            // Example: switch by table name, map rows to entities, and save
            switch (tableName) {
                case "Address":
                    importAddressSheet(sheet);
                    break;
                case "Client":
                    importClientSheet(sheet);
                    break;
                case "Region":
                    importRegionSheet(sheet);
                    break;
                case "Machine":
                    importMachineSheet(sheet);
                    break;
                default:
                    System.out.println("Skipping unknown sheet: " + tableName);
                    break;
            }
        }
        workbook.close();
    }

    private void importAddressSheet(Sheet sheet) {
        addressList = new ArrayList<>();
        XSSFSheet dataSheet = (XSSFSheet) sheet;
        dataSheet.forEach(row -> {
            if (row.getRowNum() != 0) {

                // Check if the row is empty (all relevant cells are empty)
                boolean isEmpty = (row.getCell(0) == null || row.getCell(0).toString().trim().isEmpty()) &&
                        (row.getCell(1) == null || row.getCell(1).toString().trim().isEmpty()) &&
                        (row.getCell(2) == null || row.getCell(2).toString().trim().isEmpty());
                if (isEmpty) return;

                AddressDTO address = new AddressDTO();
                address.setClientNumber(getLongValueFromCellNullSafe(row.getCell(0)));
                address.setAddress(getStringValueFromExcel(row.getCell(1)));
                address.setRegionId(getLongValueFromCellNullSafe(row.getCell(2)));
                addressList.add(address);
            }
        });
//         log.info("Importing addresses from Excel file:" + addressList);
    }

    private void importClientSheet(Sheet sheet) {
        clientList = new ArrayList<>();
        XSSFSheet dataSheet = (XSSFSheet) sheet;
        dataSheet.forEach(row -> {
            if (row.getRowNum() != 0) {
                ClientDTO client = new ClientDTO();
                client.setClientId(getLongValueFromCellNullSafe(row.getCell(0)));
                client.setYear(toLocalDateNullSafe(getLocalDateTimeFromCell(row.getCell(1))));
                client.setCustomerName(getStringValueFromExcel(row.getCell(2)));
                clientList.add(client);
            }
        });
//         log.info("Importing clientList from Excel file: " + clientList);
    }

    private void importRegionSheet(Sheet sheet) {
        regionList = new ArrayList<>();
        XSSFSheet dataSheet = (XSSFSheet) sheet;
        dataSheet.forEach(row -> {
            if (row.getRowNum() != 0) {
                RegionDTO regionDTO = new RegionDTO();
                regionDTO.setRegionId(getLongValueFromCellNullSafe(row.getCell(0)));
                regionDTO.setCity(getStringValueFromExcel(row.getCell(1)));
                regionDTO.setDistrict(getStringValueFromExcel(row.getCell(2)));
                regionDTO.setState(getStringValueFromExcel(row.getCell(3)));
                regionDTO.setCountry(getStringValueFromExcel(row.getCell(4)));
                regionList.add(regionDTO);
            }
        });
//        log.info("Importing regionList from Excel file: " + regionList);
    }

    private void importMachineSheet(Sheet sheet) {
        machineList = new ArrayList<>();
        XSSFSheet dataSheet = (XSSFSheet) sheet;
        dataSheet.forEach(row -> {
            if (row.getRowNum() != 0) {
                MachineDTO machineDTO = new MachineDTO();
                machineDTO.setClientNumber(getLongValueFromCellNullSafe(row.getCell(0)));
                machineDTO.setYear(toLocalDateNullSafe(getLocalDateTimeFromCell(row.getCell(1))));
                machineDTO.setBillDate(toLocalDateNullSafe(getLocalDateTimeFromCell(row.getCell(2))));
                machineDTO.setMachineNumber(getLongValueFromCellNullSafe(row.getCell(3)));
                machineDTO.setMachineModel(getStringValueFromExcel(row.getCell(4)));
                machineList.add(machineDTO);
            }
        });
//        log.info("Importing machineList from Excel file: " + machineList);
    }

    public static LocalDate toLocalDateNullSafe(LocalDateTime dateTime) {
        return dateTime == null ? null : dateTime.toLocalDate();
    }

    public static LocalDateTime getLocalDateTimeFromCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue();
        }
        return null;
    }

    private Long getLongValueFromCellNullSafe(Cell cell) {
        Long machineNumber = 0L;
        if (cell == null) {
            return 0L;
        }
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    machineNumber = (long) cell.getNumericCellValue();
                    break;
                case STRING:
                    machineNumber = Long.parseLong(cell.getStringCellValue());
                    break;
                default:
                    machineNumber = null; // or handle error
            }
        } catch (NumberFormatException e) {
            log.error("Invalid machine number format: " + e);
        }
        return machineNumber;
    }

    private String getStringValueFromExcel(Cell cell) {
        String stringValueFromCell = "UNKNOWN";
        if (cell == null) {
            return stringValueFromCell;
        }
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    stringValueFromCell = String.valueOf(cell.getNumericCellValue());
                    break;
                case STRING:
                    stringValueFromCell = cell.getStringCellValue();
                    break;
                default:
                    stringValueFromCell = "UNKNOWN_VALUE"; // or handle error
            }
        } catch (Exception e) {
            log.error("Invalid value: " + e);
        }
        return stringValueFromCell;
    }

//    @Transactional
//    public List<Address> saveAllData() {
//        List<Address> addresses = new ArrayList<>();
//        for (AddressDTO addressDTO : addressList) {
//            RegionDTO regionDTO = regionList.stream()
//                    .filter(r -> r.getRegionId().equals(addressDTO.getRegionId()))
//                    .findFirst().orElse(null);
//
//            Region region = null;
//            if (regionDTO != null) {
//                region = new Region();
//                region.setCity(regionDTO.getCity());
//                region.setDistrict(regionDTO.getDistrict());
//                region.setState(regionDTO.getState());
//                region.setCountry(regionDTO.getCountry());
//            }
//            regionRepository.save(region); // Save region first
//
//            //get client from addressDTO and save it
//            ClientDTO clientDTO = clientList.stream()
//                    .filter(c -> c.getClientId().equals(addressDTO.getClientNumber()))
//                    .findFirst().orElse(null);
//
//
//            Client clientEntity = new Client();
//            clientEntity.setClientId(clientDTO.getClientId());
//            clientEntity.setYear(clientDTO.getYear());
//            clientEntity.setCustomerName(clientDTO.getCustomerName());
//            clientRepository.save(clientEntity); // Save client first
//
//            Address addressEntity = new Address();
//            addressEntity.setAddress(addressDTO.getAddress());
//            addressEntity.setClient(clientEntity); // Assuming Client is set here
//            addressEntity.setRegion(region);
//            addressRepository.save(addressEntity);
//
//            MachineDTO machineDTO = machineList.stream()
//                    .filter(m -> m.getClientNumber().equals(clientDTO.getClientId()))
//                    .findFirst().orElse(null);
//
//            Machine machineEntity = new Machine();
//            machineEntity.setMachineNumber(machineDTO.getMachineNumber());
//            machineEntity.setYear(machineDTO.getYear());
//            machineEntity.setBillDate(machineDTO.getBillDate());
//            machineEntity.setMachineModel(machineDTO.getMachineModel());
//            machineEntity.setClient(clientEntity);
//            machineRepository.save(machineEntity);
//
//        }
//        return addresses;
//    }

//    public List<Client> mapClientDTOsToEntities() {
//        List<Address> addresses = mapAddressDTOsToEntities();
//        addressRepository.saveAll(addresses); // Save addresses first
//        // log.info("Saved " + addresses.size() + " addresses into excel");
//
//        List<Client> clients = new ArrayList<>();
//        for (ClientDTO clientDTO : clientList) {
//            List<Address> clientAddresses = addresses.stream()
//                    .filter(a -> a.getClient().getClientId().equals(clientDTO.getClientId()))
//                    .toList();
//
//            Client client = new Client();
//            client.setClientId(clientDTO.getClientId());
//            client.setYear(clientDTO.getYear());
//            client.setCustomerName(clientDTO.getCustomerName());
//            client.setAddresses(clientAddresses); // Assuming List<Address> in Client
//
//            clients.add(client);
//        }
//        return clients;
//    }
//
//    public List<Machine> mapMachineDTOsToEntities() {
//        List<Client> clients = mapClientDTOsToEntities();
//        clientRepository.saveAll(clients); // Save clients first
//
//        List<Machine> machines = new ArrayList<>();
//        for (MachineDTO machineDTO : machineList) {
//            Client client = clients.stream()
//                    .filter(c -> c.getClientId().equals(machineDTO.getClientNumber()))
//                    .findFirst().orElse(null);
//
//            Machine machine = new Machine();
//            machine.setMachineNumber(machineDTO.getMachineNumber());
//            machine.setYear(machineDTO.getYear());
//            machine.setBillDate(machineDTO.getBillDate());
//            machine.setMachineModel(machineDTO.getMachineModel());
//            machine.setClient(client);
//
//            machines.add(machine);
//        }
//        return machines;
//    }

//    @Transactional
//    public void saveAllData() {
//        List<Machine> machines = mapMachineDTOsToEntities();
//        machineRepository.saveAll(machines);
//        log.info("Saved " + machines.size() + " machines into excel");
//    }

    @Transactional
    public void importSampleData() {
        // Clear existing data (optional)
        machineRepository.deleteAll();
        addressRepository.deleteAll();
        clientRepository.deleteAll();
        regionRepository.deleteAll();

        List<Region> regions = regionList.stream().map(regionDTO -> {
            Region region = new Region();
            //region.setRegionId(regionDTO.getRegionId());
            region.setCity(regionDTO.getCity());
            region.setDistrict(regionDTO.getDistrict());
            region.setState(regionDTO.getState());
            region.setCountry(regionDTO.getCountry());
            return region;
        }).toList();

        regionRepository.saveAll(regions);
        log.info("Saved " + regions.size() + " regions into excel");

        List<Client> clients = clientList.stream().map(clientDTO -> {
            Client client = new Client();
            client.setClientId(clientDTO.getClientId());
            client.setYear(clientDTO.getYear());
            client.setCustomerName(clientDTO.getCustomerName());
            List<Address> clientAddresses = getAddressesForClient(clientDTO);
            client.setAddresses(clientAddresses);

            return client;
        }).toList();

        clientRepository.saveAll(clients);
        log.info("Saved " + clients.size() + " clients into excel");

        List<Machine> machines = machineList.stream().map(machineDTO -> {
            Machine machine = new Machine();
            machine.setMachineNumber(machineDTO.getMachineNumber());
            machine.setYear(machineDTO.getYear());
            machine.setBillDate(machineDTO.getBillDate());
            machine.setMachineModel(machineDTO.getMachineModel());

            Client client = clients.stream()
                    .filter(c -> c.getClientId().equals(machineDTO.getClientNumber()))
                    .findFirst().orElse(null);
            machine.setClient(client);

            return machine;
        }).toList();
        machineRepository.saveAll(machines);
        log.info("Saved " + machines.size() + " machines into excel");
    }

    @NotNull
    private List<Address> getAddressesForClient(ClientDTO clientDTO) {
        List<Address> clientAddresses = new ArrayList<>();
        try {
            clientAddresses = addressList.stream()
                    .filter(a -> a.getClientNumber()!= null)
                    .filter(a ->  a.getClientNumber().equals(clientDTO.getClientId()))
                    .map(addressDTO -> {
                        Address address = new Address();
                        //address.setAddressId(addressDTO.getAddressId());
                        address.setAddress(addressDTO.getAddress());
                        regionRepository.findById(addressDTO.getRegionId())
                                .ifPresent(region -> address.setRegion(region));

                        return address;
                    }).toList();
        } catch (Exception e) {
            log.error("Error getting addresses for client: " + clientDTO.getClientId(), e);
        }

        return clientAddresses;
    }
}