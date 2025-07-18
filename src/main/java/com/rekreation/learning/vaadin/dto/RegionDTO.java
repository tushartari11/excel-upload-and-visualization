package com.rekreation.learning.vaadin.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RegionDTO {

    private Long regionId;
    private String city;
    private String district;
    private String state;
    private String country;
}
