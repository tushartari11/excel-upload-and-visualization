package com.rekreation.learning.vaadin.repository;

import com.rekreation.learning.vaadin.backend.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
