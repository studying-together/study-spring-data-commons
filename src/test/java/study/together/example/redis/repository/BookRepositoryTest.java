package study.together.example.redis.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.together.example.redis.model.Book;

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

        Book savedBookByName = bookRepository.findByName(bookName);
        assertEquals(savedBookByName, savedBook);
        bookRepository.findAll().forEach(book1 -> {
            System.out.println(book1.getId());
        });
    }
}