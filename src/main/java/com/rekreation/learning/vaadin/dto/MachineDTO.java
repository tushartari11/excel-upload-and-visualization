package com.rekreation.learning.vaadin.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class MachineDTO {

    private Long clientNumber;
    private LocalDate year;
    private LocalDate billDate;
    private Long machineNumber;
    private String machineModel;

}
