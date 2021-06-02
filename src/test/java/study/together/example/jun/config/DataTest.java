package study.together.example.jun.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import study.together.example.jun.model.Author;
import study.together.example.jun.model.Book;
import study.together.example.jun.repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class DataTest {

    @Autowired
    private BookRepository bookRepository;

    Author jun = new Author("jun", 33);
    Author min = new Author("min", 32);

    Book book = new Book(null, "TEST", "Sub", "DESC", 500, min);
    Book book1 = new Book(null, "TEST1", "Sub", "DESC1", 1000, min);
    Book book2 = new Book(null, "TEST2", "Sub", "DESC2", 5000, jun);
    Book book3 = new Book(null, "TEST3", "Sub", "DESC3", 10000, jun);
    Book book4 = new Book(null, "TEST3", "Sub", "DESC4", 10000, min);
    Book book5 = new Book(null, "TEST4", "Sub", "DESC5", 20000, jun);

    @Test
    public void STREAMABLE_TEST() {
        bookRepository.saveAll(Stream.of(book, book1, book2, book3, book4, book5).collect(Collectors.toList()));

        Streamable<Book> books = bookRepository.findByTitleContaining("Su");
        assertEquals(6, books.stream().count());
    }

    @Test
    public void LIMIT_QUERY_TEST() {
        bookRepository.saveAll(Stream.of(book, book1, book2, book3, book4, book5).collect(Collectors.toList()));

        Book book = bookRepository.findTopByOrderByNameDesc();
        assertEquals(book5, book);

        List<Book> twoBook = bookRepository.findFirst2ByTitle("Sub", Sort.by(Sort.Order.desc("name")));
        assertEquals(2, twoBook.size());
    }

    @Test
    public void PROPERTY_TEST() {
        bookRepository.saveAll(Stream.of(book, book1, book2, book3, book4, book5).collect(Collectors.toList()));

        List<Book> junBooks = bookRepository.findByAuthor_Name("jun");
        assertEquals(3, junBooks.size());
    }

    @Test
    public void QUERY_CREATION_TEST() {
        bookRepository.saveAll(Stream.of(book, book1, book2, book3, book4, book5).collect(Collectors.toList()));

        List<Book> testBook = bookRepository.findByNameAndPrice("TEST", 500);
        assertEquals(book, testBook.get(0));

        List<Book> test34Books = bookRepository.findByNameAndPrice("TEST3", 10000);
        assertEquals(2, test34Books.size());

        List<Book> distinctBooks = bookRepository.findDistinctBooksByNameAndPrice("TEST3", 10000);
//        assertEquals(1, distinctBooks.size()); 의문???

        List<Book> test2Books = bookRepository.findByName("test2");
        assertEquals(0, test2Books.size());

        List<Book> test2IgnoreBooks = bookRepository.findByNameIgnoreCase("test2");
        assertEquals(book2, test2IgnoreBooks.get(0));

        List<Book> findNameDescBooks = bookRepository.findByNameAndDesc("test2", "desc2");
        assertEquals(0, findNameDescBooks.size());

        List<Book> findNameDescIgnoreBooks = bookRepository.findByNameAndDescAllIgnoreCase("test2", "desc2");
        assertEquals(book2, findNameDescIgnoreBooks.get(0));

        List<Book> priceAscBooks = bookRepository.findAllByOrderByPriceAsc();
        assertEquals(book, priceAscBooks.get(0));

        List<Book> priceDescBooks = bookRepository.findAllByOrderByPriceDesc();
        assertEquals(book5, priceDescBooks.get(0));
    }

    @Test
    public void CRUD_TEST() {
        // save
        Book savedBook = bookRepository.save(book);

        // saveAll
        List<Book> savedBooks = bookRepository.saveAll(Stream.of(book1, book2, book3).collect(Collectors.toList()));
        assertSame(3, savedBooks.size());

        // findById
        Book findBook = bookRepository.findById(savedBook.getId()).orElse(null);
        assertEquals(book, findBook);

        // existsById
        boolean isExist = bookRepository.existsById(savedBooks.get(0).getId());
        assertTrue(isExist);

        // findAll
        List<Book> allBooks = bookRepository.findAll();
        assertSame(4, allBooks.size());

        Iterable<String> bookIds = savedBooks.stream().map(Book::getId).collect(Collectors.toList());
        // findAllById
        List<Book> findBooks = (List<Book>) bookRepository.findAllById(bookIds);
        assertSame(3, findBooks.size());

        // count
        long allCount = bookRepository.count();
        assertSame(4L, allCount);

        // deleteById
        bookRepository.deleteById(findBook.getId());
        assertSame(3L, bookRepository.count());

        // delete
        bookRepository.delete(findBooks.get(1));
        assertSame(2L, bookRepository.count());

        // deleteAll
        bookRepository.deleteAll(findBooks);
        assertSame(0L, bookRepository.count());

        bookRepository.save(book);
        bookRepository.saveAll(Stream.of(book1, book2, book3).collect(Collectors.toList()));

        assertSame(4L, bookRepository.count());
        // deleteAll
        bookRepository.deleteAll();
        assertSame(0L, bookRepository.count());
    }

    @Test
    public void SortNPage_TEST() {
        bookRepository.saveAll(Stream.of(book, book1, book2, book3).collect(Collectors.toList()));

        List<Book> priceOrderBooks = bookRepository.findAll(Sort.by(Sort.Direction.DESC, "price"));
        assertEquals(10000, priceOrderBooks.get(0).getPrice());
        assertEquals(500, priceOrderBooks.get(3).getPrice());

        Page<Book> book1Page = bookRepository.findAll(PageRequest.of(0, 4));
        Page<Book> book2Page = bookRepository.findAll(PageRequest.of(1, 2));

        // 전체 데이터 수 조회
        assertEquals(4, book1Page.getTotalElements());
        // 전체 페이지 수 조회
        assertEquals(1, book1Page.getTotalPages());
        assertEquals(4, book2Page.getTotalElements());
        assertEquals(2, book2Page.getTotalPages());
        // 페이지 데이터 조회
        assertEquals(4L, book1Page.stream().count());
        assertEquals(2L, book2Page.stream().count());

        Slice<Book> boo32Page = bookRepository.findAll(PageRequest.of(1, 2));
    }

}
