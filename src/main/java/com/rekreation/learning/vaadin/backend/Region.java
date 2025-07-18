package com.rekreation.learning.vaadin.backend;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "region")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regionId;
    private String city;
    private String district;
    private String state;
    private String country;

    @OneToMany(mappedBy = "region")
    private List<Address> addresses;

    // getters and setters
}