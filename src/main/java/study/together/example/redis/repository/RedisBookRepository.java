package study.together.example.redis.repository;

import org.springframework.data.repository.CrudRepository;
import study.together.example.redis.model.Book;

interface RedisBookRepository extends CrudRepository<Book, String> {
    Book findByName(String name);
}
