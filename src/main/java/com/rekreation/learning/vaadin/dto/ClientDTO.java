package com.rekreation.learning.vaadin.dto;

import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
public class ClientDTO {

    private Long clientId;
    private LocalDate year;
    private String customerName;
}
