package com.rekreation.learning.vaadin.ui;

import com.rekreation.learning.vaadin.backend.Book;
import com.rekreation.learning.vaadin.service.BookService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.annotation.security.RolesAllowed;
import org.vaadin.crudui.crud.impl.GridCrud;

@Route("admin")
@RolesAllowed("ADMIN")
public class AdminView extends VerticalLayout {

    public AdminView(BookService bookService) {
        var crud = new GridCrud<>(Book.class, bookService);
        crud.getGrid().setColumns("title", "author", "yearPublished", "pages", "language", "description");
        crud.getCrudFormFactory().setVisibleProperties("title", "author", "yearPublished", "pages", "language", "description");
        crud.setAddOperationVisible(false);
        crud.getCrudLayout().addToolbarComponent(new RouterLink("New Book", NewView.class));
        add(new H1("Admin View"),
                crud);
    }
}
