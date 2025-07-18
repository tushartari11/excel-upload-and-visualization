package com.rekreation.learning.vaadin.ui.view.list;


import com.flowingcode.vaadin.addons.gridexporter.GridExporter;
import com.rekreation.learning.vaadin.backend.InvoiceCombinedDTO;
import com.rekreation.learning.vaadin.service.InvoiceService;
import com.rekreation.learning.vaadin.ui.MainLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Route(value = "invoice-export", layout = MainLayout.class)
@RolesAllowed("ADMIN")
@CssImport("./styles/shared-styles.css")
@PageTitle("Invoices | Sales CRM")
public class ListGridWithExport extends VerticalLayout {

    private InvoiceService invoiceService;
    private Grid<InvoiceCombinedDTO> grid = new Grid<>(InvoiceCombinedDTO.class);

    public ListGridWithExport(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
        setSizeFull();
        addClassName("list-view");
        configureGrid();

        GridExporter<InvoiceCombinedDTO> exporter = GridExporter.createFor(grid, "/custom-template.xlsx", "/custom-template.docx");
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("${date}", new SimpleDateFormat().format(Calendar.getInstance().getTime()));
        exporter.setExportColumn(grid.getColumnByKey("customerName"), true);
        exporter.setExportColumn(grid.getColumnByKey("year"), true);
        exporter.setExportColumn(grid.getColumnByKey("address"), true);
        exporter.setExportColumn(grid.getColumnByKey("city"), true);
        exporter.setExportColumn(grid.getColumnByKey("district"), true);
        exporter.setExportColumn(grid.getColumnByKey("state"), true);
        exporter.setExportColumn(grid.getColumnByKey("country"), true);
        exporter.setExportColumn(grid.getColumnByKey("machineNumber"), true);
        exporter.setExportColumn(grid.getColumnByKey("machineModel"), true);
        exporter.setExportColumn(grid.getColumnByKey("billDate"), true);


        exporter.setAdditionalPlaceHolders(placeholders);
        exporter.setSheetNumber(1);
        exporter.setCsvExportEnabled(false);
        exporter.setDateColumnFormat(grid.getColumnByKey("year"), "MM yyyy");
        exporter.setDateColumnFormat(grid.getColumnByKey("billDate"), "dd/MM/yyyy");
        exporter.setTitle("Invoice information");
        exporter.setFileName("Invoice_Export" + new SimpleDateFormat("yyyyddMM").format(Calendar.getInstance().getTime()));

        add(grid);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("list-view");
        grid.setSizeFull();
        grid.setColumns("customerName",
                "year",
                "address",
                "city",
                "district",
                "state",
                "country",
                "machineNumber",
                "machineModel",
                "billDate");
    }

    private void updateList() {
        log.info("Updating list...");
        List<InvoiceCombinedDTO> invoices = invoiceService.findAll();
        grid.setItems(DataProvider.fromStream(invoices.stream()));
    }
}
