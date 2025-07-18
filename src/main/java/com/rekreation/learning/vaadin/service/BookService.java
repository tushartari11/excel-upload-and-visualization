package com.rekreation.learning.vaadin.service;

import com.rekreation.learning.vaadin.backend.Book;
import com.rekreation.learning.vaadin.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.vaadin.crudui.crud.CrudListener;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookService implements CrudListener<Book> {

    private final BookRepository bookRepository;

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book add(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book update(Book book) {
        // Logic to update an existing book
        return bookRepository.save(book);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }
}
