package com.rekreation.learning.vaadin.backend;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "region")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Region {

    @Id
    private Long regionId;
    private String city;
    private String district;
    private String state;
    private String country;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Address> addresses = new ArrayList<>();

    // Helper method to maintain bidirectional relationship
    public void addAddress(Address address) {
        addresses.add(address);
        address.setRegion(this);
    }
}