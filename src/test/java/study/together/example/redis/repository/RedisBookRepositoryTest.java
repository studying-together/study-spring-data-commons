package study.together.example.redis.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.together.example.redis.model.Book;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisBookRepositoryTest {

    @Autowired
    private RedisBookRepository redisBookRepository;

    @Test
    void crud() {
        String id = "id1";
        String bookName = "book_name";

        Book book = new Book(id, bookName, "description", 10000);
        redisBookRepository.save(book);

        Book savedBook = redisBookRepository.findById(id).get();
        assertEquals(bookName, savedBook.getName());

        Book savedBookByName = redisBookRepository.findByName(bookName);
        assertEquals(savedBookByName, savedBook);
        redisBookRepository.findAll().forEach(book1 -> {
            System.out.println(book1.getId());
        });
    }
}