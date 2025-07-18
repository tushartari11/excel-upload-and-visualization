package com.rekreation.learning.vaadin.repository;



import com.rekreation.learning.vaadin.backend.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {}