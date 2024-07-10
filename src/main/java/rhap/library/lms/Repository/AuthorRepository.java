package rhap.library.lms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rhap.library.lms.Model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    public Author findByName(String name);
}
