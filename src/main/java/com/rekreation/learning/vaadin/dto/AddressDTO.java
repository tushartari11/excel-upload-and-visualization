package com.rekreation.learning.vaadin.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AddressDTO {

    private Long clientNumber;
    private String address;
    private Long regionId;
}
