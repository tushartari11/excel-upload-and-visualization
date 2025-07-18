package com.rekreation.learning.vaadin.backend;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    @OneToMany(mappedBy = "client")
    private List<Machine> machines;

    // getters and setters
}