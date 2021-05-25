package study.together.example.jun.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.together.example.jun.model.Book;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    void crud() {
        String id = "id1";
        String bookName = "book_name";

        Book book = new Book(id, bookName, "description", 10000);
        bookRepository.save(book);

        Book savedBook = bookRepository.findById(id).get();
        assertEquals(bookName, savedBook.getName());

        bookRepository.findAll().forEach(book1 -> {
            System.out.println(book1.getId());
        });
    }
}