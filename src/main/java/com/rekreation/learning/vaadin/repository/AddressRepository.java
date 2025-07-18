package com.rekreation.learning.vaadin.repository;


import com.rekreation.learning.vaadin.backend.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}