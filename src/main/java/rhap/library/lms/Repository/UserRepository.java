package rhap.library.lms.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rhap.library.lms.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);
    User findByEmail(String email);
    User findBykeyValue(String authkeyValue);
}
