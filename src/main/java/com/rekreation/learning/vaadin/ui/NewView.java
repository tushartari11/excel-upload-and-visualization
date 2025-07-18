package com.rekreation.learning.vaadin.ui;

import com.rekreation.learning.vaadin.backend.Book;
import com.rekreation.learning.vaadin.service.BookService;
import com.vaadin.collaborationengine.CollaborationBinder;
import com.vaadin.collaborationengine.CollaborationMessageInput;
import com.vaadin.collaborationengine.CollaborationMessageList;
import com.vaadin.collaborationengine.UserInfo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Route("new")
@RolesAllowed("ADMIN")
public class NewView extends VerticalLayout {

    private TextField title = new TextField("Title");

    private TextField author = new TextField("Author");
    private DatePicker yearPublished = new DatePicker("Year Published");
    private IntegerField pages = new IntegerField("Pages");
    private TextField language = new TextField("Language");
    private TextField description = new TextField("Description");

    public NewView(BookService bookService) {
//        var binder = new Binder<>(Book.class);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        var userInfo = new UserInfo(userName, userName);

        var binder = new CollaborationBinder<>(Book.class, userInfo);
        binder.bindInstanceFields(this);
        binder.setTopic("new-book", Book::new);

        var messageList = new CollaborationMessageList(userInfo, "new-book");
        add(
                new H1("New Book"),
                new HorizontalLayout(
                     new VerticalLayout(
                             new FormLayout(title, author, yearPublished, pages, language, description),
                             new Button("Save", event -> {
                                 var book = new Book();
                                 binder.writeBeanIfValid(book);
                                 bookService.add(book);
                                 Notification.show("Book saved: " + book.getTitle(), 3000, Notification.Position.MIDDLE);
                                 binder.reset(new Book());
                                 })
                     ),
                     new VerticalLayout(
                             messageList,
                             new CollaborationMessageInput(messageList)
                     )
                )
        );
    }
}
