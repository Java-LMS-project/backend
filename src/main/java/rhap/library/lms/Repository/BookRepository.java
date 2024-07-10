package rhap.library.lms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rhap.library.lms.Model.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    public Book findByTitle(String title);
    public List<Book> findByUserEmail(String email);
    public Book findById(long id);
}
