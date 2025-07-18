package com.rekreation.learning.vaadin.repository;

import com.rekreation.learning.vaadin.backend.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Long> {


    @Query(value = "select m.machine_number, m.machine_model, m.year ,m.bill_date from Machine m",
           nativeQuery = true)
    List<Machine> getAllInvoicesByMachines();

    @Query(value = "select c.customer_name, m.machine_number, m.machine_model, m.year, m.bill_date, a.address, r.district, r.city, r.state, r.country " +
            "from machine m " +
            "join client c on m.client_id = c.client_id " +
            "join address a on c.client_id = a.client_id " +
            "join region r on a.region_id = r.region_id",
            nativeQuery = true)
    List<Object[]> fetchInvoiceCombinedRaw();
}
