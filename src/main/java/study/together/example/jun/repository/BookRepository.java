package study.together.example.jun.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.util.Streamable;
import study.together.example.jun.model.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {

    List<Book> findByName(String name);

    List<Book> findByNameAndPrice(String name, int price);

    List<Book> findDistinctBooksByNameAndPrice(String name, int price);

    List<Book> findByNameIgnoreCase(String name);

    List<Book> findByNameAndDesc(String name, String desc);

    List<Book> findByNameAndDescAllIgnoreCase(String name, String desc);

    List<Book> findAllByOrderByPriceAsc();

    List<Book> findAllByOrderByPriceDesc();

    List<Book> findByAuthor_Name(String name);

    /* LIMIT */
    Book findFirstByOrderByNameAsc();

    Book findTopByOrderByNameDesc();

    Page<Book> queryFirst3ByTitle(String title, Pageable pageable);

    Slice<Book> findTop3ByTitle(String title, Pageable pageable);

    List<Book> findFirst2ByTitle(String title, Sort sort);

    List<Book> findTop2ByTitle(String title, Pageable pageable);

    /* Collection */
    Streamable<Book> findByTitleContaining(String title);

}
