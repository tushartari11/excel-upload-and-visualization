package com.rekreation.learning.vaadin.ui;

import com.rekreation.learning.vaadin.backend.Book;
import com.rekreation.learning.vaadin.service.BookService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.reports.PrintPreviewReport;

@Route("report")
@RolesAllowed("ADMIN")
public class ReportView extends VerticalLayout {

    public ReportView(BookService bookService) {

        var report = new PrintPreviewReport<>(Book.class, "title", "author", "yearPublished", "language", "description");
        report.setItems(bookService.findAll());
        report.getReportBuilder().setTitle("Book Report");

        add(
                report
        );
    }

}
