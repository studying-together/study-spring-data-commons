package study.together.example.jun.repository;

import org.springframework.data.repository.CrudRepository;
import study.together.example.jun.model.Book;

interface BookRepository extends CrudRepository<Book, String> {
    Book findByName(String name);
}
