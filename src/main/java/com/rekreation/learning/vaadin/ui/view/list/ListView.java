package com.rekreation.learning.vaadin.ui.view.list;

import com.rekreation.learning.vaadin.backend.InvoiceCombinedDTO;
import com.rekreation.learning.vaadin.service.InvoiceService;
import com.rekreation.learning.vaadin.ui.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.vaadin.crudui.crud.impl.GridCrud;

@Slf4j
@Route(value = "invoice", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@CssImport("./styles/shared-styles.css")
@PageTitle("Invoices | Sales CRM")
public class ListView extends VerticalLayout {

    private TextField filterText = new TextField();
    private InvoiceService service;

    public ListView(InvoiceService service) {
        this.service = service;
        setSizeFull();
        addClassName("list-view");
        var crud = new GridCrud<>(InvoiceCombinedDTO.class, service);
        crud.getGrid().setColumns(
                "clientId",
                "year",
                "customerName",
                "address",
                "city",
                "district",
                "state",
                "country",
                "machineNumber",
                "machineModel",
                "billDate");
        crud.getCrudFormFactory().setVisibleProperties(
                "clientId",
                "year",
                "customerName",
                "address",
                "city",
                "district",
                "state",
                "country",
                "machineNumber",
                "machineModel",
                "billDate");
        crud.setAddOperationVisible(false);
        add(new H1("Invoice View"), getToolbar(), crud);
        updateList();
    }

    private void configureFilter() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
    }

    private void updateList() {
        log.info("Updating list...");
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        HorizontalLayout toolbar = new HorizontalLayout(filterText);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
}
