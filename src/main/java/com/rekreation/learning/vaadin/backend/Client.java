package com.rekreation.learning.vaadin.backend;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Client {

    @Id
    private Long clientId;
    private LocalDate year;
    private String customerName;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Machine> machines = new ArrayList<>();

    // getters and setters

    public void addAddress(Address address) {
        addresses.add(address);
        address.setClient(this);
    }

    public void addMachines(Machine machine) {
        machines.add(machine);
        machine.setClient(this);
    }

    // getters and setters
}