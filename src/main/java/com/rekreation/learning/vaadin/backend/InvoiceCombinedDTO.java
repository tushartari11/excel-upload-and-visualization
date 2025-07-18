package com.rekreation.learning.vaadin.backend;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class InvoiceCombinedDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    // Client fields
    private Long clientId;
    private LocalDate year;
    private String customerName;

    // Address fields
    private Long addressId;
    private String address;

    // Region fields
    private String region;
    private Long regionId;
    private String city;
    private String district;
    private String state;
    private String country;

    // Machine fields
    private Long machineId;
    private Long machineNumber;
    private String machineModel;
    private LocalDate billDate;
}